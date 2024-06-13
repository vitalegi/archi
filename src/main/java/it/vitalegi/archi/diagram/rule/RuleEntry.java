package it.vitalegi.archi.diagram.rule;

import it.vitalegi.archi.diagram.rule.element.AnyDescendantOfElementInScopeRule;
import it.vitalegi.archi.diagram.rule.element.HasElementTypeRule;
import it.vitalegi.archi.diagram.rule.element.IsConnectedToEnvironmentRule;
import it.vitalegi.archi.diagram.rule.element.IsDescendantOfRule;
import it.vitalegi.archi.diagram.rule.element.IsDirectlyConnectedToElementInScopeRule;
import it.vitalegi.archi.diagram.rule.relation.AlwaysAllowRelationRule;
import it.vitalegi.archi.diagram.rule.relation.AnyRelationVertexOutOfScopeRule;
import it.vitalegi.archi.diagram.rule.relation.IsConnectedToRule;
import it.vitalegi.archi.model.element.ElementType;
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

    public static AndRule and(VisibilityRule... rules) {
        return new AndRule(rules);
    }

    public static NotRule not(VisibilityRule rule) {
        return new NotRule(rule);
    }

    public static HasElementTypeRule typeIs(ElementType... types) {
        return new HasElementTypeRule(types);
    }

    public static IsDescendantOfRule isDescendantOf(String target) {
        return new IsDescendantOfRule(target);
    }

    public static IsDirectlyConnectedToElementInScopeRule connectedToElementInScope() {
        return new IsDirectlyConnectedToElementInScopeRule();
    }

    public static AlwaysAllowRelationRule allRelations() {
        return new AlwaysAllowRelationRule();
    }

    public static AnyDescendantOfElementInScopeRule anyDescendantInScope() {
        return new AnyDescendantOfElementInScopeRule();
    }

    public static AnyRelationVertexOutOfScopeRule anyRelationVertexOutOfScope() {
        return new AnyRelationVertexOutOfScopeRule();
    }

    public static IsConnectedToRule isConnectedTo(String target) {
        return new IsConnectedToRule(target);
    }

    public static IsConnectedToEnvironmentRule isConnectedToEnvironment(String env) {
        return new IsConnectedToEnvironmentRule(env);
    }
}
