package it.vitalegi.archi.model;

import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.element.ElementType;
import it.vitalegi.archi.model.flow.Flow;
import it.vitalegi.archi.model.relation.RelationManager;
import it.vitalegi.archi.visitor.ElementVisitor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Slf4j
@ToString(callSuper = true)
public class Model extends Element {
    @Setter
    Workspace workspace;
    Map<String, Element> elementMap;
    RelationManager relationManager;
    List<Flow> flows;

    public Model() {
        super(null);
        model = this;
        elementMap = new HashMap<>();
        relationManager = new RelationManager();
        flows = new ArrayList<>();
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

    public Flow findFlowById(String id) {
        return flows.stream().filter(f -> f.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public <E> E visit(ElementVisitor<E> visitor) {
        return visitor.visitModel(this);
    }
}
