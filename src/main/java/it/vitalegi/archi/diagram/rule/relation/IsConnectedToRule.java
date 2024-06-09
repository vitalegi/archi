package it.vitalegi.archi.diagram.rule.relation;

import it.vitalegi.archi.model.diagram.DiagramScope;
import it.vitalegi.archi.diagram.rule.AbstractVisibilityRule;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.Entity;
import it.vitalegi.archi.model.relation.Relation;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class IsConnectedToRule extends AbstractVisibilityRule {
    String id;

    public boolean match(DiagramScope diagramScope, Element element) {
        return false;
    }

    public boolean match(DiagramScope diagramScope, Relation relation) {
        if (!diagramScope.isInScope(relation)) {
            return false;
        }
        if (Entity.equals(relation.getFrom().getId(), id)) {
            log.debug("Relation {} is connected to {}, match.", relation.toShortString(), id);
            return true;
        }
        if (Entity.equals(relation.getTo().getId(), id)) {
            log.debug("Relation {} is connected to {}, match.", relation.toShortString(), id);
            return true;
        }
        return false;
    }
}
