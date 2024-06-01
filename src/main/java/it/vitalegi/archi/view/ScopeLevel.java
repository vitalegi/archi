package it.vitalegi.archi.view;

import it.vitalegi.archi.util.StringUtil;
import it.vitalegi.archi.util.WorkspaceUtil;
import it.vitalegi.archi.view.dto.View;

public enum ScopeLevel {
    ALL {
        @Override
        public boolean accept(View view, String scope) {
            if (StringUtil.isNullOrEmpty(scope)) {
                return true;
            }
            return scope.equals(Scope.ALL);
        }
    }, SOFTWARE_SYSTEM {
        @Override
        public boolean accept(View view, String scope) {
            if (StringUtil.isNullOrEmpty(scope)) {
                return false;
            }
            var softwareSystem = WorkspaceUtil.findSoftwareSystem(view.getModel().getAllElements(), scope);
            return softwareSystem != null;
        }
    }, CONTAINER {
        @Override
        public boolean accept(View view, String scope) {
            if (StringUtil.isNullOrEmpty(scope)) {
                return false;
            }
            var container = WorkspaceUtil.findContainer(view.getModel().getAllElements(), scope);
            return container != null;
        }
    };

    public abstract boolean accept(View view, String scope);
}