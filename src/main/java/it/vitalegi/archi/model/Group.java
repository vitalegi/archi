package it.vitalegi.archi.model;

import it.vitalegi.archi.visitor.ElementVisitor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Group extends Element {

    public Group(Model model) {
        super(model);
    }

    public ElementType getElementType() {
        return ElementType.GROUP;
    }

    @Override
    public <E> E visit(ElementVisitor<E> visitor) {
        return visitor.visitGroup(this);
    }
}
