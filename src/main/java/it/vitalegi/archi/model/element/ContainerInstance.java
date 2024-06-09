package it.vitalegi.archi.model.element;

import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.util.StringUtil;
import it.vitalegi.archi.util.WorkspaceUtil;
import it.vitalegi.archi.visitor.ElementVisitor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

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

    @Override
    public Stream<Element> getAllChildren() {
        return Stream.of(getContainer());
    }

    public void validate() {
        if (StringUtil.isNullOrEmpty(containerId)) {
            throw new IllegalArgumentException("containerId is missing on " + toShortString());
        }
        getContainer();
    }

    public Container getContainer() {
        var container = model.getElementById(containerId);
        if (container == null) {
            throw new NoSuchElementException("Container " + containerId + " doesn't exist. Dependency is unsatisfied for " + toShortString());
        }
        if (!WorkspaceUtil.isContainer(container)) {
            throw new IllegalArgumentException("Dependency is unsatisfied for " + this.toShortString() + ". Expected a Container; Actual: " + container.toShortString());
        }
        return (Container) container;
    }

    public ElementType getElementType() {
        return ElementType.CONTAINER_INSTANCE;
    }

    @Override
    public <E> E visit(ElementVisitor<E> visitor) {
        return visitor.visitContainerInstance(this);
    }
}
