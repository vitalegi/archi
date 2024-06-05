package it.vitalegi.archi.diagram.scope;

import it.vitalegi.archi.util.StringUtil;

public class Scope {
    public static final String ALL = "*";

    public static boolean isScopeAll(String scope) {
        return StringUtil.isNullOrEmpty(scope) || ALL.equals(scope.trim());
    }
}
