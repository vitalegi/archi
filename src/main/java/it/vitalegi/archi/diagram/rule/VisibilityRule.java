package it.vitalegi.archi.diagram.rule;

import it.vitalegi.archi.diagram.model.DiagramScope;
import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.Relation;

public interface VisibilityRule {

    boolean match(DiagramScope diagramScope, Element element);

    boolean match(DiagramScope diagramScope, Relation relation);
}
