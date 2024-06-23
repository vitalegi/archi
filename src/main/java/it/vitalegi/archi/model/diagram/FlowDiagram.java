package it.vitalegi.archi.model.diagram;

import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.visitor.DiagramVisitor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FlowDiagram extends Diagram {

    String flow;

    public FlowDiagram(Model model) {
        super(model);
    }

    @Override
    public <E> E visit(DiagramVisitor<E> visitor) {
        return visitor.visitFlowDiagram(this);
    }
}