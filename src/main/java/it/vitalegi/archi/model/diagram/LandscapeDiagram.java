package it.vitalegi.archi.model.diagram;

import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.model.diagram.Diagram;
import it.vitalegi.archi.visitor.DiagramVisitor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LandscapeDiagram extends Diagram {
    public LandscapeDiagram(Model model) {
        super(model);
    }
    @Override
    public <E> E visit(DiagramVisitor<E> visitor) {
        return visitor.visitLandscapeDiagram(this);
    }
}