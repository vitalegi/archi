package it.vitalegi.archi.model.flow;

import it.vitalegi.archi.model.Model;
import lombok.Data;

import java.util.List;

@Data
public class Flow {
    Model model;
    String id;
    String name;
    List<FlowStep> steps;

    public Flow(Model model) {
        this.model = model;
    }
}
