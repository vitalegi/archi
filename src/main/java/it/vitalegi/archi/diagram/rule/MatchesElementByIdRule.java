package it.vitalegi.archi.diagram.rule;

import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.Entity;
import it.vitalegi.archi.model.Relation;
import it.vitalegi.archi.diagram.dto.DiagramScope;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString(callSuper = true)
public class MatchesElementByIdRule extends AbstractVisibilityRule {
    String id;

    public MatchesElementByIdRule(VisibilityRuleType ruleType, String id) {
        super(ruleType);
        this.id = id;
    }

    public MatchesElementByIdRule() {
    }

    public boolean match(DiagramScope diagramScope, Element element) {
        return Entity.equals(element.getUniqueId(), id);
    }

    public boolean match(DiagramScope diagramScope, Relation relation) {
        return Entity.equals(relation.getUniqueId(), id);
    }
}
