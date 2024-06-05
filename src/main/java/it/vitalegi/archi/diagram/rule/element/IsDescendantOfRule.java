package it.vitalegi.archi.diagram.rule.element;

import it.vitalegi.archi.diagram.dto.DiagramScope;
import it.vitalegi.archi.diagram.rule.AbstractVisibilityRule;
import it.vitalegi.archi.diagram.rule.VisibilityRuleType;
import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.ElementType;
import it.vitalegi.archi.model.Entity;
import it.vitalegi.archi.model.Relation;
import it.vitalegi.archi.util.WorkspaceUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class IsDescendantOfRule extends AbstractVisibilityRule {

    String id;

    public IsDescendantOfRule(VisibilityRuleType ruleType, String id) {
        super(ruleType);
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
