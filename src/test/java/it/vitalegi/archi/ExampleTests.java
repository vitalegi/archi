package it.vitalegi.archi;

import it.vitalegi.archi.view.ViewProcessorFacade;
import it.vitalegi.archi.view.constant.ViewFormat;
import it.vitalegi.archi.workspace.loader.FileSystemWorkspaceLoader;
import it.vitalegi.archi.workspace.loader.WorkspaceLoaderFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;

@ExtendWith(MockitoExtension.class)
public class ExampleTests {

    //@Disabled
    @Test
    void simpleWebapp() {
        FileSystemWorkspaceLoader loader = new FileSystemWorkspaceLoader();
        var baseDir = Path.of("examples", "simple-webapp");
        var out = baseDir.resolve("output");
        var viewProcessor = new ViewProcessorFacade();
        var factory = new WorkspaceLoaderFactory();
        factory.setViewProcessorFacade(viewProcessor);
        var ws = factory.build().load(loader.load(baseDir.resolve("workspace.yaml")));

        ws.getViews().getAll().forEach(view -> viewProcessor.render(view, out, ViewFormat.values()));
    }
}
