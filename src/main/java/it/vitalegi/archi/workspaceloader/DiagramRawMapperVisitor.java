package it.vitalegi.archi.workspaceloader;

import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.model.diagram.DeploymentDiagram;
import it.vitalegi.archi.model.diagram.Diagram;
import it.vitalegi.archi.model.diagram.FlowDiagram;
import it.vitalegi.archi.model.diagram.LandscapeDiagram;
import it.vitalegi.archi.model.diagram.SystemContextDiagram;
import it.vitalegi.archi.model.diagram.options.DiagramOptions;
import it.vitalegi.archi.visitor.DiagramRawVisitor;
import it.vitalegi.archi.workspaceloader.model.DeploymentDiagramRaw;
import it.vitalegi.archi.workspaceloader.model.DiagramRaw;
import it.vitalegi.archi.workspaceloader.model.FlowDiagramRaw;
import it.vitalegi.archi.workspaceloader.model.LandscapeDiagramRaw;
import it.vitalegi.archi.workspaceloader.model.SystemContextDiagramRaw;
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

    @Override
    public Diagram visitFlowDiagramRaw(FlowDiagramRaw diagram) {
        var out = new FlowDiagram(model);
        mapDiagram(diagram, out);
        out.setFlow(diagram.getFlow());
        return out;
    }

    protected void mapDiagram(DiagramRaw in, Diagram out) {
        out.setName(in.getName());
        out.setTitle(in.getTitle());
        out.setStyle(in.getStyle());
        if (in.getOptions() == null) {
            out.setOptions(DiagramOptions.defaultOptions());
        } else {
            out.setOptions(in.getOptions());
        }
    }
}
