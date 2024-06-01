package it.vitalegi.archi;

import it.vitalegi.archi.workspace.loader.FileSystemWorkspaceLoader;
import it.vitalegi.archi.workspace.loader.WorkspaceLoaderFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;

@ExtendWith(MockitoExtension.class)
public class ExampleTests {

    @Test
    void simpleWebapp() {
        FileSystemWorkspaceLoader loader = new FileSystemWorkspaceLoader();
        new WorkspaceLoaderFactory().build().load(loader.load(Path.of("examples", "simple-webapp", "workspace.yaml")));
    }

}
