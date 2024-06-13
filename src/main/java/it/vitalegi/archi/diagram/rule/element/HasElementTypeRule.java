package it.vitalegi.archi.diagram.rule.element;

import it.vitalegi.archi.diagram.DiagramScope;
import it.vitalegi.archi.diagram.rule.AbstractVisibilityRule;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.element.ElementType;
import it.vitalegi.archi.model.relation.Relation;
import it.vitalegi.archi.util.WorkspaceUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public class HasElementTypeRule extends AbstractVisibilityRule {

    List<ElementType> types;

    public HasElementTypeRule(ElementType... rules) {
        this(new ArrayList<>(List.of(rules)));
    }

    public HasElementTypeRule(List<ElementType> types) {
        this.types = types;
    }

    public boolean match(DiagramScope diagramScope, Element element) {
        for (var type : types) {
            if (WorkspaceUtil.isSameType(element, type)) {
                log.debug("Element {}, matches type {}, match.", element.toShortString(), types);
                return true;
            }
        }
        return false;
    }

    public boolean match(DiagramScope diagramScope, Relation relation) {
        return false;
    }

    @Override
    public String toString() {
        return "is " + types.stream().map(ElementType::name).collect(Collectors.joining(", "));
    }
}
