package it.vitalegi.archi.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CycleNotAllowedException extends Error {
    List<String> knownIds;
    List<UnresolvedDependency> unresolved;

    public CycleNotAllowedException(List<String> knownIds, List<UnresolvedDependency> unresolved) {
        super("Unresolved dependencies. Known: " + formatKnown(knownIds) + ". Unresolved: " + formatUnresolved(unresolved));
        this.knownIds = knownIds;
        this.unresolved = unresolved;
    }

    protected static String formatUnresolved(List<UnresolvedDependency> unresolved) {
        return unresolved.stream() //
                .map(e -> e.getId() + ": " + e.getDependencyId()) //
                .sorted() //
                .collect(Collectors.joining("; "));
    }

    protected static String formatKnown(List<String> knownIds) {
        return knownIds.stream().sorted(Comparator.nullsLast(String::compareTo)).collect(Collectors.joining(", "));
    }

    @Data
    @AllArgsConstructor
    public static class UnresolvedDependency {
        String id;
        String dependencyId;
    }
}
