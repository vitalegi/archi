package it.vitalegi.archi.util;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.AssertionFailureBuilder.assertionFailure;

public class AssertionUtil {
    public static void assertArrayEqualsUnsorted(List<?> expected, List<?> actual) {
        if (expected.size() != actual.size()) {
            fail(expected, actual, "Size is different");
        }
        var mismatches = expected.stream().filter(e -> actual.stream().noneMatch(e::equals)).collect(Collectors.toList());
        if (!mismatches.isEmpty()) {
            fail(expected, actual, "Mismatches: " + mismatches);
        }
    }

    private static void fail(Object expected, Object actual, String message) {
        assertionFailure() //
                .message(message) //
                .expected(expected) //
                .actual(actual) //
                .buildAndThrow();
    }
}
