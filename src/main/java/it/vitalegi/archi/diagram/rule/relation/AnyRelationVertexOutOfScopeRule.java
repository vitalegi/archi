package it.vitalegi.archi.diagram.rule.relation;

import it.vitalegi.archi.diagram.model.DiagramScope;
import it.vitalegi.archi.diagram.rule.AbstractVisibilityRule;
import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.Relation;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class AnyRelationVertexOutOfScopeRule extends AbstractVisibilityRule {

    public boolean match(DiagramScope diagramScope, Element element) {
        return false;
    }

    public boolean match(DiagramScope diagramScope, Relation relation) {
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
}
