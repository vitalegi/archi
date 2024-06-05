package it.vitalegi.archi.diagram.rule.relation;

import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.Relation;
import it.vitalegi.archi.diagram.dto.DiagramScope;
import it.vitalegi.archi.diagram.rule.AbstractVisibilityRule;
import it.vitalegi.archi.diagram.rule.VisibilityRuleType;
import lombok.ToString;

@ToString(callSuper = true)
public class AnyRelationVertexOutOfScopeRule extends AbstractVisibilityRule {
    public AnyRelationVertexOutOfScopeRule(VisibilityRuleType ruleType) {
        super(ruleType);
    }

    public AnyRelationVertexOutOfScopeRule() {
    }

    public boolean match(DiagramScope diagramScope, Element element) {
        return false;
    }

    public boolean match(DiagramScope diagramScope, Relation relation) {
        if (!diagramScope.isInScope(relation)) {
            return false;
        }
        if (!diagramScope.isInScope(relation.getFrom())) {
            return true;
        }
        if (!diagramScope.isInScope(relation.getTo())) {
            return true;
        }
        return false;
    }
}
