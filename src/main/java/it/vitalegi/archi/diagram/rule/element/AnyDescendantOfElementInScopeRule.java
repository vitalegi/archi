package it.vitalegi.archi.diagram.rule.element;

import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.Relation;
import it.vitalegi.archi.diagram.dto.DiagramScope;
import it.vitalegi.archi.diagram.rule.AbstractVisibilityRule;
import it.vitalegi.archi.diagram.rule.VisibilityRuleType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public class AnyDescendantOfElementInScopeRule extends AbstractVisibilityRule {

    public boolean match(DiagramScope diagramScope, Element element) {
        var children = element.getAllChildren();

        var firstMatch = children.filter(diagramScope::isInScope).findFirst().orElse(null);
        if (firstMatch != null) {
            log.debug("Element {}, descendant of {}, is in scope, match.", firstMatch.toShortString(), element.toShortString());
            return true;
        }
        return false;
    }

    public boolean match(DiagramScope diagramScope, Relation relation) {
        return false;
    }
}
