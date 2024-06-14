package it.vitalegi.archi.workspaceloader;

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
        var raw = LandscapeDiagramRaw.builder().name("name").build();
        var mapper = new DiagramRawMapperVisitor(null);
        var diagram = raw.visit(mapper);
        assertEquals(DiagramOptions.defaultOptions(), diagram.getOptions());
    }

    @Test
    void given_diagram_when_optionsAvailable_then_optionsArePropagated() {
        var options = DiagramOptions.builder().inheritRelations(true).build();
        var raw = LandscapeDiagramRaw.builder().name("name").options(options).build();
        var mapper = new DiagramRawMapperVisitor(null);
        var diagram = raw.visit(mapper);
        assertTrue(diagram.getOptions().isInheritRelations());
    }
}
