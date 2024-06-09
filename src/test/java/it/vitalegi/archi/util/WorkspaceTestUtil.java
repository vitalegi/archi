package it.vitalegi.archi.util;

import it.vitalegi.archi.model.builder.WorkspaceDirector;
import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.workspaceloader.model.WorkspaceRaw;

import static it.vitalegi.archi.util.ModelTestUtil.defaultBuilder;

public class WorkspaceTestUtil {


    public static WorkspaceModelBuilder b() {
        return defaultBuilder();
    }

    public static Workspace load(WorkspaceModelBuilder builder) {
        return load(builder.build());
    }

    public static Workspace load(WorkspaceRaw model) {
        var director = new WorkspaceDirector();
        return director.makeWorkspace(model).build();
    }
}
