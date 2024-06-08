package it.vitalegi.archi.diagram.rule.element;

import it.vitalegi.archi.diagram.model.DiagramScope;
import it.vitalegi.archi.diagram.rule.AbstractVisibilityRule;
import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.Relation;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class IsDirectlyConnectedToElementInScopeRule extends AbstractVisibilityRule {

    public boolean match(DiagramScope diagramScope, Element element) {
        var relations = element.getModel().getRelations();
        var from = relations.getRelationsFrom(element);
        for (var r: from) {
            if (diagramScope.isInScope(r.getTo())) {
                log.debug("Element {}, connected to {}, is in scope, match.", r.getTo().toShortString(), element.toShortString());
                return true;
            }
        }
        var to = relations.getRelationsTo(element);
        for (var r: to) {
            if (diagramScope.isInScope(r.getFrom())) {
                log.debug("Element {}, connected to {}, is in scope, match.", r.getFrom().toShortString(), element.toShortString());
                return true;
            }
        }
        return false;
    }

    public boolean match(DiagramScope diagramScope, Relation relation) {
        return false;
    }
}
