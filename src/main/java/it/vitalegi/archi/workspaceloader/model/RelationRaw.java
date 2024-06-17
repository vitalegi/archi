package it.vitalegi.archi.workspaceloader.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Data
public class RelationRaw {
    String id;
    String from;
    String to;
    String description;
    String label;
    String sprite;
    String link;
    List<String> technologies;
    List<String> tags;
    Map<String, String> metadata;

    @Builder
    public RelationRaw(String id, String from, String to, String description, String label, String sprite, String link, List<String> technologies, List<String> tags, Map<String, String> metadata) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.description = description;
        this.label = label;
        this.sprite = sprite;
        this.link = link;
        this.technologies = technologies;
        this.tags = tags;
        this.metadata = metadata;
    }
}
