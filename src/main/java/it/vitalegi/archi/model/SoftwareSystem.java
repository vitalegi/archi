package it.vitalegi.archi.model;

import it.vitalegi.archi.visitor.ElementVisitor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SoftwareSystem extends Element {

    public SoftwareSystem(Model model) {
        super(model);
    }

    public ElementType getElementType() {
        return ElementType.SOFTWARE_SYSTEM;
    }

    @Override
    public <E> E visit(ElementVisitor<E> visitor) {
        return visitor.visitSoftwareSystem(this);
    }
}
