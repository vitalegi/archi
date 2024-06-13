package it.vitalegi.archi.exporter.plantuml.builder;

import it.vitalegi.archi.model.element.Element;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class AliasGenerator {
    Map<Element, List<String>> elements;
    Map<String, Element> aliases;

    public AliasGenerator() {
        elements = new HashMap<>();
        aliases = new HashMap<>();
    }

    public String generateAlias(Element element) {
        var baseAlias = baseAlias(element);
        if (!exists(baseAlias)) {
            addAlias(baseAlias, element);
            return baseAlias;
        }
        int i = 1;
        var alias = incrementalAlias(baseAlias, i);
        while (exists(alias)) {
            alias = incrementalAlias(baseAlias, ++i);
        }
        addAlias(alias, element);
        return alias;
    }

    public List<String> getConnectedAliases(Element element) {
        var aliases = elements.get(element);
        if (aliases == null || aliases.isEmpty()) {
            throw new IllegalArgumentException("No alias found for element " + element.toShortString());
        }
        return aliases;
    }

    private boolean exists(String alias) {
        return aliases.containsKey(alias);
    }

    private String baseAlias(Element element) {
        return element.getUniqueId();
    }

    private String incrementalAlias(String baseAlias, int counter) {
        return baseAlias + "_" + counter;
    }

    private void addAlias(String alias, Element element) {
        aliases.put(alias, element);
        if (!elements.containsKey(element)) {
            elements.put(element, new ArrayList<>());
        }
        elements.get(element).add(alias);
    }
}
