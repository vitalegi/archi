package it.vitalegi.archi.model;

import it.vitalegi.archi.visitor.ElementVisitor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeploymentNode extends Element {

    public DeploymentNode(Model model) {
        super(model);
    }

    public ElementType getElementType() {
        return ElementType.DEPLOYMENT_NODE;
    }

    @Override
    public <E> E visit(ElementVisitor<E> visitor) {
        return visitor.visitDeploymentNode(this);
    }
}
