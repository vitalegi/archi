package it.vitalegi.archi.model.diagramelement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class C4DiagramRelation {
    String fromAlias;
    String toAlias;
    String label;
    String technology;
    String description;
    String sprite;
    List<String> tags;
    String link;
}