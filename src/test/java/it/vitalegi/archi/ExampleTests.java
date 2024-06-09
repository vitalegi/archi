package it.vitalegi.archi;

import it.vitalegi.archi.model.builder.WorkspaceDirector;
import it.vitalegi.archi.exporter.plantuml.PlantumlDiagramExporter;
import it.vitalegi.archi.diagram.DiagramFormat;
import it.vitalegi.archi.workspaceloader.FileSystemWorkspaceLoader;
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

        var fsLoader = new FileSystemWorkspaceLoader();
        var factory = new WorkspaceDirector();
        factory.makeWorkspace(fsLoader.load(baseDir.resolve("workspace.yaml")));
        var workspace = factory.build();
        var exporter = new PlantumlDiagramExporter(workspace, out, DiagramFormat.values());
        workspace.getDiagrams().getAll().forEach(exporter::export);
    }
}
