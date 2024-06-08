package it.vitalegi.archi.diagram.model;

import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.style.model.Style;
import lombok.Data;

@Data
public class Diagram {
    String name;
    String title;
    Style style;
    Model model;

    public Diagram(Model model) {
        this.model = model;
    }
}