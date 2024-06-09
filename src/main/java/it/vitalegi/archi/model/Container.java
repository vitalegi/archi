package it.vitalegi.archi.model;

import it.vitalegi.archi.util.WorkspaceUtil;
import it.vitalegi.archi.visitor.ElementVisitor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Container extends Element {
    public Container(Model model) {
        super(model);
    }

    public ElementType getElementType() {
        return ElementType.CONTAINER;
    }

    public SoftwareSystem getSoftwareSystem() {
        var curr = getParent();
        while (!WorkspaceUtil.isSoftwareSystem(curr)) {
            curr = curr.getParent();
        }
        return (SoftwareSystem) curr;
    }
    @Override
    public <E> E visit(ElementVisitor<E> visitor) {
        return visitor.visitContainer(this);
    }
}
