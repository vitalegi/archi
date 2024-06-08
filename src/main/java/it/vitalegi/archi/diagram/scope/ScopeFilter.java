package it.vitalegi.archi.diagram.scope;

import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.diagram.model.Diagram;

public interface ScopeFilter {

    boolean accept(Diagram diagram, Element element);
}
