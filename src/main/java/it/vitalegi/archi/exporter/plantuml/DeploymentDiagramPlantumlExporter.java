package it.vitalegi.archi.exporter.plantuml;

import it.vitalegi.archi.diagram.scope.DeploymentDiagramAllScopeBuilder;
import it.vitalegi.archi.diagram.scope.DeploymentDiagramSoftwareSystemScopeBuilder;
import it.vitalegi.archi.diagram.scope.DiagramScopeBuilder;
import it.vitalegi.archi.diagram.scope.Scope;
import it.vitalegi.archi.exception.ElementNotFoundException;
import it.vitalegi.archi.exporter.plantuml.writer.C4PlantumlWriter;
import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.DeploymentDiagram;
import it.vitalegi.archi.model.element.ContainerInstance;
import it.vitalegi.archi.model.element.DeploymentNode;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.element.SoftwareSystemInstance;
import it.vitalegi.archi.util.StringUtil;
import it.vitalegi.archi.util.WorkspaceUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class DeploymentDiagramPlantumlExporter extends AbstractModelDiagramPlantumlExporter<DeploymentDiagram> {

    @Override
    public void validate(DeploymentDiagram diagram) {
        var env = diagram.getEnvironment();
        if (StringUtil.isNullOrEmpty(env)) {
            throw new NullPointerException("Environment is mandatory on DeploymentDiagram. Error on " + diagram.getName());
        }
        var deploymentEnvironment = diagram.getModel().findDeploymentEnvironmentById(env);
        if (deploymentEnvironment == null) {
            throw new ElementNotFoundException(env, "required on diagram " + diagram.getName());
        }
        doValidateScope(diagram);
    }

    protected void doValidateScope(DeploymentDiagram diagram) {
        if (isScopeAll(diagram)) {
            return;
        }
        if (isScopeSoftwareSystem(diagram)) {
            return;
        }
        throw new ElementNotFoundException(diagram.getScope(), "Scope " + diagram.getScope() + " on diagram " + diagram.getName() + " is invalid. Check if all objects exist.");
    }

    @Override
    protected DiagramScopeBuilder<DeploymentDiagram> diagramScope(Workspace workspace, DeploymentDiagram diagram) {
        if (isScopeAll(diagram)) {
            return new DeploymentDiagramAllScopeBuilder(diagram);
        } else {
            return new DeploymentDiagramSoftwareSystemScopeBuilder(diagram);
        }
    }

    @Override
    protected void writeIncludes(C4PlantumlWriter writer) {
        writer.include("<C4/C4>");
        writer.include("<C4/C4_Context>");
        writer.include("<C4/C4_Container>");
        writer.include("<C4/C4_Deployment>");
    }

    protected void addElementTreeToPuml(List<Element> elementsInScope, Element element, C4PlantumlWriter writer) {
        if (!elementsInScope.contains(element)) {
            return;
        }
        if (WorkspaceUtil.isDeploymentNode(element)) {
            var deploymentNode = (DeploymentNode) element;
            writer.deploymentNodeStart(element);
            deploymentNode.getElements().forEach(child -> addElementTreeToPuml(elementsInScope, child, writer));
            writer.deploymentNodeEnd();
            return;
        }
        if (WorkspaceUtil.isInfrastructureNode(element)) {
            writer.container(element);
            return;
        }
        if (WorkspaceUtil.isContainerInstance(element)) {
            writer.deploymentNodeStart(element);
            var containerInstance = (ContainerInstance) element;
            var container = containerInstance.getContainer();
            writer.container(container);
            writer.deploymentNodeEnd();
            return;
        }
        if (WorkspaceUtil.isSoftwareSystemInstance(element)) {
            writer.deploymentNodeStart(element);
            var softwareSystemInstance = (SoftwareSystemInstance) element;
            var softwareSystem = softwareSystemInstance.getSoftwareSystem();
            writer.container(softwareSystem);
            writer.deploymentNodeEnd();
            return;
        }
        throw new RuntimeException("Unable to process " + element.toShortString());
    }

    protected boolean isScopeAll(DeploymentDiagram diagram) {
        return Scope.isScopeAll(diagram.getScope());
    }

    protected boolean isScopeSoftwareSystem(DeploymentDiagram diagram) {
        var scope = diagram.getScope();
        if (StringUtil.isNullOrEmpty(scope)) {
            return false;
        }
        var softwareSystem = WorkspaceUtil.findSoftwareSystem(diagram.getModel().getAllElements(), scope);
        return softwareSystem != null;
    }


}
