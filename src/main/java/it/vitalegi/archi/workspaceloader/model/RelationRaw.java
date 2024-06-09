package it.vitalegi.archi.workspaceloader.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@Data
public class RelationRaw {
    String id;
    String from;
    String to;
    String description;
    String label;
    String sprite;
    String link;
    String technologies;
    List<String> tags;
    Map<String, String> metadata;
}
