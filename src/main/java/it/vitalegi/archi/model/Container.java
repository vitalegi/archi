package it.vitalegi.archi.model;

import it.vitalegi.archi.exception.ElementNotAllowedException;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Container extends Node {
    public Container(Model model) {
        super(model);
    }

    public void addChild(Element child) {
        throw new ElementNotAllowedException(this, child);
    }

    public ElementType getElementType() {
        return ElementType.CONTAINER;
    }
}
