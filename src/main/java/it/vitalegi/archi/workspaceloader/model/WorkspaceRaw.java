package it.vitalegi.archi.workspaceloader.model;

import it.vitalegi.archi.style.model.Style;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WorkspaceRaw {
    List<ElementRaw> elements;
    List<RelationRaw> relations;
    List<DiagramRaw> diagrams;
    Style style;

    public WorkspaceRaw() {
        elements = new ArrayList<>();
        relations = new ArrayList<>();
        diagrams = new ArrayList<>();
    }
}
