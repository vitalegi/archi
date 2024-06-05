package it.vitalegi.archi.workspace;

import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.diagram.dto.Diagrams;
import lombok.Data;

@Data
public class Workspace {
    Model model;
    Diagrams diagrams;

    public Workspace() {
        model = new Model();
        diagrams = new Diagrams();
    }

    public void validate() {
        model.validate();
    }
}
