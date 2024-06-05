package it.vitalegi.archi.diagram.rule;

import it.vitalegi.archi.diagram.dto.DiagramScope;
import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.Relation;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ToString
public class AndRule extends AbstractVisibilityRule {

    List<VisibilityRule> rules;

    public AndRule(VisibilityRuleType ruleType, List<VisibilityRule> rules) {
        super(ruleType);
        this.rules = rules;
    }

    public boolean match(DiagramScope diagramScope, Element element) {
        return rules.stream().allMatch(rule -> rule.match(diagramScope, element));
    }

    public boolean match(DiagramScope diagramScope, Relation relation) {
        return rules.stream().allMatch(rule -> rule.match(diagramScope, relation));
    }

}
