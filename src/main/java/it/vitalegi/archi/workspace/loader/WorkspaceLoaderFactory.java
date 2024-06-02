package it.vitalegi.archi.workspace.loader;

import it.vitalegi.archi.view.ViewProcessorFacade;
import lombok.Data;
import lombok.Setter;

@Data
public class WorkspaceLoaderFactory {
    ViewProcessorFacade viewProcessorFacade;

    public WorkspaceLoaderFactory() {
        viewProcessorFacade = new ViewProcessorFacade();
    }

    public WorkspaceLoader build() {
        return new WorkspaceLoader(viewProcessorFacade);
    }
}
