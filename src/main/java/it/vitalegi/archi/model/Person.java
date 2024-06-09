package it.vitalegi.archi.model;

import it.vitalegi.archi.visitor.ElementVisitor;
import lombok.ToString;

@ToString(callSuper = true)
public class Person extends Element {

    public Person(Model model) {
        super(model);
    }


    public ElementType getElementType() {
        return ElementType.PERSON;
    }

    @Override
    public <E> E visit(ElementVisitor<E> visitor) {
        return visitor.visitPerson(this);
    }
}
