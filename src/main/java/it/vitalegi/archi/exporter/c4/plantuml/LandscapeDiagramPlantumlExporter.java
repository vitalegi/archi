package it.vitalegi.archi.exporter.c4.plantuml;

import it.vitalegi.archi.diagram.scope.DiagramScopeBuilder;
import it.vitalegi.archi.diagram.scope.LandscapeDiagramScopeBuilder;
import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.LandscapeDiagram;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LandscapeDiagramPlantumlExporter extends AbstractDiagramPlantumlExporter<LandscapeDiagram> {


    @Override
    public void validate(LandscapeDiagram diagram) {
    }

    @Override
    protected DiagramScopeBuilder<LandscapeDiagram> diagramScope(Workspace workspace, LandscapeDiagram diagram) {
        return new LandscapeDiagramScopeBuilder(diagram);
    }
}
