package it.vitalegi.archi;

import it.vitalegi.archi.diagram.DiagramProcessorFacade;
import it.vitalegi.archi.diagram.constant.DiagramFormat;
import it.vitalegi.archi.workspaceloader.FileSystemWorkspaceLoader;
import it.vitalegi.archi.workspaceloader.WorkspaceLoaderFactory;
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
        var diagramProcessor = DiagramProcessorFacade.defaultInstance();
        var factory = new WorkspaceLoaderFactory();
        factory.setDiagramProcessorFacade(diagramProcessor);
        var ws = factory.build().load(loader.load(baseDir.resolve("workspace.yaml")));

        ws.getDiagrams().getAll().forEach(diagram -> diagramProcessor.render(diagram, out, DiagramFormat.values()));
    }
}
