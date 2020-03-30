package pl.wyhasany;

import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Relationship;
import com.structurizr.view.ComponentView;
import com.structurizr.view.PaperSize;
import com.structurizr.view.View;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

import static com.structurizr.io.plantuml.C4PlantUMLWriter.C4_LAYOUT_DIRECTION;
import static com.structurizr.io.plantuml.C4PlantUMLWriter.C4_LAYOUT_MODE;
import static com.structurizr.io.plantuml.C4PlantUMLWriter.Directions.Down;
import static com.structurizr.io.plantuml.C4PlantUMLWriter.Directions.Left;
import static com.structurizr.io.plantuml.C4PlantUMLWriter.RelationshipModes.Lay;

@Value
class C3_ComponentDiagram implements Diagram {

    Component signinController;
    Component accountsSummaryController;
    Component resetPasswordController;
    Component securityComponent;
    Component emailComponent;
    Component mainframeFacadeComponent;

    @Getter(AccessLevel.NONE)
    C2_ContainerDiagram c2ContainerDiagram;

    C3_ComponentDiagram(C2_ContainerDiagram c2ContainerDiagram) {
        this.c2ContainerDiagram = c2ContainerDiagram;
        Container apiApplication = c2ContainerDiagram.apiApplication();

        signinController = signinController(apiApplication);
        accountsSummaryController = accountsSummaryController(apiApplication);
        resetPasswordController = resetPasswordController(apiApplication);
        securityComponent = securityComponent(apiApplication);
        emailComponent = emailComponent(apiApplication);
        mainframeFacadeComponent = mainframeFacadeComponent(apiApplication);

        addInteractions();
    }

    private Component signinController(Container apiApplication) {
        return apiApplication.addComponent(
            "Logowanie",
            "Pozwala na logowanie do platformy bankowej",
            "Komponent: Spring MVC Rest Controller"
        );
    }

    private Component accountsSummaryController(Container apiApplication) {
        return apiApplication.addComponent(
            "Konta bankowe",
            "Pozwala na wgląd w widok posiadanych kont",
            "Komponent: Spring MVC Rest Controller"
        );
    }

    private Component resetPasswordController(Container apiApplication) {
        return apiApplication.addComponent(
            "Resetowanie hasła",
            "Pozwala na wygenerowanie URL do resetu hasła",
            "Komponent: Spring MVC Rest Controller"
        );
    }

    private Component securityComponent(Container apiApplication) {
        return apiApplication.addComponent(
            "Komponent bezpieczeństwa",
            "Dostarcza funkcjonalność potrzebną do zmian hasła",
            "Komponent: Spring Bean"
        );
    }

    private Component emailComponent(Container apiApplication) {
        return apiApplication.addComponent(
            "Komponent E-mail",
            "Pozwala na wysyłanie e-maili",
            "Komponent: Spring Bean"
        );
    }

    private Component mainframeFacadeComponent(Container apiApplication) {
        return apiApplication.addComponent(
            "Fasada do Mainframe Banking System",
            "Fasada do mainframe banking system",
            "Komponent: Spring Bean"
        );
    }

    private void addInteractions() {
        c2ContainerDiagram.mobileApp().uses(signinController, "Używa", "JSON/HTTPS");
        c2ContainerDiagram.mobileApp().uses(resetPasswordController, "Używa", "JSON/HTTPS");
        c2ContainerDiagram.mobileApp().uses(accountsSummaryController, "Używa", "JSON/HTTPS");
        accountsSummaryController.uses(mainframeFacadeComponent, "Używa", "SYNC");
        mainframeFacadeComponent.uses(c2ContainerDiagram.c1ContextDiagram().mainframeBankingSystem(), "Używa", "XML/HTTPS");
        resetPasswordController.uses(securityComponent, "Używa", "SYNC");
        resetPasswordController.uses(emailComponent, "Używa", "SYNC");
        emailComponent.uses(c2ContainerDiagram.c1ContextDiagram().emailSystem(), "Używa", "XML/HTTPS");
        signinController.uses(securityComponent, "Używa", "SYNC");
        securityComponent.uses(c2ContainerDiagram.database(), "Czyta i zapisuje do", "JDBC");
        //Layout
        Relationship layout = resetPasswordController.uses(accountsSummaryController, "");
        layout.addProperty(C4_LAYOUT_MODE, Lay.name());
        layout.addProperty(C4_LAYOUT_DIRECTION, Left.name());
    }

    @Override
    public View getView(Organization organization) {
        organization.model().addImplicitRelationships();

        C1_ContextDiagram c1ContextDiagram = c2ContainerDiagram.c1ContextDiagram();

        ComponentView componentView = organization.views().createComponentView(
            c2ContainerDiagram.apiApplication(),
            "Components",
            "Diagram komponentów aplikacji API"
        );
        componentView.add(c2ContainerDiagram.mobileApp());
        componentView.add(c2ContainerDiagram.database());
        componentView.addAllComponents();
        componentView.add(c1ContextDiagram.mainframeBankingSystem());
        componentView.add(c1ContextDiagram.emailSystem());
        componentView.setPaperSize(PaperSize.A5_Landscape);

        componentView.addAnimation(c2ContainerDiagram.mobileApp());
        componentView.addAnimation(
            signinController,
            securityComponent,
            c2ContainerDiagram.database());
        componentView.addAnimation(
            accountsSummaryController,
            c1ContextDiagram.mainframeBankingSystem()
        );
        componentView.addAnimation(
            resetPasswordController,
            c1ContextDiagram.emailSystem(),
            c2ContainerDiagram.database()
        );

        return componentView;
    }
}
