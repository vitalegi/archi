package it.vitalegi.archi.diagram.rule.relation;

import it.vitalegi.archi.diagram.DiagramScope;
import it.vitalegi.archi.diagram.rule.AbstractVisibilityRule;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.relation.DirectRelation;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class AnyRelationVertexOutOfScopeRule extends AbstractVisibilityRule {

    public boolean match(DiagramScope diagramScope, Element element) {
        return false;
    }

    public boolean match(DiagramScope diagramScope, DirectRelation relation) {
        if (!diagramScope.isInScope(relation)) {
            return false;
        }
        if (!diagramScope.isInScope(relation.getFrom())) {
            log.debug("Relation {}, has vertex {} out of scope, match.", relation.toShortString(), relation.getFrom().toShortString());
            return true;
        }
        if (!diagramScope.isInScope(relation.getTo())) {
            log.debug("Relation {}, has vertex {} out of scope, match.", relation.toShortString(), relation.getTo().toShortString());
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "has vertex out of scope";
    }
}
