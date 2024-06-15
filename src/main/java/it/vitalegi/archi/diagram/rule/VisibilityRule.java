package it.vitalegi.archi.diagram.rule;

import it.vitalegi.archi.diagram.DiagramScope;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.relation.DirectRelation;

public interface VisibilityRule {

    boolean match(DiagramScope diagramScope, Element element);

    boolean match(DiagramScope diagramScope, DirectRelation relation);
}
