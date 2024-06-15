package it.vitalegi.archi.diagram.rule;

import it.vitalegi.archi.diagram.DiagramScope;
import it.vitalegi.archi.model.Entity;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.relation.DirectRelation;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class MatchesElementByIdRule extends AbstractVisibilityRule {
    String id;

    public boolean match(DiagramScope diagramScope, Element element) {
        return Entity.equals(element.getUniqueId(), id);
    }

    public boolean match(DiagramScope diagramScope, DirectRelation relation) {
        return Entity.equals(relation.getUniqueId(), id);
    }

    @Override
    public String toString() {
        return "is " + id;
    }
}
