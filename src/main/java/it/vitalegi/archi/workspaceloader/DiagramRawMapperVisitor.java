package it.vitalegi.archi.workspaceloader;

import it.vitalegi.archi.model.diagram.DeploymentDiagram;
import it.vitalegi.archi.model.diagram.Diagram;
import it.vitalegi.archi.model.diagram.LandscapeDiagram;
import it.vitalegi.archi.model.diagram.SystemContextDiagram;
import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.workspaceloader.model.DeploymentDiagramRaw;
import it.vitalegi.archi.workspaceloader.model.DiagramRaw;
import it.vitalegi.archi.workspaceloader.model.LandscapeDiagramRaw;
import it.vitalegi.archi.workspaceloader.model.SystemContextDiagramRaw;
import it.vitalegi.archi.visitor.DiagramRawVisitor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DiagramRawMapperVisitor implements DiagramRawVisitor<Diagram> {
    Model model;

    @Override
    public Diagram visitLandscapeDiagramRaw(LandscapeDiagramRaw diagram) {
        var out = new LandscapeDiagram(model);
        mapDiagram(diagram, out);
        return out;
    }

    @Override
    public Diagram visitSystemContextDiagramRaw(SystemContextDiagramRaw diagram) {
        var out = new SystemContextDiagram(model);
        mapDiagram(diagram, out);
        out.setTarget(diagram.getTarget());
        return out;
    }

    @Override
    public Diagram visitDeploymentDiagramRaw(DeploymentDiagramRaw diagram) {
        var out = new DeploymentDiagram(model);
        mapDiagram(diagram, out);
        out.setEnvironment(diagram.getEnvironment());
        out.setScope(diagram.getScope());
        return out;
    }

    protected void mapDiagram(DiagramRaw in, Diagram out) {
        out.setName(in.getName());
        out.setTitle(in.getTitle());
        out.setStyle(in.getStyle());
    }

}
