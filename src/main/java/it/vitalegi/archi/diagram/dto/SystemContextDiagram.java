package it.vitalegi.archi.diagram.dto;

import it.vitalegi.archi.model.Model;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SystemContextDiagram extends Diagram {
    public SystemContextDiagram(Model model) {
        super(model);
    }
}