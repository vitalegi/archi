package it.vitalegi.archi.model.relation;

import it.vitalegi.archi.model.Entity;
import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.element.ElementType;
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
    String label;
    @EqualsAndHashCode.Exclude
    String description;
    @EqualsAndHashCode.Exclude
    String sprite;
    @EqualsAndHashCode.Exclude
    String link;
    @EqualsAndHashCode.Exclude
    List<String> tags;
    @EqualsAndHashCode.Exclude
    String technologies;
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
                ", label='" + label + '\'' +
                ", sprite=" + sprite +
                ", link=" + link +
                ", tags=" + tags +
                ", technologies='" + technologies + '\'' +
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
