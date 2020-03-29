package pl.wyhasany;

import com.structurizr.Workspace;
import com.structurizr.model.Enterprise;
import com.structurizr.model.Model;
import com.structurizr.view.ViewSet;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
class Organization {

    Workspace workspace;

    private Model model;

    private ViewSet views;

    Organization(String enterpriseName, String workspaceDescription, String workspaceName) {
        workspace = new Workspace(workspaceName, workspaceDescription);
        model = workspace.getModel();
        views = workspace.getViews();
        model.setEnterprise(new Enterprise(enterpriseName));
    }
}
