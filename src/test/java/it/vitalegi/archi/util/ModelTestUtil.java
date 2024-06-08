package it.vitalegi.archi.util;

import it.vitalegi.archi.workspaceloader.WorkspaceLoader;
import it.vitalegi.archi.workspaceloader.WorkspaceLoaderFactory;

public class ModelTestUtil {
    public static WorkspaceLoader defaultLoader() {
        return new WorkspaceLoaderFactory().build();
    }

    public static WorkspaceLoaderBuilder defaultBuilder() {
        return new WorkspaceLoaderBuilder();
    }

}
