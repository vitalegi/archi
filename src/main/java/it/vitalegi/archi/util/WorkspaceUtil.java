package it.vitalegi.archi.util;

import it.vitalegi.archi.model.Entity;
import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.model.element.Component;
import it.vitalegi.archi.model.element.Container;
import it.vitalegi.archi.model.element.ContainerInstance;
import it.vitalegi.archi.model.element.DeploymentEnvironment;
import it.vitalegi.archi.model.element.DeploymentNode;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.element.ElementType;
import it.vitalegi.archi.model.element.Group;
import it.vitalegi.archi.model.element.InfrastructureNode;
import it.vitalegi.archi.model.element.Person;
import it.vitalegi.archi.model.element.SoftwareSystem;
import it.vitalegi.archi.model.element.SoftwareSystemInstance;
import it.vitalegi.archi.model.relation.DirectRelation;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

public class WorkspaceUtil {
    public static String createUniqueId(Entity entity) {
        if (entity == null) {
            throw new NullPointerException("Element is null");
        }
        var id = entity.getId();
        if (StringUtil.isNotNullOrEmpty(id)) {
            return entity.getClass().getSimpleName() + "_" + entity.getId();
        }
        return entity.getClass().getSimpleName() + "_auto_" + UUID.randomUUID();
    }

    public static Person getPerson(List<Element> elements, String id) {
        var element = findPerson(elements, id);
        if (element == null) {
            throw new NoSuchElementException("No element with id " + id + ". Available IDs: " + Entity.collectIds(elements));
        }
        return element;
    }

    public static Person findPerson(List<? extends Element> elements, String id) {
        var element = findElementById(elements, id);
        if (element == null) {
            return null;
        }
        if (isPerson(element)) {
            return (Person) element;
        }
        throw new IllegalArgumentException("Element with id " + id + " is not a person: " + element);
    }

    public static List<Person> getPeople(List<? extends Element> elements) {
        return elements.stream().filter(WorkspaceUtil::isPerson).map(e -> ((Person) e)).collect(Collectors.toList());
    }

    public static SoftwareSystem getSoftwareSystem(List<? extends Element> elements, String id) {
        var element = findSoftwareSystem(elements, id);
        if (element == null) {
            throw new NoSuchElementException("No element with id " + id + ". Available IDs: " + Entity.collectIds(elements));
        }
        return element;
    }

    public static SoftwareSystem findSoftwareSystem(List<? extends Element> elements, String id) {
        var element = findElementById(elements, id);
        if (element == null) {
            return null;
        }
        if (isSoftwareSystem(element)) {
            return (SoftwareSystem) element;
        }
        throw new IllegalArgumentException("Element with id " + id + " is not a SoftwareSystem: " + element);
    }

    public static List<SoftwareSystem> getSoftwareSystems(List<? extends Element> elements) {
        return elements.stream().filter(WorkspaceUtil::isSoftwareSystem).map(e -> ((SoftwareSystem) e)).collect(Collectors.toList());
    }

    public static Container getContainer(List<? extends Element> elements, String id) {
        var element = findContainer(elements, id);
        if (element == null) {
            throw new NoSuchElementException("No element with id " + id + ". Available IDs: " + Entity.collectIds(elements));
        }
        return element;
    }

    public static Container findContainer(List<? extends Element> elements, String id) {
        var element = findElementById(elements, id);
        if (element == null) {
            return null;
        }
        if (isContainer(element)) {
            return (Container) element;
        }
        throw new IllegalArgumentException("Element with id " + id + " is not a Container: " + element);
    }


    public static List<Container> getContainers(List<? extends Element> elements) {
        return elements.stream().filter(WorkspaceUtil::isContainer).map(e -> ((Container) e)).collect(Collectors.toList());
    }

    public static List<Component> getComponents(List<? extends Element> elements) {
        return elements.stream().filter(WorkspaceUtil::isComponent).map(e -> ((Component) e)).collect(Collectors.toList());
    }


    public static Component getComponent(List<? extends Element> elements, String id) {
        var element = findComponent(elements, id);
        if (element == null) {
            throw new NoSuchElementException("No element with id " + id + ". Available IDs: " + Entity.collectIds(elements));
        }
        return element;
    }

