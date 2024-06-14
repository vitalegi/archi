package it.vitalegi.archi.exporter.c4.plantuml;

import it.vitalegi.archi.diagram.scope.DiagramScopeBuilder;
import it.vitalegi.archi.diagram.scope.LandscapeDiagramScopeBuilder;
import it.vitalegi.archi.exporter.c4.plantuml.builder.LandscapeDiagramModelBuilder;
import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.LandscapeDiagram;
import it.vitalegi.archi.model.diagramelement.C4DiagramModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LandscapeDiagramPlantumlExporter extends AbstractDiagramPlantumlExporter<LandscapeDiagram> {


    @Override
    public void validate(LandscapeDiagram diagram) {
    }

    @Override
    protected C4DiagramModel buildModel(Workspace workspace, LandscapeDiagram diagram) {
        return new LandscapeDiagramModelBuilder(workspace, diagram).build();
    }

    @Override
    protected DiagramScopeBuilder<LandscapeDiagram> diagramScope(Workspace workspace, LandscapeDiagram diagram) {
        return new LandscapeDiagramScopeBuilder(diagram);
    }
}
