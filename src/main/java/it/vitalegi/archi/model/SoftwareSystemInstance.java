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
public class SoftwareSystemInstance extends Element {

    String softwareSystemId;

    public SoftwareSystemInstance(Model model, String softwareSystemId) {
        super(model);
        this.softwareSystemId = softwareSystemId;
    }

    public void addChild(Element child) {
        throw new ElementNotAllowedException(this, child);
    }

    public void validate() {
        if (StringUtil.isNullOrEmpty(softwareSystemId)) {
            throw new IllegalArgumentException("softwareSystemId is missing on " + toShortString());
        }
        var softwareSystem = model.getElementById(softwareSystemId);
        if (softwareSystem == null) {
            throw new NoSuchElementException("SoftwareSystem " + softwareSystemId + " doesn't exist. Dependency is unsatisfied for " + toShortString());
        }
        if (!WorkspaceUtil.isSoftwareSystem(softwareSystem)) {
            throw new IllegalArgumentException("Dependency is unsatisfied for " + this.toShortString() + ". Expected a SoftwareSystem; Actual: " + softwareSystem.toShortString());
        }
    }

    public ElementType getElementType() {
        return ElementType.SOFTWARE_SYSTEM_INSTANCE;
    }
}
