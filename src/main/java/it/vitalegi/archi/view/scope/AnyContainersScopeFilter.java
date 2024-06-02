package it.vitalegi.archi.view.scope;

import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.util.WorkspaceUtil;
import it.vitalegi.archi.view.dto.View;

public class AnyContainersScopeFilter implements ScopeFilter {
    @Override
    public boolean accept(View view, Element element) {
        return WorkspaceUtil.isContainer(element);
    }
}
