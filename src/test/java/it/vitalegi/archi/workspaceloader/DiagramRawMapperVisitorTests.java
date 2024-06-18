package it.vitalegi.archi.workspaceloader;

import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.DiagramOptions;
import it.vitalegi.archi.workspaceloader.model.LandscapeDiagramRaw;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class DiagramRawMapperVisitorTests {

    @Test
    void given_diagram_when_noOptions_then_defaultOptionsAreLoaded() {
        var model = new Model();
        var workspace = new Workspace();
        model.setWorkspace(workspace);

        var raw = LandscapeDiagramRaw.builder().name("name").build();
        var mapper = new DiagramRawMapperVisitor(model);
        var diagram = raw.visit(mapper);
        assertEquals(DiagramOptions.defaultOptions(), diagram.getOptionsAggregated());
    }

    @Test
    void given_diagram_when_optionsAvailable_then_optionsArePropagated() {
        var model = new Model();
        var workspace = new Workspace();
        model.setWorkspace(workspace);

        var options = DiagramOptions.builder().inheritRelations(true).build();
        var raw = LandscapeDiagramRaw.builder().name("name").options(options).build();
        var mapper = new DiagramRawMapperVisitor(model);
        var diagram = raw.visit(mapper);
        assertTrue(diagram.getOptionsAggregated().isInheritRelations());
    }
}
