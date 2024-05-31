package it.vitalegi.archi.model;

import it.vitalegi.archi.exception.ElementNotAllowedException;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Model extends Node {

    Map<String, Element> elementIds;

    public Model() {
        super(null);
        model = this;
        elementIds = new HashMap<>();
    }

    public void addChild(Element child) {
        if (addSoftwareSystem(this, child)) {
            return;
        }
        if (addGroup(this, child)) {
            return;
        }
        if (addPerson(this, child)) {
            return;
        }
        throw new ElementNotAllowedException(this, child);
    }

    public Element getElementById(String id) {
        return elementIds.get(id);
    }

    protected boolean addSoftwareSystem(Node parent, Element child) {
        if (child instanceof SoftwareSystem) {
            addChild(parent, child);
            return true;
        }
        return false;
    }

    protected boolean addGroup(Node parent, Element child) {
        if (child instanceof Group) {
            addChild(parent, child);
            return true;
        }
        return false;
    }

    protected boolean addContainer(Node parent, Element child) {
        if (child instanceof Container) {
            addChild(parent, child);
            return true;
        }
        return false;
    }

    protected boolean addPerson(Node parent, Element child) {
        if (child instanceof Person) {
            addChild(parent, child);
            return true;
        }
        return false;
    }

    protected void addChild(Node parent, Element child) {
        log.info("Add {} ({}) to {} ({})", child.getId(), child.getClass().getSimpleName(), parent.getId(), parent.getClass().getSimpleName());
        parent.getElements().add(child);
        child.setParent(parent);
        elementIds.put(child.getId(), child);
    }

    public String toShortString() {
        return getClass().getSimpleName();
    }
}
