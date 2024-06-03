package it.vitalegi.archi.model;

import it.vitalegi.archi.exception.ElementNotAllowedException;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InfrastructureNode extends Element {

    public InfrastructureNode(Model model) {
        super(model);
    }

    public void addChild(Element child) {
        throw new ElementNotAllowedException(this, child);
    }

    public ElementType getElementType() {
        return ElementType.INFRASTRUCTURE_NODE;
    }
}
