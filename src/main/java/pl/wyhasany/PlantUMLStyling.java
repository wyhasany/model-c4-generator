package pl.wyhasany;

import com.structurizr.io.plantuml.PlantUMLWriter;
import com.structurizr.model.Tags;
import com.structurizr.view.Shape;
import com.structurizr.view.Styles;
import com.structurizr.view.ViewSet;

import static pl.wyhasany.StructurizrTags.DATABASE_TAG;

class PlantUMLStyling {

    PlantUMLStyling(PlantUMLWriter plantUMLWriter, Organization organization) {
        addPlantUmlStyling(plantUMLWriter);
        addStructurizrStylilng(organization.views());
    }

    private void addStructurizrStylilng(ViewSet views) {
        Styles styles = views.getConfiguration().getStyles();
        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#1168bd").color("#ffffff");
        styles.addElementStyle(Tags.PERSON).background("#08427b").color("#ffffff").shape(Shape.Person);
        styles.addElementStyle(DATABASE_TAG).background("#1168bd").color("#ffffff").shape(Shape.Cylinder);
        styles.addElementStyle(Tags.CONTAINER).background("#bbbbbb").color("#ffffff").shape(Shape.Box);
    }

    private static void addPlantUmlStyling(PlantUMLWriter plantUMLWriter) {
        plantUMLWriter.addSkinParam("rectangleFontColor", "#ffffff");
        plantUMLWriter.addSkinParam("rectangleStereotypeFontColor", "#ffffff");
    }
}
