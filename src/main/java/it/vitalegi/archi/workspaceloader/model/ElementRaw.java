package it.vitalegi.archi.workspaceloader.model;

import it.vitalegi.archi.model.element.ElementType;
import it.vitalegi.archi.model.element.PropertyEntries;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElementRaw {
    ElementType type;
    String parentId;
    String id;
    String name;
    String description;
    List<String> tags;
    List<String> technologies;
    PropertyEntries properties;
    String containerId;
    String softwareSystemId;
    List<ElementRaw> elements;

    public static ElementRaw.ElementRawBuilder softwareSystem() {
        return builder().type(ElementType.SOFTWARE_SYSTEM);
    }

    public static ElementRaw.ElementRawBuilder container() {
        return builder().type(ElementType.CONTAINER);
    }
}
