package it.vitalegi.archi.diagram.rule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleEntry {
    VisibilityRuleType type;
    VisibilityRule rule;

    public static RuleEntry include(VisibilityRule rule) {
        return new RuleEntry(VisibilityRuleType.INCLUSION, rule);
    }

    public static RuleEntry exclude(VisibilityRule rule) {
        return new RuleEntry(VisibilityRuleType.EXCLUSION, rule);
    }
}
