package it.vitalegi.archi.diagram.rule;

import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.Relation;
import it.vitalegi.archi.diagram.dto.DiagramScope;

public interface VisibilityRule {

    boolean match(DiagramScope diagramScope, Element element);

    boolean match(DiagramScope diagramScope, Relation relation);

    VisibilityRuleType getRuleType();
}