    public static Component findComponent(List<? extends Element> elements, String id) {
        var element = findElementById(elements, id);
        if (element == null) {
            return null;
        }
        if (isComponent(element)) {
            return (Component) element;
        }
        throw new IllegalArgumentException("Element with id " + id + " is not a Component: " + element);
    }

    public static Group getGroup(List<? extends Element> elements, String id) {
        var element = findGroup(elements, id);
        if (element == null) {
            throw new NoSuchElementException("No element with id " + id + ". Available IDs: " + Entity.collectIds(elements));
        }
        return element;
    }

    public static Group findGroup(List<? extends Element> elements, String id) {
        var element = findElementById(elements, id);
        if (element == null) {
            return null;
        }
        if (isGroup(element)) {
            return (Group) element;
        }
        throw new IllegalArgumentException("Element with id " + id + " is not a Group: " + element);
    }


    public static DeploymentEnvironment getDeploymentEnvironment(List<? extends Element> elements, String id) {
        var element = findDeploymentEnvironment(elements, id);
        if (element == null) {
            throw new NoSuchElementException("No element with id " + id + ". Available IDs: " + Entity.collectIds(elements));
        }
        return element;
    }

    public static DeploymentEnvironment findDeploymentEnvironment(List<? extends Element> elements, String id) {
        var element = findElementById(elements, id);
        if (element == null) {
            return null;
        }
        if (isDeploymentEnvironment(element)) {
            return (DeploymentEnvironment) element;
        }
        throw new IllegalArgumentException("Element with id " + id + " is not a DeploymentEnvironment: " + element);
    }


    public static DeploymentNode getDeploymentNode(List<? extends Element> elements, String id) {
        var element = findDeploymentNode(elements, id);
        if (element == null) {
            throw new NoSuchElementException("No element with id " + id + ". Available IDs: " + Entity.collectIds(elements));
        }
        return element;
    }

    public static DeploymentNode findDeploymentNode(List<? extends Element> elements, String id) {
        var element = findElementById(elements, id);
        if (element == null) {
            return null;
        }
        if (isDeploymentNode(element)) {
            return (DeploymentNode) element;
        }
        throw new IllegalArgumentException("Element with id " + id + " is not a DeploymentNode: " + element);
    }


    public static ContainerInstance getContainerInstance(List<? extends Element> elements, String id) {
        var element = findContainerInstance(elements, id);
        if (element == null) {
            throw new NoSuchElementException("No element with id " + id + ". Available IDs: " + Entity.collectIds(elements));
        }
        return element;
    }

    public static ContainerInstance findContainerInstance(List<? extends Element> elements, String id) {
        var element = findElementById(elements, id);
        if (element == null) {
            return null;
        }
        if (isContainerInstance(element)) {
            return (ContainerInstance) element;
        }
        throw new IllegalArgumentException("Element with id " + id + " is not a ContainerInstance: " + element);
    }

    public static SoftwareSystemInstance getSoftwareSystemInstance(List<? extends Element> elements, String id) {
        var element = findSoftwareSystemInstance(elements, id);
        if (element == null) {
            throw new NoSuchElementException("No element with id " + id + ". Available IDs: " + Entity.collectIds(elements));
        }
        return element;
    }

    public static SoftwareSystemInstance findSoftwareSystemInstance(List<? extends Element> elements, String id) {
        var element = findElementById(elements, id);
        if (element == null) {
            return null;
        }
        if (isSoftwareSystemInstance(element)) {
            return (SoftwareSystemInstance) element;
        }
        throw new IllegalArgumentException("Element with id " + id + " is not a SoftwareSystemInstance: " + element);
    }

    public static InfrastructureNode getInfrastructureNode(List<? extends Element> elements, String id) {
        var element = findInfrastructureNode(elements, id);
        if (element == null) {
            throw new NoSuchElementException("No element with id " + id + ". Available IDs: " + Entity.collectIds(elements));
        }
        return element;
    }

    public static InfrastructureNode findInfrastructureNode(List<? extends Element> elements, String id) {
        var element = findElementById(elements, id);
        if (element == null) {
            return null;
        }
        if (isInfrastructureNode(element)) {
            return (InfrastructureNode) element;
        }
        throw new IllegalArgumentException("Element with id " + id + " is not a InfrastructureNode: " + element);
    }

