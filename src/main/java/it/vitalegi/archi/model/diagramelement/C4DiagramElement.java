package it.vitalegi.archi.model.diagramelement;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class C4DiagramElement {

    String id;
    List<C4DiagramElement> children;
    String name;
    String description;
    List<String> tags;
    List<String> technologies;
    List<C4DiagramElementProperty> properties;

    public C4DiagramElement() {
        children = new ArrayList<>();
        tags = new ArrayList<>();
        properties = new ArrayList<>();
    }

    public void addChild(C4DiagramElement element) {
        children.add(element);
    }
}
