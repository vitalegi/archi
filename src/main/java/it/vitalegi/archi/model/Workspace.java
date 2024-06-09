package it.vitalegi.archi.model;

import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.model.diagram.Diagrams;
import it.vitalegi.archi.model.style.Style;
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
