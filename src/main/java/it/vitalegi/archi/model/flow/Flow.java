package it.vitalegi.archi.model.flow;

import it.vitalegi.archi.model.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
public class Flow {
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Model model;
    String id;
    String name;
    List<FlowStep> steps;

    public Flow(Model model) {
        this.model = model;
    }
}
