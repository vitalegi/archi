package it.vitalegi.archi.diagram;

import it.vitalegi.archi.model.Entity;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.relation.DirectRelation;
import it.vitalegi.archi.util.Cloneable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DiagramScope implements Cloneable<DiagramScope> {
    Map<String, Element> elements;
    Map<String, DirectRelation> relations;

    public DiagramScope() {
        elements = new HashMap<>();
        relations = new HashMap<>();
    }

    @Override
    public DiagramScope duplicate() {
        var clone = new DiagramScope();
        clone.elements.putAll(this.elements);
        clone.relations.putAll(this.relations);
        return clone;
    }

    @Override
    public String toString() {
        return "DiagramScope: " +
                "elements=" + elements.values().stream().map(Element::toShortString).collect(Collectors.joining(", ")) +
                ", relations=" + relations.values().stream().map(DirectRelation::toShortString).collect(Collectors.joining(", "));
    }

    public void add(Element element) {
        elements.put(getId(element), element);
    }

    public void add(DirectRelation relation) {
        relations.put(getId(relation), relation);
    }

    public void remove(Element element) {
        elements.remove(getId(element));
    }

    public void remove(DirectRelation relation) {
        relations.remove(getId(relation));
    }


    public boolean isInScope(Element element) {
        return elements.containsKey(getId(element));
    }

    public boolean isInScope(DirectRelation relation) {
        return relations.containsKey(getId(relation));
    }

    public List<Element> getElements() {
        return new ArrayList<>(elements.values());
    }

    public List<DirectRelation> getRelations() {
        return new ArrayList<>(relations.values());
    }

    protected String getId(Entity entity) {
        return entity.getUniqueId();
    }

}
