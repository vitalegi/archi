package it.vitalegi.archi.exporter.plantuml;

import it.vitalegi.archi.model.diagram.DeploymentDiagram;
import it.vitalegi.archi.model.diagram.LandscapeDiagram;
import it.vitalegi.archi.model.diagram.SystemContextDiagram;
import it.vitalegi.archi.visitor.DiagramVisitor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PlantumlDiagramValidatorVisitor implements DiagramVisitor<Void> {
    @Override
    public Void visitLandscapeDiagram(LandscapeDiagram diagram) {
        var processor = new LandscapeDiagramPlantumlExporter();
        processor.validate(diagram);
        return null;
    }

    @Override
    public Void visitSystemContextDiagram(SystemContextDiagram diagram) {
        var processor = new SystemContextDiagramPlantumlExporter();
        processor.validate(diagram);
        return null;
    }

    @Override
    public Void visitDeploymentDiagram(DeploymentDiagram diagram) {
        var processor = new DeploymentDiagramPlantumlExporter();
        processor.validate(diagram);
        return null;
    }
}
