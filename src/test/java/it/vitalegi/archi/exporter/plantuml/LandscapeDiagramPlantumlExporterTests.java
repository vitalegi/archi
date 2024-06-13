package it.vitalegi.archi.exporter.plantuml;

import it.vitalegi.archi.model.diagram.LandscapeDiagram;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static it.vitalegi.archi.util.WorkspaceTestUtil.b;
import static it.vitalegi.archi.util.WorkspaceTestUtil.load;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class LandscapeDiagramPlantumlExporterTests {

    LandscapeDiagramPlantumlExporter diagramProcessor;

    @BeforeEach
    void init() {
        diagramProcessor = new LandscapeDiagramPlantumlExporter();
    }

    @Nested
    class Loader {
        @Test
        void given_correctConfiguration_thenSucceed() {
            var ws = load(b().landscapeDiagram("A", "B"));
            var diagram = ws.getDiagrams().getByName("A");
            assertNotNull(diagram);
            assertEquals(diagram.getClass(), LandscapeDiagram.class);
            var d = (LandscapeDiagram) diagram;
            assertEquals("A", d.getName());
            assertEquals("B", d.getTitle());
        }
    }
}
