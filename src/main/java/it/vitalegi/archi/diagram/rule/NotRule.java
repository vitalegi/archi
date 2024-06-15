package it.vitalegi.archi.diagram.rule;

import it.vitalegi.archi.diagram.DiagramScope;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.relation.DirectRelation;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public class NotRule extends AbstractVisibilityRule {

    VisibilityRule rule;

    public NotRule(VisibilityRule rule) {
        this.rule = rule;
    }

    public boolean match(DiagramScope diagramScope, Element element) {
        var result = rule.match(diagramScope, element);
        log.debug("Negate result for {}, was {}, becomes {}", element.toShortString(), result, !result);
        return !result;
    }

    public boolean match(DiagramScope diagramScope, DirectRelation relation) {
        var result = rule.match(diagramScope, relation);
        log.debug("Negate result for {}, was {}, becomes {}", relation.toShortString(), result, !result);
        return !result;
    }

    @Override
    public String toString() {
        return "NOT( " + rule.toString() + ")";
    }
}
