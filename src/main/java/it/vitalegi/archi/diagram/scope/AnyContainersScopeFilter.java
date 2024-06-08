package it.vitalegi.archi.diagram.scope;

import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.util.WorkspaceUtil;
import it.vitalegi.archi.diagram.model.Diagram;

public class AnyContainersScopeFilter implements ScopeFilter {
    @Override
    public boolean accept(Diagram diagram, Element element) {
        return WorkspaceUtil.isContainer(element);
    }
}
