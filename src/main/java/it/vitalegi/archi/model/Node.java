package it.vitalegi.archi.model;

import it.vitalegi.archi.util.WorkspaceUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Getter
@Slf4j
@EqualsAndHashCode(callSuper = true)
public abstract class Node extends Element {
    List<Element> elements;

    public Node(Model model) {
        super(model);
        elements = new ArrayList<>();
    }

    public Element findChildById(String id) {
        return elements.stream().filter(e -> e.sameId(id)).findFirst().orElse(null);
    }

    public Container findContainerById(String id) {
        return WorkspaceUtil.findContainer(elements, id);
    }

    public Group findGroupById(String id) {
        return WorkspaceUtil.findGroup(elements, id);
    }

    public SoftwareSystem findSoftwareSystemById(String id) {
        return WorkspaceUtil.findSoftwareSystem(elements, id);
    }

    public Person findPersonById(String id) {
        return WorkspaceUtil.findPerson(elements, id);
    }


    public List<Container> getContainers() {
        return WorkspaceUtil.getContainers(elements);
    }

    public List<Group> getGroups() {
        return WorkspaceUtil.getGroups(elements);
    }

    public List<SoftwareSystem> getSoftwareSystems() {
        return WorkspaceUtil.getSoftwareSystems(elements);
    }

    public List<Person> getPeople() {
        return WorkspaceUtil.getPeople(elements);
    }
}
