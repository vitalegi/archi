package it.vitalegi.archi.diagram.dto;

import it.vitalegi.archi.model.Model;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LandscapeDiagram extends Diagram {
    public LandscapeDiagram(Model model) {
        super(model);
    }
}