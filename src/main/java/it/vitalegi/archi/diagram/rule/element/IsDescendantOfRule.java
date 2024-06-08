package it.vitalegi.archi.diagram.rule.element;

import it.vitalegi.archi.diagram.model.DiagramScope;
import it.vitalegi.archi.diagram.rule.AbstractVisibilityRule;
import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.Entity;
import it.vitalegi.archi.model.Relation;
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
public class IsDescendantOfRule extends AbstractVisibilityRule {

    String id;

    public IsDescendantOfRule(String id) {
        this.id = id;
    }

    public boolean match(DiagramScope diagramScope, Element element) {
        var ancestors = element.getPathFromRoot();
        for (var ancestor : ancestors) {
            if (Entity.equals(id, ancestor.getId())) {
                log.debug("Element {} is ancestor of {}, match.", ancestor.toShortString(), element.toShortString());
                return true;
            }
        }
        return false;
    }

    public boolean match(DiagramScope diagramScope, Relation relation) {
        return false;
    }

}
