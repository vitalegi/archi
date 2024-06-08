package it.vitalegi.archi.workspaceloader;

import it.vitalegi.archi.diagram.DiagramProcessorFacade;
import lombok.Data;

@Data
public class WorkspaceLoaderFactory {
    DiagramProcessorFacade diagramProcessorFacade;

    public WorkspaceLoaderFactory() {
        diagramProcessorFacade = DiagramProcessorFacade.defaultInstance();
    }

    public WorkspaceLoader build() {
        return new WorkspaceLoader(diagramProcessorFacade);
    }
}
