package pl.wyhasany;

import com.structurizr.io.plantuml.C4PlantUMLWriter;
import com.structurizr.io.plantuml.C4PlantUMLWriter.RelationshipModes;
import com.structurizr.io.plantuml.C4PlantUMLWriter.Type;
import com.structurizr.model.Container;
import com.structurizr.model.Person;
import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.ContainerView;
import com.structurizr.view.View;
import lombok.Value;
import lombok.experimental.Accessors;

import static com.structurizr.io.plantuml.C4PlantUMLWriter.C4_ELEMENT_TYPE;
import static com.structurizr.io.plantuml.C4PlantUMLWriter.C4_LAYOUT_DIRECTION;
import static com.structurizr.io.plantuml.C4PlantUMLWriter.C4_LAYOUT_MODE;
import static com.structurizr.io.plantuml.C4PlantUMLWriter.Directions.Down;
import static com.structurizr.io.plantuml.C4PlantUMLWriter.Directions.Left;
import static com.structurizr.io.plantuml.C4PlantUMLWriter.Directions.Right;
import static com.structurizr.io.plantuml.C4PlantUMLWriter.Directions.Up;
import static com.structurizr.io.plantuml.C4PlantUMLWriter.RelationshipModes.Lay;
import static com.structurizr.io.plantuml.C4PlantUMLWriter.Type.Db;
import static pl.wyhasany.StructurizrTags.DATABASE_TAG;

@Value
@Accessors(fluent = true)
class C2_ContainerDiagram implements Diagram {

    Container mobileApp;

    Container apiApplication;

    Container database;

    C1_ContextDiagram c1ContextDiagram;

    C2_ContainerDiagram(C1_ContextDiagram c1ContextDiagram) {
        this.c1ContextDiagram = c1ContextDiagram;

        SoftwareSystem internetBankingSystem = c1ContextDiagram.internetBankingSystem();
        Person customer = c1ContextDiagram.customer();
        SoftwareSystem mainframeBankingSystem = c1ContextDiagram.mainframeBankingSystem();
        SoftwareSystem emailSystem = c1ContextDiagram.emailSystem();

        mobileApp = mobileApp(internetBankingSystem);
        apiApplication = apiApplication(internetBankingSystem);
        database = database(internetBankingSystem);

        interactions(customer, mainframeBankingSystem, emailSystem);
    }

    private Container database(SoftwareSystem internetBankingSystem) {
        Container database = internetBankingSystem.addContainer(
            "Baza danych",
            "Informacje o klientach, hashowane hasła, logi etc",
            "Kontener: Relacyjna baza danych"
        );
        database.addProperty(C4_ELEMENT_TYPE, Db.name());
        database.addTags(DATABASE_TAG);
        return database;
    }

    private Container apiApplication(SoftwareSystem internetBankingSystem) {
        return internetBankingSystem.addContainer(
            "API",
            "Dostarcza funkcjonalność bankowości online poprzez JSON/HTTPS API.",
            "Kontener: Java i Spring MVC"
        );
    }

    private Container mobileApp(SoftwareSystem internetBankingSystem) {
        return internetBankingSystem.addContainer(
            "Aplikacja mobilna",
            "Dostarcza ograniczoną funkcjonalność bankowości online dla klientów",
            "Kontener: Xamarin"
        );
    }

    private void interactions(Person customer, SoftwareSystem mainframeBankingSystem, SoftwareSystem emailSystem) {
        customer.uses(mobileApp, "Używa", "")
            .addProperty(C4_LAYOUT_DIRECTION, Right.name());
        apiApplication.uses(database, "Czyta/Zapisuje", "JDBC")
            .addProperty(C4_LAYOUT_DIRECTION, Down.name());
        apiApplication.uses(mainframeBankingSystem, "Używa", "XML/HTTPS")
            .addProperty(C4_LAYOUT_DIRECTION, Down.name());
        mobileApp.uses(apiApplication, "Używa", "JSON/HTTPS")
            .addProperty(C4_LAYOUT_DIRECTION, Down.name());
        apiApplication.uses(emailSystem, "Wysyła maile", "SMTP")
            .addProperty(C4_LAYOUT_DIRECTION, Left.name());
        //Layout
        Relationship layout = emailSystem.uses(mainframeBankingSystem, "");
        layout.addProperty(C4_LAYOUT_MODE, Lay.name());
        layout.addProperty(C4_LAYOUT_DIRECTION, Down.name());
    }

    @Override
    public View getView(Organization organization) {
        ContainerView containerView = organization.views().createContainerView(
            c1ContextDiagram.internetBankingSystem(),
            "Containers",
            "Diagram kontenerów systemu Platformy Bankowości Online"
        );
        containerView.add(c1ContextDiagram.customer());
        containerView.addAllContainers();
        containerView.add(c1ContextDiagram.mainframeBankingSystem());
        containerView.add(c1ContextDiagram.emailSystem());

        containerView.addAnimation(
            c1ContextDiagram.customer(),
            c1ContextDiagram.mainframeBankingSystem(),
            c1ContextDiagram.emailSystem()
        );
        containerView.addAnimation(mobileApp);
        containerView.addAnimation(apiApplication);
        containerView.addAnimation(database);
        return containerView;
    }
}
