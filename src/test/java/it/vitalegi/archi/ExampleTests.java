package it.vitalegi.archi;

import it.vitalegi.archi.view.ViewService;
import it.vitalegi.archi.view.renderer.ViewFormat;
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
        var baseDir = Path.of("examples", "simple-webapp");
        var out = baseDir.resolve("output");

        var ws = new WorkspaceLoaderFactory().build().load(loader.load(baseDir.resolve("workspace.yaml")));
        var viewRenderer = new ViewService();
        ws.getViews().getAll().forEach(view -> viewRenderer.render(view, out, ViewFormat.values()));
    }
}
