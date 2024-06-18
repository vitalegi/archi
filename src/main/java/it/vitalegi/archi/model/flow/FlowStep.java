package it.vitalegi.archi.model.flow;

import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.model.relation.DirectRelation;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FlowStep extends DirectRelation {
    public FlowStep(Model model) {
        super(model);
    }
}
