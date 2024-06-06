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
public class AnyAncestorOfElementInScopeRule extends AbstractVisibilityRule {
    public boolean match(DiagramScope diagramScope, Element element) {
        var ancestors = element.getPathFromRoot();
        for (var ancestor : ancestors) {
            if (diagramScope.isInScope(ancestor)) {
                log.debug("Element {}, ancestor of {}, is in scope, match.", ancestor.toShortString(), element.toShortString());
                return true;
            }
        }
        return false;
    }

    public boolean match(DiagramScope diagramScope, Relation relation) {
        return false;
    }
}
