package it.vitalegi.archi;

import it.vitalegi.archi.diagram.DiagramFormat;
import it.vitalegi.archi.exporter.c4.plantuml.PlantumlDiagramExporter;
import it.vitalegi.archi.model.builder.WorkspaceDirector;
import it.vitalegi.archi.workspaceloader.FileSystemWorkspaceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("Expected 2 arguments: path/to/workspace/directory/ output/");
        }
        var modelDir = Path.of(args[0]);
        var outputDir = Path.of(args[1]);
        execute(modelDir, outputDir);
    }

    public static void execute(Path modelDir, Path outputDir) {
        log.info("Model dir:  {}", modelDir);
        log.info("Output dir: {}", outputDir);
        var fsLoader = new FileSystemWorkspaceLoader();
        var factory = new WorkspaceDirector();
        factory.makeWorkspace(fsLoader.load(modelDir));
        var workspace = factory.build();
        log.info("Loaded workspace");
        var exporter = new PlantumlDiagramExporter(workspace, outputDir, DiagramFormat.values());
        workspace.getDiagrams().getAll().forEach(exporter::export);
    }
}