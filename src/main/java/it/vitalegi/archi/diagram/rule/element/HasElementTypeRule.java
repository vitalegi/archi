package it.vitalegi.archi.diagram.rule.element;

import it.vitalegi.archi.diagram.dto.DiagramScope;
import it.vitalegi.archi.diagram.rule.AbstractVisibilityRule;
import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.ElementType;
import it.vitalegi.archi.model.Relation;
import it.vitalegi.archi.util.WorkspaceUtil;
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
public class HasElementTypeRule extends AbstractVisibilityRule {

    List<ElementType> types;

    public HasElementTypeRule(ElementType... rules) {
        this(new ArrayList<>(List.of(rules)));
    }

    public HasElementTypeRule(List<ElementType> types) {
        this.types = types;
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
