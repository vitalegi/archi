package it.vitalegi.archi.workspace;

import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.diagram.model.Diagrams;
import it.vitalegi.archi.style.Style;
import lombok.Data;

@Data
public class Workspace {
    Model model;
    Diagrams diagrams;
    Style style;

    public void validate() {
        model.validate();
    }
}
