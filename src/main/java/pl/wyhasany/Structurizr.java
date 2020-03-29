package pl.wyhasany;

/**
 * This is a simple example of how to get started with pl.wyhasany.Structurizr for Java.
 */
public class Structurizr {

    public static void main(String[] args) throws Exception {
        Organization organization = new Organization(
            "Bankowość korporacyjna",
            "Nasz model systemu",
            "Bankowość");
        C1_ContextDiagram c1ContextDiagram = new C1_ContextDiagram(organization);
        C2_ContainerDiagram c2ContainerDiagram = new C2_ContainerDiagram(c1ContextDiagram);
        C3_ComponentDiagram c3ComponentDiagram = new C3_ComponentDiagram(c2ContainerDiagram);

        PlantUMLGenerator plantUMLGenerator = new PlantUMLGenerator(organization);
        plantUMLGenerator.saveDiagramToFile(c1ContextDiagram, "c1.puml");
        plantUMLGenerator.saveDiagramToFile(c2ContainerDiagram, "c2.puml");
        plantUMLGenerator.saveDiagramToFile(c3ComponentDiagram, "c3.puml");
    }
}
