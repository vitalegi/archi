package it.vitalegi.archi.model.element;

import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.visitor.ElementVisitor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeploymentEnvironment extends Element {

    public DeploymentEnvironment(Model model) {
        super(model);
    }

    public ElementType getElementType() {
        return ElementType.DEPLOYMENT_ENVIRONMENT;
    }

    @Override
    public <E> E visit(ElementVisitor<E> visitor) {
        return visitor.visitDeploymentEnvironment(this);
    }
}
