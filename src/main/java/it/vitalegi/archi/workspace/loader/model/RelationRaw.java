package it.vitalegi.archi.workspace.loader.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RelationRaw {
    String from;
    String to;
    String description;
    List<String> tags;
    Map<String, String> metadata;
}
