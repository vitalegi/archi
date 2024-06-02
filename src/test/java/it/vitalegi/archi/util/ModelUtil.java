package it.vitalegi.archi.util;

import it.vitalegi.archi.workspace.loader.WorkspaceLoader;
import it.vitalegi.archi.workspace.loader.WorkspaceLoaderFactory;

public class ModelUtil {
    public static WorkspaceLoader defaultLoader() {
        return new WorkspaceLoaderFactory().build();
    }

    public static WorkspaceLoaderBuilder defaultBuilder() {
        return new WorkspaceLoaderBuilder();
    }

}
