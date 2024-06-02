package it.vitalegi.archi.view.scope;

import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.view.dto.View;

public interface ScopeFilter {

    boolean accept(View view, Element element);
}
