package it.vitalegi.archi.diagram.rule.relation;

import it.vitalegi.archi.diagram.dto.DiagramScope;
import it.vitalegi.archi.diagram.rule.AbstractVisibilityRule;
import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.Relation;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor

public class AlwaysAllowRelationRule extends AbstractVisibilityRule {
    public boolean match(DiagramScope diagramScope, Element element) {
        return false;
    }

    public boolean match(DiagramScope diagramScope, Relation relation) {
        return true;
    }
}
