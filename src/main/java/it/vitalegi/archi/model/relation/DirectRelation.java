package it.vitalegi.archi.model.relation;

import it.vitalegi.archi.model.Entity;
import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.element.ElementType;
import it.vitalegi.archi.model.element.PropertyEntries;
import it.vitalegi.archi.util.Cloneable;
import it.vitalegi.archi.visitor.RelationVisitor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DirectRelation extends Entity implements Cloneable<DirectRelation>, Relation {
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
    List<String> technologies;
    @EqualsAndHashCode.Exclude
    PropertyEntries properties;

    public DirectRelation(Model model) {
        super(model);
        tags = new ArrayList<>();
    }

    @Builder
    public DirectRelation(Model model, Element from, Element to, String label, String description, String sprite, String link, List<String> tags, List<String> technologies, PropertyEntries properties) {
        super(model);
        this.from = from;
        this.to = to;
        this.label = label;
        this.description = description;
        this.sprite = sprite;
        this.link = link;
        this.tags = tags;
        this.technologies = technologies;
        this.properties = properties;
    }

    @Override
    public DirectRelation duplicate() {
        var out = new DirectRelation(model);
        out.model = model;
        out.setId(getId());
        out.setUniqueId(getUniqueId());

        out.from = from;
        out.to = to;
        out.label = label;
        out.description = description;
        out.sprite = sprite;
        out.link = link;
        out.tags = new ArrayList<>(tags);
        out.technologies = technologies;
        if (properties != null) {
            out.properties = properties.duplicate();
        }
        return out;
    }

    public ElementType getElementType() {
        return ElementType.RELATION;
    }

    @Override
    public String toString() {
        return "Relation{" + "from=" + (from != null ? from.toShortString() : "null") + ", to=" + (to != null ? to.toShortString() : "null") + ", description='" + description + '\'' + ", label='" + label + '\'' + ", sprite=" + sprite + ", link=" + link + ", tags=" + tags + ", technologies='" + technologies + '\'' + ", properties=" + properties + '}';
    }

    public String toShortString() {
        var fromTo = (from != null ? from.toShortString() : "null") + " => " + (to != null ? to.toShortString() : "null");
        if (getId() != null) {
            return getClass().getSimpleName() + " (" + getId() + ") " + fromTo;
        }
        return getClass().getSimpleName() + " " + fromTo;
    }

    @Override
    public <E> E visit(RelationVisitor<E> visitor) {
        return visitor.visitDirectRelation(this);
    }
}
