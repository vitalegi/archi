package it.vitalegi.archi.model.diagramelement;

import it.vitalegi.archi.util.StringUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class C4DiagramModel {
    private Map<String, C4DiagramElement> elementMap;
    @Getter
    private List<C4DiagramElement> topLevelElements;
    @Getter
    private List<C4DiagramRelation> relations;

    public C4DiagramModel() {
        elementMap = new HashMap<>();
        topLevelElements = new ArrayList<>();
        relations = new ArrayList<>();
    }

    public void addElement(String parentId, C4DiagramElement element) {
        var id = element.getId();
        if (StringUtil.isNullOrEmpty(id)) {
            throw new IllegalArgumentException("ID is mandatory, error on " + element);
        }
        if (elementMap.containsKey(id)) {
            throw new IllegalArgumentException("ID " + id + " already in use");
        }
        if (parentId != null) {
            var parent = elementMap.get(parentId);
            if (parent == null) {
                throw new IllegalArgumentException("Parent " + id + " doesn't exist");
            }
            parent.addChild(element);
        } else {
            topLevelElements.add(element);
        }
        elementMap.put(id, element);
    }

    public void addRelation(C4DiagramRelation relation) {
        relations.add(relation);
    }

}
