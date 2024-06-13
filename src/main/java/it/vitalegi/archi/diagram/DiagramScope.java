package it.vitalegi.archi.diagram;

import it.vitalegi.archi.model.Entity;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.relation.Relation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public List<Element> getElements() {
        return new ArrayList<>(elements.values());
    }

    public List<Relation> getRelations() {
        return new ArrayList<>(relations.values());
    }

    protected String getId(Entity entity) {
        return entity.getUniqueId();
    }
}
