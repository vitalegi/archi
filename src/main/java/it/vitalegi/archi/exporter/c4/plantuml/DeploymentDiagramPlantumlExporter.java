package it.vitalegi.archi.exporter.c4.plantuml;

import it.vitalegi.archi.exception.ElementNotFoundException;
import it.vitalegi.archi.exporter.c4.plantuml.builder.DeploymentDiagramModelBuilder;
import it.vitalegi.archi.exporter.c4.plantuml.writer.C4PlantumlWriter;
import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.DeploymentDiagram;
import it.vitalegi.archi.model.diagramelement.C4DiagramElement;
import it.vitalegi.archi.model.diagramelement.C4DiagramModel;
import it.vitalegi.archi.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeploymentDiagramPlantumlExporter extends AbstractDiagramPlantumlExporter<DeploymentDiagram> {

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
        if (diagram.isScopeAll()) {
            return;
        }
        if (diagram.isScopeSoftwareSystem()) {
            return;
        }
        throw new ElementNotFoundException(diagram.getScope(), "Scope " + diagram.getScope() + " on diagram " + diagram.getName() + " is invalid. Check if all objects exist.");
    }

    @Override
    protected C4DiagramModel buildModel(Workspace workspace, DeploymentDiagram diagram) {
        return new DeploymentDiagramModelBuilder(workspace, diagram).build();
    }

    @Override
    protected void writeIncludes(C4PlantumlWriter writer) {
        writer.include("<C4/C4>");
        writer.include("<C4/C4_Context>");
        writer.include("<C4/C4_Container>");
        writer.include("<C4/C4_Deployment>");
    }

    @Override
    protected void writeAsNode(DeploymentDiagram diagram, C4DiagramModel model, C4DiagramElement element, C4PlantumlWriter writer) {
        writer.deploymentNodeStart(element);
        writeChildren(diagram, model, element, writer);
        writer.deploymentNodeEnd();
    }

    @Override
    protected void writeAsLeaf(DeploymentDiagram diagram, C4DiagramModel model, C4DiagramElement element, C4PlantumlWriter writer) {
        writer.container(element);
    }
}
