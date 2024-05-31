package it.vitalegi.archi.model;

import it.vitalegi.archi.exception.ElementNotAllowedException;
import it.vitalegi.archi.util.WorkspaceUtil;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Group extends Node {

    public Group(Model model) {
        super(model);
    }

    public void addChild(Element child) {
        var path = getPathFromRoot();
        var isDescendantOfSoftwareSystem = path.stream().anyMatch(WorkspaceUtil::isSoftwareSystem);
        var isLikeRootElements = path.stream().allMatch(e -> WorkspaceUtil.isGroup(e) || WorkspaceUtil.isModel(e));
        if (isLikeRootElements && getModel().addSoftwareSystem(this, child)) {
            return;
        }
        if (getModel().addGroup(this, child)) {
            return;
        }
        if (isLikeRootElements && getModel().addPerson(this, child)) {
            return;
        }
        if (isDescendantOfSoftwareSystem && getModel().addContainer(this, child)) {
            return;
        }
        throw new ElementNotAllowedException(this, child);
    }
}
