package it.vitalegi.archi.workspaceloader.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FlowRaw {
    String id;
    String name;
    List<FlowStepRaw> steps;

    @Builder
    public FlowRaw(String id, String name, List<FlowStepRaw> steps) {
        this.id = id;
        this.name = name;
        this.steps = steps;
    }
}
