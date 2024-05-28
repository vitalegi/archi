package it.vitalegi.archi.util;

import it.vitalegi.archi.model.Container;
import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.Person;
import it.vitalegi.archi.model.SoftwareSystem;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

public class WorkspaceUtil {
    public static Person getPerson(List<Element> elements, String id) {
        var element = findElementById(elements, id);
        if (element == null) {
            throw new NoSuchElementException("No element with id " + id + ". Available IDs: " + Element.collectIds(elements));
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
        var element = findElementById(elements, id);
        if (element == null) {
            throw new NoSuchElementException("No element with id " + id + ". Available IDs: " + Element.collectIds(elements));
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
        var element = findElementById(elements, id);
        if (element == null) {
            throw new NoSuchElementException("No element with id " + id + ". Available IDs: " + Element.collectIds(elements));
        }
        if (isContainer(element)) {
            return (Container) element;
        }
        throw new IllegalArgumentException("Element with id " + id + " is not a Container: " + element);
    }

    public static List<Container> getContainers(List<? extends Element> elements) {
        return getSoftwareSystems(elements).stream().flatMap(ss -> ss.getContainers().stream()).collect(Collectors.toList());
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

    protected static Element findElementById(List<? extends Element> elements, String id) {
        return elements.stream().filter(e -> Objects.equals(id, e.getId())).findFirst().orElse(null);
    }
}
