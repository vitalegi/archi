package it.vitalegi.archi.diagram.rule.element;

import it.vitalegi.archi.diagram.DiagramScope;
import it.vitalegi.archi.diagram.rule.AbstractVisibilityRule;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.relation.DirectRelation;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class IsDirectlyConnectedToElementInScopeRule extends AbstractVisibilityRule {

    public boolean match(DiagramScope diagramScope, Element element) {
        var relations = element.getModel().getRelationManager();
        var from = relations.getDirect().getRelationsFrom(element);
        for (var r : from) {
            if (diagramScope.isInScope(r.getTo())) {
                log.debug("Element {}, connected to {}, is in scope, match.", r.getTo().toShortString(), element.toShortString());
                return true;
            }
        }
        var to = relations.getDirect().getRelationsTo(element);
        for (var r : to) {
            if (diagramScope.isInScope(r.getFrom())) {
                log.debug("Element {}, connected to {}, is in scope, match.", r.getFrom().toShortString(), element.toShortString());
                return true;
            }
        }
        return false;
    }

    public boolean match(DiagramScope diagramScope, DirectRelation relation) {
        return false;
    }

    @Override
    public String toString() {
        return "is connected to anything in scope";
    }
}
