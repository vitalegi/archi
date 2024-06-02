package it.vitalegi.archi.model;

import it.vitalegi.archi.exception.ElementNotAllowedException;
import it.vitalegi.archi.util.StringUtil;
import it.vitalegi.archi.util.WorkspaceUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.NoSuchElementException;

@Slf4j
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContainerInstance extends Element {

    String containerId;

    public ContainerInstance(Model model, String containerId) {
        super(model);
        this.containerId = containerId;
    }

    public void addChild(Element child) {
        throw new ElementNotAllowedException(this, child);
    }

    public void validate() {
        if (StringUtil.isNullOrEmpty(containerId)) {
            throw new IllegalArgumentException("containerId is missing on " + toShortString());
        }
        var container = model.getElementById(containerId);
        if (container == null) {
            throw new NoSuchElementException("Container " + containerId + " doesn't exist. Dependency is unsatisfied for " + toShortString());
        }
        if (!WorkspaceUtil.isContainer(container)) {
            throw new IllegalArgumentException("Dependency is unsatisfied for " + this.toShortString() + ". Expected a Container; Actual: " + container.toShortString());
        }
    }

    public ElementType getElementType() {
        return ElementType.CONTAINER_INSTANCE;
    }
}
