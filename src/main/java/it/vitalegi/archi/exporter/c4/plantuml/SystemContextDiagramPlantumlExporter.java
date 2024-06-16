package it.vitalegi.archi.exporter.c4.plantuml;

import it.vitalegi.archi.diagram.scope.DiagramScopeBuilder;
import it.vitalegi.archi.exception.ElementNotFoundException;
import it.vitalegi.archi.exporter.c4.plantuml.builder.SystemContextDiagramModelBuilder;
import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.SystemContextDiagram;
import it.vitalegi.archi.model.diagramelement.C4DiagramModel;
import it.vitalegi.archi.util.StringUtil;
import it.vitalegi.archi.util.WorkspaceUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SystemContextDiagramPlantumlExporter extends AbstractDiagramPlantumlExporter<SystemContextDiagram> {
    @Override
    public void validate(SystemContextDiagram diagram) {
        if (StringUtil.isNullOrEmpty(diagram.getTarget())) {
            throw new IllegalArgumentException("Diagram " + diagram.getName() + ", missing target.");
        }
        var target = diagram.getModel().getElementById(diagram.getTarget());
        if (target == null) {
            throw new ElementNotFoundException(diagram.getTarget(), "invalid target on Diagram " + diagram.getName());
        }
        if (!WorkspaceUtil.isSoftwareSystem(target)) {
            throw new IllegalArgumentException("Diagram " + diagram.getName() + ", invalid target. Expected: SoftwareSystem, Actual: " + target.toShortString());
        }
    }

    @Override
    protected C4DiagramModel buildModel(Workspace workspace, SystemContextDiagram diagram) {
        return new SystemContextDiagramModelBuilder(workspace, diagram).build();
    }

    @Override
    protected DiagramScopeBuilder<SystemContextDiagram> diagramScope(Workspace workspace, SystemContextDiagram diagram) {
        throw new RuntimeException("Method shouldn't be invoked");
    }
}
