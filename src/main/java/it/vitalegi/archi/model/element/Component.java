package it.vitalegi.archi.model.element;

import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.util.WorkspaceUtil;
import it.vitalegi.archi.visitor.ElementVisitor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Component extends Element {
    public Component(Model model) {
        super(model);
    }

    public ElementType getElementType() {
        return ElementType.COMPONENT;
    }

    public Container getContainer() {
        var curr = getParent();
        while (!WorkspaceUtil.isContainer(curr)) {
            curr = curr.getParent();
        }
        return (Container) curr;
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
        return visitor.visitComponent(this);
    }
}
