package it.vitalegi.archi.model;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public abstract class Element {
    String id;
    String name;
    String description;
    List<String> tags;
    Map<String, String> metadata;

    public static String collectIds(List<? extends  Element> elements) {
        return elements.stream().map(Element::getId).collect(Collectors.joining(", "));
    }

    public abstract Element findChildById(String id);

    public abstract void addChild(Element child);
}
