package it.vitalegi.archi.diagram.model;

import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.Entity;
import it.vitalegi.archi.model.Relation;

import java.util.HashMap;
import java.util.Map;

public class DiagramScope {
    Map<String, Element> elements;
    Map<String, Relation> relations;

    public DiagramScope() {
        elements = new HashMap<>();
        relations = new HashMap<>();
    }

    public void add(Element element) {
        elements.put(getId(element), element);
    }

    public void add(Relation relation) {
        relations.put(getId(relation), relation);
    }

    public void remove(Element element) {
        elements.remove(getId(element));
    }

    public void remove(Relation relation) {
        relations.remove(getId(relation));
    }


    public boolean isInScope(Element element) {
        return elements.containsKey(getId(element));
    }

    public boolean isInScope(Relation relation) {
        return relations.containsKey(getId(relation));
    }

    protected String getId(Entity entity) {
        return entity.getUniqueId();
    }
}
