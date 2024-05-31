package it.vitalegi.archi.model;

import lombok.Data;

@Data
public class Workspace {
    Model model;

    public Workspace() {
        model = new Model();
    }
}
