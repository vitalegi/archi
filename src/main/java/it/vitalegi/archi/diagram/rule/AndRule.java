package it.vitalegi.archi.diagram.rule;

import it.vitalegi.archi.diagram.DiagramScope;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.relation.DirectRelation;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public class AndRule extends AbstractVisibilityRule {

    List<VisibilityRule> rules;

    public AndRule(List<VisibilityRule> rules) {
        this.rules = rules;
    }

    public AndRule(VisibilityRule... rules) {
        this(new ArrayList<>(List.of(rules)));
    }

    public boolean match(DiagramScope diagramScope, Element element) {
        return rules.stream().allMatch(rule -> rule.match(diagramScope, element));
    }

    public boolean match(DiagramScope diagramScope, DirectRelation relation) {
        return rules.stream().allMatch(rule -> rule.match(diagramScope, relation));
    }

    @Override
    public String toString() {
        return "(" + rules.stream().map(VisibilityRule::toString).collect(Collectors.joining(" and ")) + ")";
    }
}
