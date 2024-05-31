package it.vitalegi.archi.model;

import it.vitalegi.archi.exception.ElementNotAllowedException;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeploymentNode extends Node {

    public DeploymentNode(Model model) {
        super(model);
    }

    public void addChild(Element child) {
        if (getModel().addGroup(this, child)) {
            return;
        }
        if (getModel().addContainerInstancce(this, child)) {
            return;
        }
        throw new ElementNotAllowedException(this, child);
    }
}
