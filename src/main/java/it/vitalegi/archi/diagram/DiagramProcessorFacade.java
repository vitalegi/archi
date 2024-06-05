package it.vitalegi.archi.diagram;

import it.vitalegi.archi.diagram.constant.DiagramFormat;
import it.vitalegi.archi.diagram.dto.Diagram;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DiagramProcessorFacade {
    private static final List<DiagramProcessor> DIAGRAM_PROCESSORS = Arrays.asList( //
            new DeploymentDiagramProcessor(), //
            new LandscapeDiagramProcessor(), //
            new SystemContextDiagramProcessor() //
    );

    public void render(Diagram diagram, Path basePath, DiagramFormat[] formats) {
        getAcceptedDiagramProcessors(diagram).forEach(r -> r.render(diagram, basePath, formats));
    }


    public void validate(Diagram diagram) {
        getAcceptedDiagramProcessors(diagram).forEach(v -> v.validate(diagram));
    }

    protected List<DiagramProcessor> getAcceptedDiagramProcessors(Diagram diagram) {
        return DIAGRAM_PROCESSORS.stream().filter(p -> p.accept(diagram)).collect(Collectors.toList());
    }
}
