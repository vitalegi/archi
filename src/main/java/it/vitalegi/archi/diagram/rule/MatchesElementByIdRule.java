package it.vitalegi.archi.diagram.rule;

import it.vitalegi.archi.diagram.model.DiagramScope;
import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.Entity;
import it.vitalegi.archi.model.Relation;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class MatchesElementByIdRule extends AbstractVisibilityRule {
    String id;

    public boolean match(DiagramScope diagramScope, Element element) {
        return Entity.equals(element.getUniqueId(), id);
    }

    public boolean match(DiagramScope diagramScope, Relation relation) {
        return Entity.equals(relation.getUniqueId(), id);
    }
}
