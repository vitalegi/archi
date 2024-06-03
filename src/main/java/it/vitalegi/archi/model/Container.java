package it.vitalegi.archi.model;

import it.vitalegi.archi.exception.ElementNotAllowedException;
import it.vitalegi.archi.util.WorkspaceUtil;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Container extends Element {
    public Container(Model model) {
        super(model);
    }

    public void addChild(Element child) {
        throw new ElementNotAllowedException(this, child);
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
}
