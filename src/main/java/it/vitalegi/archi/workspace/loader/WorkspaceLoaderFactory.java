package it.vitalegi.archi.workspace.loader;

import it.vitalegi.archi.diagram.DiagramProcessorFacade;
import lombok.Data;

@Data
public class WorkspaceLoaderFactory {
    DiagramProcessorFacade diagramProcessorFacade;

    public WorkspaceLoaderFactory() {
        diagramProcessorFacade = new DiagramProcessorFacade();
    }

    public WorkspaceLoader build() {
        return new WorkspaceLoader(diagramProcessorFacade);
    }
}
