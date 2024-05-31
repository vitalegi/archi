package it.vitalegi.archi.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Data
@ToString(exclude = "model")
public abstract class Element {
    @Getter
    protected Model model;
    @Setter(AccessLevel.PROTECTED)
    @Getter
    Node parent;
    String id;
    String name;
    String description;
    List<String> tags;
    Map<String, String> metadata;

    public Element(Model model) {
        tags = new ArrayList<>();
        metadata = new HashMap<>();
        this.model = model;
    }

    public static boolean sameId(String id1, String id2) {
        return Objects.equals(id1, id2);
    }

    public static String collectIds(List<? extends Element> elements) {
        return elements.stream().map(Element::getId).collect(Collectors.joining(", "));
    }

    public abstract void addChild(Element child);

    public boolean sameId(String id) {
        return sameId(this.id, id);
    }

    public String toShortString() {
        return getClass().getSimpleName() + " (" + getId() + ")";
    }

    public List<Element> getPathFromRoot() {
        List<Element> elements = new ArrayList<>();
        var curr = this;
        while (curr != null) {
            elements.add(0, curr);
            curr = curr.getParent();
        }
        return elements;
    }

    public void validate() {
    }
}
