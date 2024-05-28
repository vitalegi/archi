package it.vitalegi.archi.model;

import it.vitalegi.archi.util.WorkspaceUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public Group getGroup(String id) {
        return WorkspaceUtil.getGroup(elements, id);
    }

    public List<Group> getGroup() {
        return WorkspaceUtil.getGroups(elements);
    }

    public Element findChildById(String id) {
        for (var e : elements) {
            if (Objects.equals(e.getId(), id)) {
                return e;
            }
            var child = e.findChildById(id);
            if (child != null) {
                return child;
            }
        }
        return null;
    }
}