    public static List<Group> getGroups(List<? extends Element> elements) {
        return elements.stream().filter(WorkspaceUtil::isGroup).map(e -> ((Group) e)).collect(Collectors.toList());
    }

    public static List<DeploymentEnvironment> getDeploymentEnvironments(List<? extends Element> elements) {
        return elements.stream().filter(WorkspaceUtil::isDeploymentEnvironment).map(e -> ((DeploymentEnvironment) e)).collect(Collectors.toList());
    }

    public static List<DeploymentNode> getDeploymentNodes(List<? extends Element> elements) {
        return elements.stream().filter(WorkspaceUtil::isDeploymentNode).map(e -> ((DeploymentNode) e)).collect(Collectors.toList());
    }

    public static List<ContainerInstance> getContainerInstances(List<? extends Element> elements) {
        return elements.stream().filter(WorkspaceUtil::isContainerInstance).map(e -> ((ContainerInstance) e)).collect(Collectors.toList());
    }

    public static List<SoftwareSystemInstance> getSoftwareSystemInstances(List<? extends Element> elements) {
        return elements.stream().filter(WorkspaceUtil::isSoftwareSystemInstance).map(e -> ((SoftwareSystemInstance) e)).collect(Collectors.toList());
    }

    public static List<InfrastructureNode> getInfrastructureNodes(List<? extends Element> elements) {
        return elements.stream().filter(WorkspaceUtil::isInfrastructureNode).map(e -> ((InfrastructureNode) e)).collect(Collectors.toList());
    }

    public static Container toContainer(Entity element) {
        if (isContainer(element)) {
            return (Container) element;
        }
        throw new IllegalArgumentException("Element " + element.toShortString() + " is not a container");
    }

    public static boolean isPerson(Entity element) {
        return element instanceof Person;
    }

    public static boolean isSoftwareSystem(Entity element) {
        return element instanceof SoftwareSystem;
    }

    public static boolean isContainer(Entity element) {
        return element instanceof Container;
    }

    public static boolean isComponent(Entity element) {
        return element instanceof Component;
    }

    public static boolean isGroup(Entity element) {
        return element instanceof Group;
    }

    public static boolean isModel(Entity element) {
        return element instanceof Model;
    }

    public static boolean isDeploymentEnvironment(Entity element) {
        return element instanceof DeploymentEnvironment;
    }

    public static boolean isDeploymentNode(Entity element) {
        return element instanceof DeploymentNode;
    }

    public static boolean isContainerInstance(Entity element) {
        return element instanceof ContainerInstance;
    }

    public static boolean isSoftwareSystemInstance(Entity element) {
        return element instanceof SoftwareSystemInstance;
    }

    public static boolean isInfrastructureNode(Entity element) {
        return element instanceof InfrastructureNode;
    }

    public static boolean isRelation(Entity element) {
        return element instanceof DirectRelation;
    }

    public static boolean isSameType(Entity element, ElementType type) {
        switch (type) {
            case PERSON -> {
                return isPerson(element);
            }
            case SOFTWARE_SYSTEM -> {
                return isSoftwareSystem(element);
            }
            case CONTAINER -> {
                return isContainer(element);
            }
            case COMPONENT -> {
                return isComponent(element);
            }
            case GROUP -> {
                return isGroup(element);
            }
            case DEPLOYMENT_ENVIRONMENT -> {
                return isDeploymentEnvironment(element);
            }
            case DEPLOYMENT_NODE -> {
                return isDeploymentNode(element);
            }
            case INFRASTRUCTURE_NODE -> {
                return isInfrastructureNode(element);
            }
            case SOFTWARE_SYSTEM_INSTANCE -> {
                return isSoftwareSystemInstance(element);
            }
            case CONTAINER_INSTANCE -> {
                return isContainerInstance(element);
            }
            case RELATION -> {
                return isRelation(element);
            }
        }
        throw new IllegalArgumentException("Element " + element.toShortString() + " is not supported, [" + type + "]");
    }

    public static Element findElementById(List<? extends Element> elements, String id) {
        return elements.stream().filter(e -> Entity.equals(id, e.getId())).findFirst().orElse(null);
    }

    public static List<Element> getPathFromRoot(Element element) {
        List<Element> elements = new ArrayList<>();
        var curr = element;
        while (curr != null) {
            elements.add(0, curr);
            curr = curr.getParent();
        }
        return elements;
    }
}
