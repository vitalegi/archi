package it.vitalegi.archi.diagram.rule.element;

import it.vitalegi.archi.diagram.DiagramScope;
import it.vitalegi.archi.diagram.rule.AbstractVisibilityRule;
import it.vitalegi.archi.model.Entity;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.relation.DirectRelation;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public class IsElementRule extends AbstractVisibilityRule {

    String id;

    public IsElementRule(String id) {
        this.id = id;
    }

    public boolean match(DiagramScope diagramScope, Element element) {
        if (Entity.equals(id, element.getId())) {
            log.debug("Element {} is {}, match.", element.toShortString(), id);
            return true;
        }
        return false;
    }

    public boolean match(DiagramScope diagramScope, DirectRelation relation) {
        return false;
    }

    @Override
    public String toString() {
        return "is " + id;
    }
}
