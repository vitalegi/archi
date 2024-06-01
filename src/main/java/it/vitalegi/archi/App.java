package it.vitalegi.archi;

import it.vitalegi.archi.workspace.loader.FileSystemWorkspaceLoader;
import it.vitalegi.archi.workspace.loader.WorkspaceLoaderFactory;
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
        var loader = new WorkspaceLoaderFactory().build();
        var workspace = loader.load(fsLoader.load(model));
        log.info("Loaded workspace");

    }
}