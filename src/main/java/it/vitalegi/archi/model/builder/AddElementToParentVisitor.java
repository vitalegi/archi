package it.vitalegi.archi.model.builder;

import it.vitalegi.archi.exception.ElementNotAllowedException;
import it.vitalegi.archi.exception.NonUniqueIdException;
import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.element.Component;
import it.vitalegi.archi.model.element.Container;
import it.vitalegi.archi.model.element.ContainerInstance;
import it.vitalegi.archi.model.element.DeploymentEnvironment;
import it.vitalegi.archi.model.element.DeploymentNode;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.element.Group;
import it.vitalegi.archi.model.element.InfrastructureNode;
import it.vitalegi.archi.model.element.Person;
import it.vitalegi.archi.model.element.SoftwareSystem;
import it.vitalegi.archi.model.element.SoftwareSystemInstance;
import it.vitalegi.archi.util.WorkspaceUtil;
import it.vitalegi.archi.visitor.ElementVisitor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class AddElementToParentVisitor implements ElementVisitor<Void> {
    Workspace workspace;
    Element child;

    @Override
    public Void visitModel(Model parent) {
        if (addSoftwareSystem(parent, child)) {
            return null;
        }
        if (addGroup(parent, child)) {
            return null;
        }
        if (addPerson(parent, child)) {
            return null;
        }
        if (addDeploymentEnvironment(parent, child)) {
            return null;
        }
        throw new ElementNotAllowedException(parent, child);
    }

    @Override
    public Void visitComponent(Component parent) {
        throw new ElementNotAllowedException(parent, child);
    }

    @Override
    public Void visitContainer(Container parent) {
        if (addGroup(parent, child)) {
            return null;
        }
        if (addComponent(parent, child)) {
            return null;
        }
        throw new ElementNotAllowedException(parent, child);
    }

    @Override
    public Void visitContainerInstance(ContainerInstance parent) {
        throw new ElementNotAllowedException(parent, child);
    }

    @Override
    public Void visitDeploymentEnvironment(DeploymentEnvironment parent) {
        if (addGroup(parent, child)) {
            return null;
        }
        if (addDeploymentNode(parent, child)) {
            return null;
        }
        if (addInfrastructureNode(parent, child)) {
            return null;
        }
        throw new ElementNotAllowedException(parent, child);
    }

    @Override
    public Void visitDeploymentNode(DeploymentNode parent) {

        if (addGroup(parent, child)) {
            return null;
        }
        if (addInfrastructureNode(parent, child)) {
            return null;
        }
        if (addContainerInstancce(parent, child)) {
            return null;
        }
        if (addSoftwareSystemInstance(parent, child)) {
            return null;
        }
        if (addDeploymentNode(parent, child)) {
            return null;
        }
        throw new ElementNotAllowedException(parent, child);
    }

    @Override
    public Void visitGroup(Group parent) {
        var path = WorkspaceUtil.getPathFromRoot(parent);
        var isDescendantOfSoftwareSystem = path.stream().anyMatch(WorkspaceUtil::isSoftwareSystem);
        var isDescendantOfContainer = path.stream().anyMatch(WorkspaceUtil::isContainer);
        var isLikeRootElements = path.stream().allMatch(e -> WorkspaceUtil.isGroup(e) || WorkspaceUtil.isModel(e));
        if (isLikeRootElements && addSoftwareSystem(parent, child)) {
            return null;
        }
        if (addGroup(parent, child)) {
            return null;
        }
        if (isLikeRootElements && addPerson(parent, child)) {
            return null;
        }
        if (isDescendantOfSoftwareSystem && addContainer(parent, child)) {
            return null;
        }
        if (isDescendantOfContainer && addComponent(parent, child)) {
            return null;
        }
        throw new ElementNotAllowedException(parent, child);
    }

    @Override
    public Void visitInfrastructureNode(InfrastructureNode parent) {
        throw new ElementNotAllowedException(parent, child);
    }

    @Override
    public Void visitPerson(Person parent) {
        throw new ElementNotAllowedException(parent, child);
    }

    @Override
    public Void visitSoftwareSystem(SoftwareSystem parent) {
        if (addGroup(parent, child)) {
            return null;
        }
        if (addContainer(parent, child)) {
            return null;
        }
        throw new ElementNotAllowedException(parent, child);
    }

    @Override
    public Void visitSoftwareSystemInstance(SoftwareSystemInstance parent) {
        throw new ElementNotAllowedException(parent, child);
    }

    protected boolean addSoftwareSystem(Element parent, Element child) {
        if (WorkspaceUtil.isSoftwareSystem(child)) {
            addChild(parent, child);
            return true;
        }
        return false;
    }

    protected boolean addGroup(Element parent, Element child) {
        if (WorkspaceUtil.isGroup(child)) {
            addChild(parent, child);
            return true;
        }
        return false;
    }

    protected boolean addContainer(Element parent, Element child) {
        if (WorkspaceUtil.isContainer(child)) {
            addChild(parent, child);
            return true;
        }
        return false;
    }

    protected boolean addComponent(Element parent, Element child) {
        if (WorkspaceUtil.isComponent(child)) {
            addChild(parent, child);
            return true;
        }
        return false;
    }

    protected boolean addPerson(Element parent, Element child) {
        if (WorkspaceUtil.isPerson(child)) {
            addChild(parent, child);
            return true;
        }
        return false;
    }

    protected boolean addDeploymentEnvironment(Element parent, Element child) {
        if (WorkspaceUtil.isDeploymentEnvironment(child)) {
            addChild(parent, child);
            return true;
        }
        return false;
    }

    protected boolean addDeploymentNode(Element parent, Element child) {
        if (WorkspaceUtil.isDeploymentNode(child)) {
            addChild(parent, child);
            return true;
        }
        return false;
    }

    protected boolean addInfrastructureNode(Element parent, Element child) {
        if (WorkspaceUtil.isInfrastructureNode(child)) {
            addChild(parent, child);
            return true;
        }
        return false;
    }

    protected boolean addContainerInstancce(Element parent, Element child) {
        if (WorkspaceUtil.isContainerInstance(child)) {
            addChild(parent, child);
            return true;
        }
        return false;
    }

    protected boolean addSoftwareSystemInstance(Element parent, Element child) {
        if (WorkspaceUtil.isSoftwareSystemInstance(child)) {
            addChild(parent, child);
            return true;
        }
        return false;
    }


    protected void addChild(Element parent, Element child) {
        log.debug("Add {} to {}", child.toShortString(), parent.toShortString());
        if (child.getId() != null && workspace.getModel().getElementMap().containsKey(child.getId())) {
            throw new NonUniqueIdException(child.getId());
        }
        parent.getElements().add(child);
        child.setParent(parent);
        workspace.getModel().getElementMap().put(child.getId(), child);
    }
}
