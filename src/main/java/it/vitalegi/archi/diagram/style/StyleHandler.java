package it.vitalegi.archi.diagram.style;

import it.vitalegi.archi.diagram.model.Diagram;
import it.vitalegi.archi.style.model.Style;
import it.vitalegi.archi.workspace.Workspace;

public class StyleHandler {
    public Style buildStyle(Workspace workspace, Diagram diagram) {
        return Style.merge(workspace.getGlobalStyle(), diagram.getStyle());
    }

}
