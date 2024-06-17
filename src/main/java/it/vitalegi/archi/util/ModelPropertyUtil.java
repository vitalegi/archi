package it.vitalegi.archi.util;

import it.vitalegi.archi.model.diagramelement.C4DiagramElementProperty;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModelPropertyUtil {
    public static List<C4DiagramElementProperty> properties(Map<String, String> properties) {
        if (properties == null) {
            return Collections.emptyList();
        }
        return properties.entrySet().stream().map(e -> new C4DiagramElementProperty(e.getKey(), e.getValue())).collect(Collectors.toList());
    }
}
