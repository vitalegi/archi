package it.vitalegi.archi.util;

import it.vitalegi.archi.model.builder.WorkspaceDirector;

public class ModelTestUtil {
    public static WorkspaceDirector defaultLoader() {
        return new WorkspaceDirector();
    }

    public static WorkspaceModelBuilder defaultBuilder() {
        return new WorkspaceModelBuilder();
    }

}
