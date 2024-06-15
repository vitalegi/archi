package it.vitalegi.archi.model;

import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.element.ElementType;
import it.vitalegi.archi.model.relation.RelationManager;
import it.vitalegi.archi.visitor.ElementVisitor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Slf4j
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Model extends Element {
    Map<String, Element> elementMap;
    RelationManager relationManager;

    public Model() {
        super(null);
        model = this;
        elementMap = new HashMap<>();
        relationManager = new RelationManager();
    }

    public Element getElementById(String id) {
        return elementMap.get(id);
    }

    public List<Element> getAllElements() {
        return new ArrayList<>(elementMap.values());
    }

    public String toShortString() {
        return getClass().getSimpleName();
    }

    @Override
    public void validate() {
        getAllElements().forEach(Element::validate);
    }

    public ElementType getElementType() {
        return null;
    }

    @Override
    public <E> E visit(ElementVisitor<E> visitor) {
        return visitor.visitModel(this);
    }
}
