package it.vitalegi.archi;

import it.vitalegi.archi.diagram.constant.DiagramFormat;
import it.vitalegi.archi.workspaceloader.FileSystemWorkspaceLoader;
import it.vitalegi.archi.workspaceloader.WorkspaceLoaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("Expected 2 arguments: path/to/file.dsl path/to/out/dir/");
        }
        var model = Path.of(args[0]);
        var mainDir = Path.of(args[1]);

        log.info("Model:      {}", model);
        log.info("Output dir: {}", mainDir);
        var fsLoader = new FileSystemWorkspaceLoader();
        var factory = new WorkspaceLoaderFactory();
        var loader = factory.build();
        var workspace = loader.load(fsLoader.load(model));
        log.info("Loaded workspace");
        workspace.getDiagrams().getAll().forEach(diagram -> factory.getDiagramProcessorFacade().render(diagram, mainDir, DiagramFormat.values()));
    }
}