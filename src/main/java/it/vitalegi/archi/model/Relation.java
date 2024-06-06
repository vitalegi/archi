package it.vitalegi.archi.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Relation extends Entity {
    @EqualsAndHashCode.Exclude
    Element from;
    @EqualsAndHashCode.Exclude
    Element to;
    @EqualsAndHashCode.Exclude
    String description;
    @EqualsAndHashCode.Exclude
    List<String> tags;
    @EqualsAndHashCode.Exclude
    Map<String, String> metadata;

    public Relation(Model model) {
        super(model);
        tags = new ArrayList<>();
        metadata = new HashMap<>();
    }

    public ElementType getElementType() {
        return ElementType.RELATION;
    }

    @Override
    public String toString() {
        return "Relation{" +
                "from=" + (from != null ? from.toShortString() : "null") +
                ", to=" + (to != null ? to.toShortString() : "null") +
                ", description='" + description + '\'' +
                ", tags=" + tags +
                ", metadata=" + metadata +
                '}';
    }

    public String toShortString() {
        var fromTo = (from != null ? from.toShortString() : "null") + " => " + (to != null ? to.toShortString() : "null");
        if (getId() != null) {
            return getClass().getSimpleName() + " (" + getId() + ") " + fromTo;
        }
        return getClass().getSimpleName() + " " + fromTo;
    }
}
