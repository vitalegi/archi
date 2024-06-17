package it.vitalegi.archi;

import it.vitalegi.archi.diagram.DiagramFormat;
import it.vitalegi.archi.exporter.c4.plantuml.PlantumlDiagramExporter;
import it.vitalegi.archi.model.builder.WorkspaceDirector;
import it.vitalegi.archi.workspaceloader.FileSystemWorkspaceLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;

@ExtendWith(MockitoExtension.class)
public class ExampleTests {

    @Test
    void simpleWebapp() {
        build(Path.of("examples", "simple-webapp"));
    }

    @Test
    void properties() {
        build(Path.of("examples", "properties"));
    }

    void build(Path baseDir) {
        var out = baseDir.resolve("output");

        var fsLoader = new FileSystemWorkspaceLoader();
        var factory = new WorkspaceDirector();
        factory.makeWorkspace(fsLoader.load(baseDir));
        var workspace = factory.build();
        var exporter = new PlantumlDiagramExporter(workspace, out, DiagramFormat.values());
        workspace.getDiagrams().getAll().forEach(exporter::export);
    }
}
