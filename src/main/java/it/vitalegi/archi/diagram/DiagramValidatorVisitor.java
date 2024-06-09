package it.vitalegi.archi.diagram;

import it.vitalegi.archi.diagram.model.DeploymentDiagram;
import it.vitalegi.archi.diagram.model.LandscapeDiagram;
import it.vitalegi.archi.diagram.model.SystemContextDiagram;
import it.vitalegi.archi.diagram.processor.DeploymentDiagramProcessor;
import it.vitalegi.archi.diagram.processor.LandscapeDiagramProcessor;
import it.vitalegi.archi.diagram.processor.SystemContextDiagramProcessor;
import it.vitalegi.archi.visitor.DiagramVisitor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DiagramValidatorVisitor implements DiagramVisitor<Void> {
    @Override
    public Void visitLandscapeDiagram(LandscapeDiagram diagram) {
        var processor = new LandscapeDiagramProcessor();
        processor.validate(diagram);
        return null;
    }

    @Override
    public Void visitSystemContextDiagram(SystemContextDiagram diagram) {
        var processor = new SystemContextDiagramProcessor();
        processor.validate(diagram);
        return null;
    }

    @Override
    public Void visitDeploymentDiagram(DeploymentDiagram diagram) {
        var processor = new DeploymentDiagramProcessor();
        processor.validate(diagram);
        return null;
    }
}
