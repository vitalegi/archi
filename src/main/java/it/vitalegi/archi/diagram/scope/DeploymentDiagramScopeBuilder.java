package it.vitalegi.archi.diagram.scope;

import it.vitalegi.archi.model.diagram.DeploymentDiagram;
import it.vitalegi.archi.model.element.Container;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.element.SoftwareSystem;
import it.vitalegi.archi.util.WorkspaceUtil;

import java.util.stream.Collectors;

public abstract class DeploymentDiagramScopeBuilder extends DiagramScopeBuilder<DeploymentDiagram> {
    public DeploymentDiagramScopeBuilder(DeploymentDiagram diagram) {
        super(diagram);
    }

    @Override
    protected boolean isAllowed(Element element) {
        if (isChildOfDeploymentEnvironment(element)) {
            return true;
        }
        if (WorkspaceUtil.isSoftwareSystem(element)) {
            return isConnectedToDeploymentEnvironment((SoftwareSystem) element);
        }
        if (WorkspaceUtil.isContainer(element)) {
            return isConnectedToDeploymentEnvironment((Container) element);
        }
        return false;
    }

    protected boolean isChildOfDeploymentEnvironment(Element element) {
        var env = diagram.getEnvironment();

        var deploymentEnvironment = WorkspaceUtil.findDeploymentEnvironment(WorkspaceUtil.getPathFromRoot(element), env);
        return deploymentEnvironment != null;
    }

    protected boolean isConnectedToDeploymentEnvironment(SoftwareSystem softwareSystem) {
        var deploymentEnvironment = diagram.getModel().findDeploymentEnvironmentById(diagram.getEnvironment());
        var children = deploymentEnvironment.getAllChildren().collect(Collectors.toList());
        var instances = WorkspaceUtil.getSoftwareSystemInstances(children);
        return instances.stream().anyMatch(i -> i.getSoftwareSystem().equals(softwareSystem));
    }

    protected boolean isConnectedToDeploymentEnvironment(Container container) {
        var deploymentEnvironment = diagram.getModel().findDeploymentEnvironmentById(diagram.getEnvironment());
        var children = deploymentEnvironment.getAllChildren().collect(Collectors.toList());
        var instances = WorkspaceUtil.getContainerInstances(children);
        return instances.stream().anyMatch(i -> i.getContainer().equals(container));
    }
}
