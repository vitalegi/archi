package it.vitalegi.archi.workspace.loader.model;

import it.vitalegi.archi.model.view.BaseView;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Workspace {
    List<ElementRaw> elements;
    List<RelationRaw> relations;
    List<BaseView> views;
    public Workspace() {
        elements = new ArrayList<>();
        relations = new ArrayList<>();
        views = new ArrayList<>();
    }
}
