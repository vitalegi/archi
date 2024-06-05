package it.vitalegi.archi.diagram.rule.relation;

import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.Relation;
import it.vitalegi.archi.diagram.dto.DiagramScope;
import it.vitalegi.archi.diagram.rule.AbstractVisibilityRule;
import it.vitalegi.archi.diagram.rule.VisibilityRuleType;
import lombok.ToString;

@ToString(callSuper = true)
public class AlwaysAllowRelationRule extends AbstractVisibilityRule {
    public AlwaysAllowRelationRule(VisibilityRuleType ruleType) {
        super(ruleType);
    }

    public AlwaysAllowRelationRule() {
    }

    public boolean match(DiagramScope diagramScope, Element element) {
        return false;
    }

    public boolean match(DiagramScope diagramScope, Relation relation) {
        return true;
    }
}
