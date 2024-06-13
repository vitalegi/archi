package it.vitalegi.archi.model.builder;

import it.vitalegi.archi.exception.RelationNotAllowedException;
import it.vitalegi.archi.model.element.DeploymentEnvironment;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.element.ElementType;
import it.vitalegi.archi.model.relation.Relation;
import it.vitalegi.archi.util.WorkspaceUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RelationValidator {
    private final Map<ElementType, List<ElementType>> FROM_TO_ALLOWED = new HashMap<>();

    public RelationValidator() {
        FROM_TO_ALLOWED.put(ElementType.PERSON, Arrays.asList(ElementType.PERSON, ElementType.SOFTWARE_SYSTEM, ElementType.CONTAINER, ElementType.COMPONENT));
        FROM_TO_ALLOWED.put(ElementType.SOFTWARE_SYSTEM, Arrays.asList(ElementType.PERSON, ElementType.SOFTWARE_SYSTEM, ElementType.CONTAINER, ElementType.COMPONENT));
        FROM_TO_ALLOWED.put(ElementType.CONTAINER, Arrays.asList(ElementType.PERSON, ElementType.SOFTWARE_SYSTEM, ElementType.CONTAINER, ElementType.COMPONENT));
        FROM_TO_ALLOWED.put(ElementType.COMPONENT, Arrays.asList(ElementType.PERSON, ElementType.SOFTWARE_SYSTEM, ElementType.CONTAINER, ElementType.COMPONENT));
        FROM_TO_ALLOWED.put(ElementType.DEPLOYMENT_ENVIRONMENT, Collections.emptyList());
        FROM_TO_ALLOWED.put(ElementType.DEPLOYMENT_NODE, List.of(ElementType.DEPLOYMENT_NODE));
        FROM_TO_ALLOWED.put(ElementType.INFRASTRUCTURE_NODE, Arrays.asList(ElementType.DEPLOYMENT_NODE, ElementType.INFRASTRUCTURE_NODE, ElementType.SOFTWARE_SYSTEM_INSTANCE, ElementType.CONTAINER_INSTANCE));
        FROM_TO_ALLOWED.put(ElementType.SOFTWARE_SYSTEM_INSTANCE, List.of(ElementType.INFRASTRUCTURE_NODE));
        FROM_TO_ALLOWED.put(ElementType.CONTAINER_INSTANCE, List.of(ElementType.INFRASTRUCTURE_NODE));
        FROM_TO_ALLOWED.put(ElementType.GROUP, Collections.emptyList());
    }

    public void checkAllowed(Relation relation) {
        var from = relation.getFrom();
        var to = relation.getTo();
        if (from == null) {
            throw new NullPointerException("Can't process relation, from is null. " + relation);
        }
        if (to == null) {
            throw new NullPointerException("Can't process relation, to is null. " + relation);
        }
        if (from.getElementType() == null) {
            throw new NullPointerException(from.toShortString() + " doesn't have ElementType, can't be connected. " + relation);
        }
        if (to.getElementType() == null) {
            throw new NullPointerException(to.toShortString() + " doesn't have ElementType, can't be connected. " + relation);
        }
        var allowed = FROM_TO_ALLOWED.get(from.getElementType());
        if (allowed == null) {
            throw new RuntimeException("Missing configuration for " + from.getElementType() + ", " + relation);
        }
        if (!allowed.contains(to.getElementType())) {
            throw new RelationNotAllowedException(relation);
        }
        var de1 = getDeploymentEnvironmentOrNull(from);
        var de2 = getDeploymentEnvironmentOrNull(to);
        if (!Objects.equals(de1, de2)) {
            var msg = new StringBuilder();
            msg.append(from.toShortString());
            if (de1 != null) {
                msg.append(" is part of ").append(de1.toShortString());
            } else {
                msg.append(" is not part of any environment");
            }
            msg.append(", ").append(to.toShortString());
            if (de2 != null) {
                msg.append(" is part of ").append(de2.toShortString());
            } else {
                msg.append(" is not part of any environment");
            }
            throw new RelationNotAllowedException(msg.toString(), relation);
        }
    }

    protected DeploymentEnvironment getDeploymentEnvironmentOrNull(Element element) {
        var path = WorkspaceUtil.getPathFromRoot(element);
        for (var e : path) {
            if (WorkspaceUtil.isDeploymentEnvironment(e)) {
                return (DeploymentEnvironment) e;
            }
        }
        return null;
    }
}
