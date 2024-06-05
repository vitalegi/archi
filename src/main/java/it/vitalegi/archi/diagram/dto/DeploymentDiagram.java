package it.vitalegi.archi.diagram.dto;

import it.vitalegi.archi.model.Model;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeploymentDiagram extends Diagram {
    String scope;
    String environment;

    public DeploymentDiagram(Model model) {
        super(model);
    }
}