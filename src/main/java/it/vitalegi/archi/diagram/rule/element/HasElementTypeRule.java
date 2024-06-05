package it.vitalegi.archi.diagram.rule.element;

import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.ElementType;
import it.vitalegi.archi.model.Relation;
import it.vitalegi.archi.util.WorkspaceUtil;
import it.vitalegi.archi.diagram.dto.DiagramScope;
import it.vitalegi.archi.diagram.rule.AbstractVisibilityRule;
import it.vitalegi.archi.diagram.rule.VisibilityRuleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HasElementTypeRule extends AbstractVisibilityRule {

    List<ElementType> types;

    public HasElementTypeRule(VisibilityRuleType ruleType, List<ElementType> types) {
        super(ruleType);
        this.types = types;
    }

    public HasElementTypeRule() {
        super();
    }

    public boolean match(DiagramScope diagramScope, Element element) {
        for (var type : types) {
            if (WorkspaceUtil.isSameType(element, type)) {
                return true;
            }
        }
        return false;
    }

    public boolean match(DiagramScope diagramScope, Relation relation) {
        return false;
    }

}
