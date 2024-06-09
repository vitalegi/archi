package it.vitalegi.archi.diagram;

import it.vitalegi.archi.diagram.model.DeploymentDiagram;
import it.vitalegi.archi.diagram.model.LandscapeDiagram;
import it.vitalegi.archi.diagram.model.SystemContextDiagram;
import it.vitalegi.archi.diagram.processor.DeploymentDiagramProcessor;
import it.vitalegi.archi.diagram.processor.LandscapeDiagramProcessor;
import it.vitalegi.archi.diagram.processor.SystemContextDiagramProcessor;
import it.vitalegi.archi.visitor.DiagramVisitor;
import it.vitalegi.archi.workspace.Workspace;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class C4PlantUmlDiagramVisitor implements DiagramVisitor<String> {
    Workspace workspace;

    @Override
    public String visitLandscapeDiagram(LandscapeDiagram diagram) {
        var processor = new LandscapeDiagramProcessor();
        return processor.createPuml(workspace, diagram);
    }

    @Override
    public String visitSystemContextDiagram(SystemContextDiagram diagram) {
        var processor = new SystemContextDiagramProcessor();
        return processor.createPuml(workspace, diagram);
    }

    @Override
    public String visitDeploymentDiagram(DeploymentDiagram diagram) {
        var processor = new DeploymentDiagramProcessor();
        return processor.createPuml(workspace, diagram);
    }
}
