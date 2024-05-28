package it.vitalegi.archi.model;

import it.vitalegi.archi.util.WorkspaceUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Workspace {
    List<Element> elements;

    public Workspace() {
        elements = new ArrayList<>();
    }

    public Person getPerson(String id) {
        return WorkspaceUtil.getPerson(elements, id);
    }

    public List<Person> getPeople() {
        return WorkspaceUtil.getPeople(elements);
    }

    public SoftwareSystem getSoftwareSystem(String id) {
        return WorkspaceUtil.getSoftwareSystem(elements, id);
    }

    public List<SoftwareSystem> getSoftwareSystems() {
        return WorkspaceUtil.getSoftwareSystems(elements);
    }

    public Container getContainer(String id) {
        var softwareSystems = WorkspaceUtil.getSoftwareSystems(elements);
        return WorkspaceUtil.getContainer(softwareSystems.stream().flatMap(ss -> ss.getContainers().stream()).collect(Collectors.toList()), id);
    }

    public List<Container> getContainers() {
        return WorkspaceUtil.getContainers(WorkspaceUtil.getSoftwareSystems(elements));
    }
}
