package it.vitalegi.archi.exporter.c4.plantuml;

import it.vitalegi.archi.exception.ElementNotFoundException;
import it.vitalegi.archi.exporter.c4.plantuml.builder.FlowDiagramModelBuilder;
import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.FlowDiagram;
import it.vitalegi.archi.model.diagramelement.C4DiagramModel;
import it.vitalegi.archi.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FlowDiagramPlantumlExporter extends AbstractDiagramPlantumlExporter<FlowDiagram> {
    @Override
    public void validate(FlowDiagram diagram) {
        if (StringUtil.isNullOrEmpty(diagram.getFlow())) {
            throw new IllegalArgumentException("Diagram " + diagram.getName() + ", missing flow.");
        }
        var flowId = diagram.getFlow();
        var flow = diagram.getModel().findFlowById(flowId);
        if (flow == null) {
            throw new ElementNotFoundException(flowId, "invalid flow on Diagram " + diagram.getName());
        }
    }

    @Override
    protected C4DiagramModel buildModel(Workspace workspace, FlowDiagram diagram) {
        return new FlowDiagramModelBuilder(workspace, diagram).build();
    }
}
