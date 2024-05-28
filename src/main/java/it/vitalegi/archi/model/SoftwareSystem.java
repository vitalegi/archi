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
public class SoftwareSystem extends Element {
    List<Container> containers;
    List<Group> groups;

    public SoftwareSystem() {
        containers = new ArrayList<>();
        groups = new ArrayList<>();
    }

    public Container findContainerById(String id) {
        return WorkspaceUtil.findContainer(containers, id);
    }

    public Group findGroupById(String id) {
        return WorkspaceUtil.findGroup(groups, id);
    }


    public void addChild(Element child) {
        if (child instanceof Group o) {
            log.info("Add {} to software system {}", child.getId(), this.getId());
            groups.add(o);
            return;
        }
        if (child instanceof Container o) {
            log.info("Add {} to software system {}", child.getId(), this.getId());
            containers.add(o);
            return;
        }
        throw new IllegalArgumentException("Can't add " + child + " to a software system " + this);
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
        return null;
    }
}
