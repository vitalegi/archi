package it.vitalegi.archi.workspace.loader;

import it.vitalegi.archi.view.ViewService;
import it.vitalegi.archi.view.validator.ViewValidator;
import lombok.Setter;

@Setter
public class WorkspaceLoaderFactory {
    ViewValidator viewValidator;

    public WorkspaceLoaderFactory() {
        viewValidator = new ViewService();
    }

    public WorkspaceLoader build() {
        return new WorkspaceLoader(viewValidator);
    }
}
