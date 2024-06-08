package it.vitalegi.archi.workspace;

import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.diagram.model.Diagrams;
import it.vitalegi.archi.style.model.Style;
import lombok.Data;

@Data
public class Workspace {
    Model model;
    Diagrams diagrams;
    Style globalStyle;

    public Workspace() {
        model = new Model();
        diagrams = new Diagrams();
        globalStyle = new Style();
    }

    public void validate() {
        model.validate();
    }
}
