package it.vitalegi.archi.diagram.rule.element;

import it.vitalegi.archi.diagram.DiagramScope;
import it.vitalegi.archi.diagram.rule.AbstractVisibilityRule;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.relation.DirectRelation;
import it.vitalegi.archi.util.WorkspaceUtil;
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
public class AnyAncestorOfElementInScopeRule extends AbstractVisibilityRule {
    public boolean match(DiagramScope diagramScope, Element element) {
        var ancestors = WorkspaceUtil.getPathFromRoot(element);
        for (var ancestor : ancestors) {
            if (diagramScope.isInScope(ancestor)) {
                log.debug("Element {}, ancestor of {}, is in scope, match.", ancestor.toShortString(), element.toShortString());
                return true;
            }
        }
        return false;
    }

    public boolean match(DiagramScope diagramScope, DirectRelation relation) {
        return false;
    }

    @Override
    public String toString() {
        return "any ancestor in scope";
    }
}
