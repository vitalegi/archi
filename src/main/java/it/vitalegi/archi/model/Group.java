package it.vitalegi.archi.model;

import it.vitalegi.archi.util.WorkspaceUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
public class Group extends Element {
    List<SoftwareSystem> softwareSystems;
    List<Group> groups;
    List<Container> containers;
    List<Person> people;

    public Group() {
        softwareSystems = new ArrayList<>();
        groups = new ArrayList<>();
        containers = new ArrayList<>();
        people = new ArrayList<>();
    }

    public void addChild(Element child) {
        if (child instanceof SoftwareSystem o) {
            log.info("Add {} to group {}", child.getId(), this.getId());
            softwareSystems.add(o);
            return;
        }
        if (child instanceof Group o) {
            log.info("Add {} to group {}", child.getId(), this.getId());
            groups.add(o);
            return;
        }
        if (child instanceof Container o) {
            log.info("Add {} to group {}", child.getId(), this.getId());
            containers.add(o);
            return;
        }
        if (child instanceof Person o) {
            log.info("Add {} to group {}", child.getId(), this.getId());
            people.add(o);
            return;
        }
        throw new IllegalArgumentException("Can't add " + child + " to a group " + this);
    }
    public Element findChildById(String id) {
        var container = findContainerById(id);
        if (container != null) {
            return container;
        }
        var group = findGroupById(id);
        if (group != null) {
            return group;
        }
        var softwareSystem = findSoftwareSystemById(id);
        if (softwareSystem != null) {
            return softwareSystem;
        }
        var person = findPersonById(id);
        if (person != null) {
            return person;
        }
        return null;
    }

    public Container findContainerById(String id) {
        return WorkspaceUtil.findContainer(containers, id);
    }

    public Group findGroupById(String id) {
        return WorkspaceUtil.findGroup(groups, id);
    }

    public SoftwareSystem findSoftwareSystemById(String id) {
        return WorkspaceUtil.findSoftwareSystem(softwareSystems, id);
    }

    public Person findPersonById(String id) {
        return WorkspaceUtil.findPerson(people, id);
    }
}
