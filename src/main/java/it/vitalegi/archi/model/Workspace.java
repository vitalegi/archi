package it.vitalegi.archi.model;

import it.vitalegi.archi.model.view.Views;
import lombok.Data;

@Data
public class Workspace {
    Model model;
    Views views;

    public Workspace() {
        model = new Model();
        views = new Views();
    }

    public void validate() {
        model.validate();
    }
}
