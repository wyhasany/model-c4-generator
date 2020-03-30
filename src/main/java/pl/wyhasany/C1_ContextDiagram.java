package pl.wyhasany;

import com.structurizr.io.plantuml.C4PlantUMLWriter;
import com.structurizr.io.plantuml.C4PlantUMLWriter.Directions;
import com.structurizr.model.Location;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.View;
import lombok.Value;
import lombok.experimental.Accessors;

import static com.structurizr.io.plantuml.C4PlantUMLWriter.C4_LAYOUT_DIRECTION;
import static com.structurizr.io.plantuml.C4PlantUMLWriter.Directions.Down;
import static com.structurizr.io.plantuml.C4PlantUMLWriter.Directions.Left;
import static com.structurizr.io.plantuml.C4PlantUMLWriter.Directions.Right;
import static com.structurizr.io.plantuml.C4PlantUMLWriter.Directions.Up;

@Value
@Accessors(fluent = true)
class C1_ContextDiagram implements Diagram {

    Person customer;

    SoftwareSystem internetBankingSystem;

    SoftwareSystem mainframeBankingSystem;

    SoftwareSystem emailSystem;

    C1_ContextDiagram(Organization organization) {
        Model model = organization.model();
        customer = customer(model);
        internetBankingSystem = internetBankingSystem(model);
        mainframeBankingSystem = mainframeBankingSystem(model);
        emailSystem = emailSystem(model);
        defineSystemDependencies();
    }

    private Person customer(Model model) {
        return model.addPerson(
            Location.External,
            "Klient indywidualny",
            "Klient banku, "
            + "posiada konta bankowe z numerami");
    }

    private SoftwareSystem internetBankingSystem(Model model) {
        return model.addSoftwareSystem(
            Location.Internal,
            "Platforma bankowości online",
            "Pozwala na przeglądanie stanu konta i dokonywanie transakcji"
        );
    }

    private SoftwareSystem mainframeBankingSystem(Model model) {
        return model.addSoftwareSystem(
            Location.Internal,
            "System bankowy Mainframe",
            "Core'owy system bankowy. Zawiera informacje o klientach, transakcjach, kontach, etc."
        );
    }

    private SoftwareSystem emailSystem(Model model) {
        return model.addSoftwareSystem(
            Location.Internal,
            "E-mail System",
            "System e-mailowy Microsoft Exchange"
        );
    }

    private void defineSystemDependencies() {
        customer.uses(
            internetBankingSystem,
            "Używa"
        ).addProperty(C4_LAYOUT_DIRECTION, Down.name());

        internetBankingSystem.uses(
            mainframeBankingSystem,
            "Pobiera informacje/Wysyła informacje",
            "XML/HTTPS"
        ).addProperty(C4_LAYOUT_DIRECTION, Left.name());

        internetBankingSystem.uses(
            emailSystem,
            "Pobiera informacje/Wysyła informacje"
        ).addProperty(C4_LAYOUT_DIRECTION, Right.name());

        emailSystem.delivers(
            customer,
            "Wysyła e-maile do"
        ).addProperty(C4_LAYOUT_DIRECTION, Up.name());
    }

    @Override
    public View getView(Organization organization) {
        SystemContextView systemContextView = organization.views().createSystemContextView(
            internetBankingSystem,
            "SystemContext",
            "Diagram kontekstowy systemu bankowego"
        );
        systemContextView.addNearestNeighbours(internetBankingSystem);
        systemContextView.addAnimation(
            internetBankingSystem,
            customer,
            mainframeBankingSystem,
            emailSystem
        );
        return systemContextView;
    }
}
