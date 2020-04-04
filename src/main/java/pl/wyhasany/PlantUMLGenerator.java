package pl.wyhasany;

import com.structurizr.io.plantuml.C4PlantUMLWriter;
import com.structurizr.io.plantuml.C4PlantUMLWriter.RelationshipModes;
import com.structurizr.io.plantuml.PlantUMLWriter;
import com.structurizr.model.Element;
import com.structurizr.model.Model;
import com.structurizr.model.Relationship;
import com.structurizr.view.RelationshipView;
import com.structurizr.view.View;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.structurizr.io.plantuml.C4PlantUMLWriter.C4_LAYOUT_MODE;
import static com.structurizr.io.plantuml.C4PlantUMLWriter.RelationshipModes.Lay;

class PlantUMLGenerator {

    public static final Pattern PACKAGE_PATTERN = Pattern.compile("^\\s*package\\s*\"(.*)\"\\s*\\{\\s*$");

    private final C4PlantUMLWriter plantUMLWriter;

    private Organization organization;

    PlantUMLGenerator(Organization organization) {
        this.organization = organization;
        plantUMLWriter = new C4PlantUMLWriter();
        new PlantUMLStyling(plantUMLWriter, organization);
    }

    void saveDiagramToFile(Diagram diagram, String fileName) throws IOException {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            View view = diagram.getView(organization);
            plantUMLWriter.write(view, fileWriter);
            fileWriter.flush();
        }
        removeLayouts();
        addLegend(fileName);
        replacePackageWithSystemBondary(fileName);
    }

    private void removeLayouts() {
        try {
            removeLayoutsFromModel();
            removeLayoutRelationshipsPerElement();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private void removeLayoutRelationshipsPerElement() throws NoSuchFieldException, IllegalAccessException {
        Model model = organization.model();
        Set<Element> elements = model.getElements();
        for (Element element : elements) {
            for (Relationship relationship : element.getRelationships()) {
                String layoutMode = relationship.getProperties().get(C4_LAYOUT_MODE);
                if (Lay.name().equals(layoutMode)) {
                    Class<? extends Element> elementClass = Element.class;
                    Field relationshipsField = elementClass.getDeclaredField("relationships");
                    relationshipsField.setAccessible(true);
                    Set<Relationship> relationships = (Set<Relationship>) relationshipsField.get(element);
                    relationships.remove(relationship);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void removeLayoutsFromModel() throws NoSuchFieldException, IllegalAccessException {
        Model model = organization.model();
        Class<? extends Model> modelClass = model.getClass();
        Field relationshipsByIdField = modelClass.getDeclaredField("relationshipsById");
        relationshipsByIdField.setAccessible(true);
        Map<String, Relationship> relationshipViews = (Map<String, Relationship>)relationshipsByIdField.get(model);
        List<String> keysOfLayoutsRelations = relationshipViews.entrySet().stream()
            .filter(entry -> {
                Relationship value = entry.getValue();
                Map<String, String> properties = value.getProperties();
                String layout = properties.get(C4_LAYOUT_MODE);
                return Lay.name().equals(layout);
            })
            .map(Entry::getKey)
            .collect(Collectors.toList());
        keysOfLayoutsRelations.forEach(relationshipViews::remove);
    }

    private void addLegend(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        List<String> lines = Files.readAllLines(path);
        lines.set(lines.size() - 1, "LAYOUT_WITH_LEGEND()");
        lines.add("@enduml");
        Files.write(path, lines);
    }

    private void replacePackageWithSystemBondary(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        List<String> lines = Files.readAllLines(path);
        List<String> linesWithoutPackages = lines.stream()
            .map(this::mapLinesToC4PlantUmlFormat)
            .collect(Collectors.toList());
        Files.write(path, linesWithoutPackages);
    }

    private String mapLinesToC4PlantUmlFormat(String line) {
        Matcher matcher = PACKAGE_PATTERN.matcher(line);
        if (matcher.matches()) {
            return String.format("System_Boundary(%s, %s) {", UUID.randomUUID().toString(), matcher.group(1));
        }
        return line;
    }
}
