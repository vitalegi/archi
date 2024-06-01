package it.vitalegi.archi.workspace.loader.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Workspace {
    List<ElementRaw> elements;
    List<RelationRaw> relations;

    public Workspace() {
        elements = new ArrayList<>();
        relations = new ArrayList<>();
    }
}
