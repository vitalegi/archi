package it.vitalegi.archi.model;

import it.vitalegi.archi.exception.ElementNotAllowedException;
import it.vitalegi.archi.exception.NonUniqueIdException;
import it.vitalegi.archi.util.WorkspaceUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Model extends Node {

    Map<String, Element> elementMap;
    @Getter
    Relations relations;

    public Model() {
        super(null);
        model = this;
        elementMap = new HashMap<>();
        relations = new Relations();
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
        if (addDeploymentEnvironment(this, child)) {
            return;
        }
        throw new ElementNotAllowedException(this, child);
    }

    public void addRelation(Relation relation) {
        relations.addRelation(relation);
    }

    public Element getElementById(String id) {
        return elementMap.get(id);
    }

    public List<Element> getAllElements() {
        return new ArrayList<>(elementMap.values());
    }

    protected boolean addSoftwareSystem(Node parent, Element child) {
        if (WorkspaceUtil.isSoftwareSystem(child)) {
            addChild(parent, child);
            return true;
        }
        return false;
    }

    protected boolean addGroup(Node parent, Element child) {
        if (WorkspaceUtil.isGroup(child)) {
            addChild(parent, child);
            return true;
        }
        return false;
    }

    protected boolean addContainer(Node parent, Element child) {
        if (WorkspaceUtil.isContainer(child)) {
            addChild(parent, child);
            return true;
        }
        return false;
    }

    protected boolean addPerson(Node parent, Element child) {
        if (WorkspaceUtil.isPerson(child)) {
            addChild(parent, child);
            return true;
        }
        return false;
    }

    protected boolean addDeploymentEnvironment(Node parent, Element child) {
        if (WorkspaceUtil.isDeploymentEnvironment(child)) {
            addChild(parent, child);
            return true;
        }
        return false;
    }

    protected boolean addDeploymentNode(Node parent, Element child) {
        if (WorkspaceUtil.isDeploymentNode(child)) {
            addChild(parent, child);
            return true;
        }
        return false;
    }

    protected boolean addInfrastructureNode(Node parent, Element child) {
        if (WorkspaceUtil.isInfrastructureNode(child)) {
            addChild(parent, child);
            return true;
        }
        return false;
    }

    protected boolean addContainerInstancce(Node parent, Element child) {
        if (WorkspaceUtil.isContainerInstance(child)) {
            addChild(parent, child);
            return true;
        }
        return false;
    }

    protected boolean addSoftwareSystemInstance(Node parent, Element child) {
        if (WorkspaceUtil.isSoftwareSystemInstance(child)) {
            addChild(parent, child);
            return true;
        }
        return false;
    }

    protected void addChild(Node parent, Element child) {
        log.debug("Add {} to {}", child.toShortString(), parent.toShortString());
        if (child.getId() != null && elementMap.containsKey(child.getId())) {
            throw new NonUniqueIdException(child.getId());
        }
        parent.getElements().add(child);
        child.setParent(parent);
        elementMap.put(child.getId(), child);
    }

    public String toShortString() {
        return getClass().getSimpleName();
    }

    @Override
    public void validate() {
        getAllElements().forEach(Element::validate);
    }

    public ElementType getElementType() {
        return null;
    }
}
