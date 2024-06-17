package it.vitalegi.archi.workspaceloader.model;

import it.vitalegi.archi.model.element.PropertyEntries;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    PropertyEntries properties;

    @Builder
    public RelationRaw(String id, String from, String to, String description, String label, String sprite, String link, List<String> technologies, List<String> tags, PropertyEntries properties) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.description = description;
        this.label = label;
        this.sprite = sprite;
        this.link = link;
        this.technologies = technologies;
        this.tags = tags;
        this.properties = properties;
    }
}
