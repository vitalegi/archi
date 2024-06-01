package it.vitalegi.archi.model;

import it.vitalegi.archi.exception.ElementNotAllowedException;
import lombok.ToString;

@ToString(callSuper = true)
public class Person extends Element {

    public Person(Model model) {
        super(model);
    }

    public void addChild(Element child) {
        throw new ElementNotAllowedException(this, child);
    }

    public ElementType getElementType() {
        return ElementType.PERSON;
    }
}
