package it.vitalegi.archi.diagram.rule;

import it.vitalegi.archi.diagram.dto.DiagramScope;
import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.Relation;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public class AndRule extends AbstractVisibilityRule {

    List<VisibilityRule> rules;

    public AndRule(List<VisibilityRule> rules) {
        this.rules = rules;
    }

    public AndRule(VisibilityRule ... rules) {
        this(new ArrayList<>(List.of(rules)));
    }

    public boolean match(DiagramScope diagramScope, Element element) {
        return rules.stream().allMatch(rule -> rule.match(diagramScope, element));
    }

    public boolean match(DiagramScope diagramScope, Relation relation) {
        return rules.stream().allMatch(rule -> rule.match(diagramScope, relation));
    }

}
