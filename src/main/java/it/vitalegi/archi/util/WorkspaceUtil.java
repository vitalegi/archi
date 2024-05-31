package it.vitalegi.archi.util;

import it.vitalegi.archi.model.Container;
import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.Group;
import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.model.Person;
import it.vitalegi.archi.model.SoftwareSystem;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class WorkspaceUtil {
    public static Person getPerson(List<Element> elements, String id) {
        var element = findPerson(elements, id);
        if (element == null) {
            throw new NoSuchElementException("No element with id " + id + ". Available IDs: " + Element.collectIds(elements));
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
            throw new NoSuchElementException("No element with id " + id + ". Available IDs: " + Element.collectIds(elements));
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
            throw new NoSuchElementException("No element with id " + id + ". Available IDs: " + Element.collectIds(elements));
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

    public static Group getGroup(List<? extends Element> elements, String id) {
        var element = findGroup(elements, id);
        if (element == null) {
            throw new NoSuchElementException("No element with id " + id + ". Available IDs: " + Element.collectIds(elements));
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

    public static List<Group> getGroups(List<? extends Element> elements) {
        return elements.stream().filter(WorkspaceUtil::isGroup).map(e -> ((Group) e)).collect(Collectors.toList());
    }

    public static boolean isPerson(Element element) {
        return element instanceof Person;
    }

    public static boolean isSoftwareSystem(Element element) {
        return element instanceof SoftwareSystem;
    }

    public static boolean isContainer(Element element) {
        return element instanceof Container;
    }

    public static boolean isGroup(Element element) {
        return element instanceof Group;
    }

    public static boolean isModel(Element element) {
        return element instanceof Model;
    }

    public static Element findElementById(List<? extends Element> elements, String id) {
        return elements.stream().filter(e -> Element.sameId(id, e.getId())).findFirst().orElse(null);
    }
}
