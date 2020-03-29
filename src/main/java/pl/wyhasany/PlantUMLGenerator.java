package pl.wyhasany;

import com.structurizr.io.plantuml.PlantUMLWriter;
import com.structurizr.view.View;

import java.io.FileWriter;
import java.io.IOException;

class PlantUMLGenerator {

    private final PlantUMLWriter plantUMLWriter;

    private Organization organization;

    PlantUMLGenerator(Organization organization) {
        this.organization = organization;
        plantUMLWriter = new PlantUMLWriter();
        new PlantUMLStyling(plantUMLWriter, organization);
    }

    void saveDiagramToFile(Diagram diagram, String fileName) throws IOException {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            View view = diagram.getView(organization);
            plantUMLWriter.write(view, fileWriter);
            fileWriter.flush();
        }
    }
}
