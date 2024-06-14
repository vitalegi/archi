package it.vitalegi.archi.exporter.c4.plantuml;

import it.vitalegi.archi.exception.ElementNotFoundException;
import it.vitalegi.archi.exporter.c4.plantuml.SystemContextDiagramPlantumlExporter;
import it.vitalegi.archi.model.diagram.SystemContextDiagram;
import org.junit.jupiter.api.Assertions;
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
public class SystemContextDiagramPlantumlExporterTests {

    SystemContextDiagramPlantumlExporter diagramProcessor;

    @BeforeEach
    void init() {
        diagramProcessor = new SystemContextDiagramPlantumlExporter();
    }

    @Nested
    class Loader {
        @Test
        void given_correctConfiguration_thenSucceed() {
            var ws = load(b().softwareSystem("B").systemContextDiagram("A", "B", "title"));
            var diagram = ws.getDiagrams().getByName("A");
            assertNotNull(diagram);
            assertEquals(diagram.getClass(), SystemContextDiagram.class);
            var d = (SystemContextDiagram) diagram;
            assertEquals("A", d.getName());
            assertEquals("title", d.getTitle());
            assertEquals("B", d.getTarget());
        }


        @Test
        void given_missingTargetField_thenFail() {
            var e = Assertions.assertThrows(IllegalArgumentException.class, () -> load(b() //
                    .systemContextDiagram("diagram1", "", "123") //
            ));
            assertEquals("Diagram diagram1, missing target.", e.getMessage());
        }

        @Test
        void given_missingTarget_thenFail() {
            var e = Assertions.assertThrows(ElementNotFoundException.class, () -> load(b() //
                    .systemContextDiagram("diagram1", "ss", "123") //
            ));
            assertEquals("Can't find ss: invalid target on Diagram diagram1", e.getMessage());
        }

        @Test
        void given_targetIsNotSoftwareSystem_thenFail() {
            var e = Assertions.assertThrows(IllegalArgumentException.class, () -> load(b() //
                    .systemContextDiagram("diagram1", "c", "123") //
                    .softwareSystem("ss") //
                    .container("ss", "c")));
            assertEquals("Diagram diagram1, invalid target. Expected: SoftwareSystem, Actual: Container (c)", e.getMessage());
        }
    }
}
