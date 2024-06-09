package it.vitalegi.archi.exporter.plantuml;

import it.vitalegi.archi.model.diagram.DeploymentDiagram;
import it.vitalegi.archi.model.diagram.LandscapeDiagram;
import it.vitalegi.archi.model.diagram.SystemContextDiagram;
import it.vitalegi.archi.visitor.DiagramVisitor;
import it.vitalegi.archi.model.Workspace;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DiagramToPlantumlConverter implements DiagramVisitor<String> {
    Workspace workspace;

    @Override
    public String visitLandscapeDiagram(LandscapeDiagram diagram) {
        var processor = new LandscapeDiagramPlantumlExporter();
        return processor.export(workspace, diagram);
    }

    @Override
    public String visitSystemContextDiagram(SystemContextDiagram diagram) {
        var processor = new SystemContextDiagramPlantumlExporter();
        return processor.export(workspace, diagram);
    }

    @Override
    public String visitDeploymentDiagram(DeploymentDiagram diagram) {
        var processor = new DeploymentDiagramPlantumlExporter();
        return processor.export(workspace, diagram);
    }
}
