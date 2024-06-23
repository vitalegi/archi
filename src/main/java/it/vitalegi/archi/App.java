package it.vitalegi.archi;

import it.vitalegi.archi.diagram.DiagramFormat;
import it.vitalegi.archi.exporter.c4.plantuml.PlantumlDiagramExporter;
import it.vitalegi.archi.model.builder.WorkspaceDirector;
import it.vitalegi.archi.model.diagram.Diagram;
import it.vitalegi.archi.workspaceloader.FileSystemWorkspaceLoader;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.List;

@Slf4j
public class App {

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
        export(exporter, workspace.getDiagrams().getAll());
    }

    protected static void export(PlantumlDiagramExporter exporter, List<Diagram> diagrams) {
        diagrams.parallelStream().forEach(diagram -> export(exporter, diagram));
    }

    protected static void export(PlantumlDiagramExporter exporter, Diagram diagram) {
        log.info("Export of {} started", diagram.getName());
        var startTime = System.currentTimeMillis();
        exporter.export(diagram);
        var duration = System.currentTimeMillis() - startTime;
        log.info("Export of {} done in {}ms", diagram.getName(), duration);
    }
}