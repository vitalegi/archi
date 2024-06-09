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
public class DeploymentDiagram extends Diagram {
    String scope;
    String environment;

    public DeploymentDiagram(Model model) {
        super(model);
    }

    @Override
    public <E> E visit(DiagramVisitor<E> visitor) {
        return visitor.visitDeploymentDiagram(this);
    }
}