package it.vitalegi.archi.model.diagram;

import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.model.diagram.Diagram;
import it.vitalegi.archi.visitor.DiagramVisitor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class SystemContextDiagram extends Diagram {
    String target;

    public SystemContextDiagram(Model model) {
        super(model);
    }

    @Override
    public <E> E visit(DiagramVisitor<E> visitor) {
        return visitor.visitSystemContextDiagram(this);
    }
}