package it.vitalegi.archi.diagram.style;

import it.vitalegi.archi.util.StyleTestUtil;
import org.junit.jupiter.api.BeforeEach;
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
public class StyleHandlerTests {

    StyleHandler styleHandler;

    @BeforeEach
    void init() {
        styleHandler = new StyleHandler();
    }

    @Test
    void given_globalConfiguration_then_sameValuesAreReturned() {
        var globalStyle = StyleTestUtil.randomStyle();
        int skinParams = globalStyle.getSkinParams().size();
        var ws = load(b().globalStyle(globalStyle).landscapeDiagram("diagram1"));
        var diagram = ws.getDiagrams().getByName("diagram1");
        var style = styleHandler.buildStyle(ws, diagram);
        assertEquals(globalStyle, style, "If diagram doesn't have a style, use global style");
        style.getSkinParams().remove(0);
        assertNotEquals(skinParams, style.getSkinParams().size(), "Changing the generated style doesn't affect the original ones");
    }

    @Test
    void given_noConfiguration_then_emptyConfigurationIsReturned() {
        var ws = load(b().landscapeDiagram("diagram1"));
        var diagram = ws.getDiagrams().getByName("diagram1");
        var style = styleHandler.buildStyle(ws, diagram);
        assertNotNull(style);
        assertNotNull(style.getSkinParams());
        assertTrue(style.getSkinParams().isEmpty());
    }

    @Test
    void given_globalConfigurationAndDiagramConfiguration_then_mergedValueIsReturned() {
        var globalStyle = StyleTestUtil.randomStyle();
        var ws = load(b().globalStyle(globalStyle).landscapeDiagram("diagram1"));
        var diagram = ws.getDiagrams().getByName("diagram1");
        var style = styleHandler.buildStyle(ws, diagram);
        assertEquals(globalStyle, style, "If diagram doesn't have a style, use global style");
        style.getSkinParams().remove(0);
        assertNotEquals(globalStyle, style, "Changing the generated style doesn't affect the original ones");
    }
}
