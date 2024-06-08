package it.vitalegi.archi.diagram;

import it.vitalegi.archi.diagram.constant.DiagramFormat;
import it.vitalegi.archi.diagram.model.Diagram;

import java.nio.file.Path;

public interface DiagramProcessor {
    boolean accept(Diagram diagram);
    void validate(Diagram diagram);
    void render(Diagram diagram, Path basePath, DiagramFormat[] formats);
}
