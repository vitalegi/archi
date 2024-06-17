package it.vitalegi.archi.util;

import it.vitalegi.archi.model.diagramelement.C4DiagramElementProperty;
import it.vitalegi.archi.model.element.PropertyEntries;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ModelPropertyUtil {
    public static List<C4DiagramElementProperty> properties(PropertyEntries properties) {
        if (properties == null) {
            return Collections.emptyList();
        }
        return properties.getProperties().stream().map(e -> new C4DiagramElementProperty(e.getKey(), e.getValue())).collect(Collectors.toList());
    }
}
