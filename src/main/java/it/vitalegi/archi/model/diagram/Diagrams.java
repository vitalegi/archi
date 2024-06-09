package it.vitalegi.archi.model.diagram;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ToString
public class Diagrams {
    List<Diagram> diagrams;

    public Diagrams() {
        diagrams = new ArrayList<>();
    }

    public List<Diagram> getAll() {
        return diagrams;
    }

    public void add(Diagram diagram) {
        diagrams.add(diagram);
    }

    public Diagram getByName(String name) {
        return diagrams.stream().filter(v -> Objects.equals(v.getName(), name)).findFirst().orElse(null);
    }
}
