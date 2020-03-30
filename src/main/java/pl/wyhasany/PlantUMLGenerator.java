package pl.wyhasany;

import com.structurizr.io.plantuml.C4PlantUMLWriter;
import com.structurizr.io.plantuml.C4PlantUMLWriter.RelationshipModes;
import com.structurizr.io.plantuml.PlantUMLWriter;
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
import java.util.stream.Collectors;

import static com.structurizr.io.plantuml.C4PlantUMLWriter.C4_LAYOUT_MODE;
import static com.structurizr.io.plantuml.C4PlantUMLWriter.RelationshipModes.Lay;

class PlantUMLGenerator {

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
    }

    private void removeLayouts() {
        try {
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
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void addLegend(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        List<String> lines = Files.readAllLines(path);
        lines.set(lines.size() - 1, "LAYOUT_WITH_LEGEND()");
        lines.add("@enduml");
        Files.write(path, lines);
    }
}
