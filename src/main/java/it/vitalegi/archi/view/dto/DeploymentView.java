package it.vitalegi.archi.view.dto;

import it.vitalegi.archi.model.Model;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeploymentView extends View {
    String scope;
    String environment;

    public DeploymentView(Model model) {
        super(model);
    }
}