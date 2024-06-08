package it.vitalegi.archi.util;

import it.vitalegi.archi.workspace.Workspace;

import static it.vitalegi.archi.util.ModelTestUtil.defaultBuilder;
import static it.vitalegi.archi.util.ModelTestUtil.defaultLoader;

public class WorkspaceTestUtil {

    public static Workspace load(WorkspaceLoaderBuilder builder) {
        return defaultLoader().load(builder.build());
    }

    public static WorkspaceLoaderBuilder b() {
        return defaultBuilder();
    }

}
