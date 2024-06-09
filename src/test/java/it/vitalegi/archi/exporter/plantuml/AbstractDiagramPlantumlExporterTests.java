package it.vitalegi.archi.exporter.plantuml;

import it.vitalegi.archi.model.diagram.Diagram;
import it.vitalegi.archi.util.StyleTestUtil;
import it.vitalegi.archi.model.Workspace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static it.vitalegi.archi.util.WorkspaceTestUtil.b;
import static it.vitalegi.archi.util.WorkspaceTestUtil.load;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class AbstractDiagramPlantumlExporterTests {

    AbstractDiagramPlantumlExporter<?> processor;

    @BeforeEach
    void init() {
        processor = new AbstractDiagramPlantumlExporter<>() {
            @Override
            public void validate(Diagram diagram) {

            }

            @Override
            public String export(Workspace workspace, Diagram diagram) {
                return null;
            }
        };
    }

    @Nested
    class BuildStyle {
        @Test
        void given_globalConfiguration_then_sameValuesAreReturned() {
            var globalStyle = StyleTestUtil.randomStyle();
            int skinParams = globalStyle.getSkinParams().size();
            var ws = load(b().globalStyle(globalStyle).landscapeDiagram("diagram1"));
            var diagram = ws.getDiagrams().getByName("diagram1");
            var style = processor.buildStyle(ws, diagram);
            assertEquals(globalStyle, style, "If diagram doesn't have a style, use global style");
            style.getSkinParams().remove(0);
            assertNotEquals(skinParams, style.getSkinParams().size(), "Changing the generated style doesn't affect the original ones");
        }

        @Test
        void given_noConfiguration_then_emptyConfigurationIsReturned() {
            var ws = load(b().landscapeDiagram("diagram1"));
            var diagram = ws.getDiagrams().getByName("diagram1");
            var style = processor.buildStyle(ws, diagram);
            assertNotNull(style);

            assertNotNull(style.getSkinParams());
            assertTrue(style.getSkinParams().isEmpty());

            assertNotNull(style.getTags());
            assertNotNull(style.getTags().getElements());
            assertTrue(style.getTags().getElements().isEmpty());
            assertNotNull(style.getTags().getRelations());
            assertTrue(style.getTags().getRelations().isEmpty());
        }

        @Test
        void given_globalConfigurationAndDiagramConfiguration_then_mergedValueIsReturned() {
            var globalStyle = StyleTestUtil.randomStyle();
            var ws = load(b().globalStyle(globalStyle).landscapeDiagram("diagram1"));
            var diagram = ws.getDiagrams().getByName("diagram1");
            var style = processor.buildStyle(ws, diagram);
            assertEquals(globalStyle, style, "If diagram doesn't have a style, use global style");
            style.getSkinParams().remove(0);
            assertNotEquals(globalStyle, style, "Changing the generated style doesn't affect the original ones");
        }
    }
}
