package it.vitalegi.archi.diagram;

import it.vitalegi.archi.diagram.constant.DiagramFormat;
import it.vitalegi.archi.diagram.model.Diagram;
import it.vitalegi.archi.workspace.Workspace;

import java.nio.file.Path;

public interface DiagramProcessor {
    boolean accept(Diagram diagram);
    void validate(Diagram diagram);
    void render(Workspace workspace, Diagram diagram, Path basePath, DiagramFormat[] formats);
}
