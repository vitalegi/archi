package it.vitalegi.archi.diagram.dto;

import it.vitalegi.archi.model.Model;
import lombok.Data;

@Data
public class Diagram {
    String name;
    String title;
    Model model;

    public Diagram(Model model) {
        this.model = model;
    }
}