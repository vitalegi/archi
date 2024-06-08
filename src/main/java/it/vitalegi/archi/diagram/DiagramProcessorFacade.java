package it.vitalegi.archi.diagram;

import it.vitalegi.archi.diagram.constant.DiagramFormat;
import it.vitalegi.archi.diagram.model.Diagram;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class DiagramProcessorFacade {

    DiagramFactory diagramFactory;

    public static DiagramProcessorFacade defaultInstance() {
        return new DiagramProcessorFacade(new DiagramFactory());
    }

    public DiagramProcessorFacade(DiagramFactory diagramFactory) {
        this.diagramFactory = diagramFactory;
    }

    public void render(Diagram diagram, Path basePath, DiagramFormat[] formats) {
        getAcceptedDiagramProcessors(diagram).forEach(r -> r.render(diagram, basePath, formats));
    }


    public void validate(Diagram diagram) {
        getAcceptedDiagramProcessors(diagram).forEach(v -> v.validate(diagram));
    }

    protected List<DiagramProcessor> getAcceptedDiagramProcessors(Diagram diagram) {
        return diagramFactory.diagramProcessors().stream().filter(p -> p.accept(diagram)).collect(Collectors.toList());
    }
}
