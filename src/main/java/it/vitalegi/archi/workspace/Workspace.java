package it.vitalegi.archi.workspace;

import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.view.dto.Views;
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
