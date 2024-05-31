package it.vitalegi.archi.model;

import it.vitalegi.archi.exception.ElementNotAllowedException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SoftwareSystem extends Node {

    public SoftwareSystem(Model model) {
        super(model);
    }

    public void addChild(Element child) {
        if (getModel().addGroup(this, child)) {
            return;
        }
        if (getModel().addContainer(this, child)) {
            return;
        }
        throw new ElementNotAllowedException(this, child);
    }
}
