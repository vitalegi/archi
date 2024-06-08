package it.vitalegi.archi.diagram;

import it.vitalegi.archi.diagram.style.StyleHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class DiagramFactory {
    StyleHandler styleHandler;

    public DiagramFactory() {
        styleHandler = new StyleHandler();
    }

    public List<DiagramProcessor> diagramProcessors() {
        return Arrays.asList( //
                deploymentDiagramProcessor(), //
                landscapeDiagramProcessor(), //
                systemContextDiagramProcessor() //
        );
    }

    public DeploymentDiagramProcessor deploymentDiagramProcessor() {
        return new DeploymentDiagramProcessor(styleHandler);
    }

    public LandscapeDiagramProcessor landscapeDiagramProcessor() {
        return new LandscapeDiagramProcessor(styleHandler);
    }

    public SystemContextDiagramProcessor systemContextDiagramProcessor() {
        return new SystemContextDiagramProcessor(styleHandler);
    }
}
