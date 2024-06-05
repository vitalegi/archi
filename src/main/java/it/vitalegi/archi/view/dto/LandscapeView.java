package it.vitalegi.archi.view.dto;

import it.vitalegi.archi.model.Model;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LandscapeView extends View {
    public LandscapeView(Model model) {
        super(model);
    }
}