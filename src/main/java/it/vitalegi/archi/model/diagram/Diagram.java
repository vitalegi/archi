package it.vitalegi.archi.model.diagram;

import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.model.style.Style;
import it.vitalegi.archi.visitor.DiagramVisitor;
import lombok.Data;

@Data
public abstract class Diagram {
    String name;
    String title;
    Style style;
    Model model;

    public Diagram(Model model) {
        this.model = model;
    }

    public abstract <E> E visit(DiagramVisitor<E> visitor);
}