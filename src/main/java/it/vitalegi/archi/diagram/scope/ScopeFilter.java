package it.vitalegi.archi.diagram.scope;

import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.diagram.Diagram;

public interface ScopeFilter {

    boolean accept(Diagram diagram, Element element);
}
