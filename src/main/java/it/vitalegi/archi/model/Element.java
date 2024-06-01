package it.vitalegi.archi.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Getter
@Setter
public abstract class Element extends Entity {
    @Setter(AccessLevel.PROTECTED)
    @Getter
    Node parent;
    String name;
    String description;
    List<String> tags;
    Map<String, String> metadata;

    public Element(Model model) {
        super(model);
        tags = new ArrayList<>();
        metadata = new HashMap<>();
    }

    public static boolean sameId(String id1, String id2) {
        return Objects.equals(id1, id2);
    }

    public abstract void addChild(Element child);

    public List<Element> getPathFromRoot() {
        List<Element> elements = new ArrayList<>();
        var curr = this;
        while (curr != null) {
            elements.add(0, curr);
            curr = curr.getParent();
        }
        return elements;
    }

    @Override
    public String toString() {
        return "Element{" +
                "parent=" + (parent != null ? parent.toShortString() : "null") +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", tags=" + tags +
                ", metadata=" + metadata +
                '}';
    }
}
