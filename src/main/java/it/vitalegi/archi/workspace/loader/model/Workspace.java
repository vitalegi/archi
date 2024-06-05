package it.vitalegi.archi.workspace.loader.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Workspace {
    List<ElementRaw> elements;
    List<RelationRaw> relations;
    List<DiagramRaw> diagrams;

    public Workspace() {
        elements = new ArrayList<>();
        relations = new ArrayList<>();
        diagrams = new ArrayList<>();
    }
}
