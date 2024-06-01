package it.vitalegi.archi.view.dto;

import it.vitalegi.archi.model.Model;
import lombok.Data;

@Data
public class View {
    String name;
    Model model;

    public View(Model model) {
        this.model = model;
    }
}