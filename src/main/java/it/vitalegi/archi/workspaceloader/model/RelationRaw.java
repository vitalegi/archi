package it.vitalegi.archi.workspaceloader.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RelationRaw {
    String id;
    String from;
    String to;
    String description;
    List<String> tags;
    Map<String, String> metadata;
}
