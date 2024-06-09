package it.vitalegi.archi.diagram.style;

import it.vitalegi.archi.diagram.model.Diagram;
import it.vitalegi.archi.style.model.Style;
import it.vitalegi.archi.style.model.Tags;
import it.vitalegi.archi.workspace.Workspace;

import java.util.ArrayList;

public class StyleHandler {

    private static Style defaultStyle() {
        var style = new Style();
        style.setSkinParams(new ArrayList<>());
        style.setTags(new Tags());
        return style;
    }

    public Style buildStyle(Workspace workspace, Diagram diagram) {
        var defaultStyle = defaultStyle();
        var style1 = workspace.getStyle();
        var style2 = diagram.getStyle();
        return defaultStyle.merge(style1).merge(style2);
    }

}
