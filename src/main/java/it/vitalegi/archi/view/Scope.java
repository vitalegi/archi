package it.vitalegi.archi.view;

import it.vitalegi.archi.view.dto.View;

import java.util.List;
import java.util.stream.Collectors;

public class Scope {
    public static final String ALL = "*";
    List<ScopeLevel> validScopes;

    public Scope(List<ScopeLevel> validScopes) {
        this.validScopes = validScopes;
    }

    public void validate(View view, String scope) {
        var acceptable = validScopes.stream().filter(valid -> valid.accept(view, scope)).collect(Collectors.toList());
        if (acceptable.isEmpty()) {
            throw new RuntimeException("Scope " + scope + " on view " + view.getName() + " is invalid. Expected one of: " + validScopes+". Check if all objects exist.");
        }
    }
}
