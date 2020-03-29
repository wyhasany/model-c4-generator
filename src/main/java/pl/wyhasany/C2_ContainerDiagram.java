package pl.wyhasany;

import com.structurizr.model.Container;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.ContainerView;
import com.structurizr.view.View;
import lombok.Value;
import lombok.experimental.Accessors;

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
        customer.uses(mobileApp, "Używa", "");
        apiApplication.uses(database, "Czyta/Zapisuje", "JDBC");
        apiApplication.uses(mainframeBankingSystem, "Używa", "XML/HTTPS");
        mobileApp.uses(apiApplication, "Używa", "JSON/HTTPS");
        apiApplication.uses(emailSystem, "Wysyła maile", "SMTP");
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
