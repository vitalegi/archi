package it.vitalegi.archi.workspace.loader.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ElementYaml {
    ElementType type;
    String parentId;
    String id;
    String name;
    String description;
    List<String> tags;
    Map<String, String> metadata;
    String containerId;
}
