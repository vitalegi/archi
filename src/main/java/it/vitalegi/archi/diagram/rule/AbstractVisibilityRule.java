package it.vitalegi.archi.diagram.rule;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public abstract class AbstractVisibilityRule implements VisibilityRule {

    VisibilityRuleType ruleType;

    @Override
    public VisibilityRuleType getRuleType() {
        return ruleType;
    }
}
