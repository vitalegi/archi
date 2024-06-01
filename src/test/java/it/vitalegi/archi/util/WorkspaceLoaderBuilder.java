package it.vitalegi.archi.util;

import it.vitalegi.archi.workspace.loader.model.ElementRaw;
import it.vitalegi.archi.model.ElementType;
import it.vitalegi.archi.workspace.loader.model.RelationRaw;
import it.vitalegi.archi.workspace.loader.model.Workspace;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WorkspaceLoaderBuilder {
    protected Workspace workspace;

    public WorkspaceLoaderBuilder() {
        workspace = new Workspace();
    }

    public WorkspaceLoaderBuilder person(String id) {
        return person(null, id);
    }

    public WorkspaceLoaderBuilder person(String parentId, String id) {
        return person(parentId, id, UUID.randomUUID().toString());
    }

    public WorkspaceLoaderBuilder person(String parentId, String id, String name) {
        workspace.getElements().add(element(ElementType.PERSON, parentId, id, name, null, null, null));
        return this;
    }

    public WorkspaceLoaderBuilder softwareSystem(String id) {
        return softwareSystem(null, id);
    }

    public WorkspaceLoaderBuilder softwareSystem(String parentId, String id) {
        return softwareSystem(parentId, id, UUID.randomUUID().toString());
    }

    public WorkspaceLoaderBuilder softwareSystem(String parentId, String id, String name) {
        workspace.getElements().add(element(ElementType.SOFTWARE_SYSTEM, parentId, id, name, null, null, null));
        return this;
    }

    public WorkspaceLoaderBuilder container(String id) {
        return container(null, id);
    }

    public WorkspaceLoaderBuilder container(String parentId, String id) {
        return container(parentId, id, UUID.randomUUID().toString());
    }

    public WorkspaceLoaderBuilder container(String parentId, String id, String name) {
        workspace.getElements().add(element(ElementType.CONTAINER, parentId, id, name, null, null, null));
        return this;
    }

    public WorkspaceLoaderBuilder group(String id) {
        return group(null, id);
    }

    public WorkspaceLoaderBuilder group(String parentId, String id) {
        return group(parentId, id, UUID.randomUUID().toString());
    }

    public WorkspaceLoaderBuilder group(String parentId, String id, String name) {
        workspace.getElements().add(element(ElementType.GROUP, parentId, id, name, null, null, null));
        return this;
    }

    public WorkspaceLoaderBuilder deploymentEnvironment(String id) {
        return deploymentEnvironment(null, id);
    }

    public WorkspaceLoaderBuilder deploymentEnvironment(String parentId, String id) {
        return deploymentEnvironment(parentId, id, UUID.randomUUID().toString());
    }

    public WorkspaceLoaderBuilder deploymentEnvironment(String parentId, String id, String name) {
        workspace.getElements().add(element(ElementType.DEPLOYMENT_ENVIRONMENT, parentId, id, name, null, null, null));
        return this;
    }

    public WorkspaceLoaderBuilder deploymentNode(String id) {
        return deploymentNode(null, id);
    }

    public WorkspaceLoaderBuilder deploymentNode(String parentId, String id) {
        return deploymentNode(parentId, id, UUID.randomUUID().toString());
    }

    public WorkspaceLoaderBuilder deploymentNode(String parentId, String id, String name) {
        workspace.getElements().add(element(ElementType.DEPLOYMENT_NODE, parentId, id, name, null, null, null));
        return this;
    }

    public WorkspaceLoaderBuilder containerInstance(String id, String containerId) {
        return containerInstance(null, id, containerId);
    }

    public WorkspaceLoaderBuilder containerInstance(String parentId, String id, String containerId) {
        return containerInstance(parentId, id, containerId, UUID.randomUUID().toString());
    }

    public WorkspaceLoaderBuilder containerInstance(String parentId, String id, String containerId, String name) {
        workspace.getElements().add(containerInstance(ElementType.CONTAINER_INSTANCE, parentId, id, name, null, null, null, containerId));
        return this;
    }

    public WorkspaceLoaderBuilder infrastructureNode(String id) {
        return infrastructureNode(null, id);
    }

    public WorkspaceLoaderBuilder infrastructureNode(String parentId, String id) {
        return infrastructureNode(parentId, id, UUID.randomUUID().toString());
    }

    public WorkspaceLoaderBuilder infrastructureNode(String parentId, String id, String name) {
        workspace.getElements().add(element(ElementType.INFRASTRUCTURE_NODE, parentId, id, name, null, null, null));
        return this;
    }

    public Workspace build() {
        return workspace;
    }

    protected ElementRaw containerInstance(ElementType type, String parentId, String id, String name, String description, List<String> tags, Map<String, String> metadata, String containerId) {
        var out = element(type, parentId, id, name, description, tags, metadata);
        out.setContainerId(containerId);
        return out;
    }

    public WorkspaceLoaderBuilder softwareSystemInstance(String id, String softwareSystemId) {
        return softwareSystemInstance(null, id, softwareSystemId);
    }

    public WorkspaceLoaderBuilder softwareSystemInstance(String parentId, String id, String softwareSystemId) {
        return softwareSystemInstance(parentId, id, softwareSystemId, UUID.randomUUID().toString());
    }

    public WorkspaceLoaderBuilder softwareSystemInstance(String parentId, String id, String softwareSystemId, String name) {
        workspace.getElements().add(softwareSystemInstance(ElementType.SOFTWARE_SYSTEM_INSTANCE, parentId, id, name, null, null, null, softwareSystemId));
        return this;
    }

    public WorkspaceLoaderBuilder relation(String from, String to) {
        workspace.getRelations().add(relation(from, to, null, null, null));
        return this;
    }

    protected ElementRaw softwareSystemInstance(ElementType type, String parentId, String id, String name, String description, List<String> tags, Map<String, String> metadata, String softwareSystemId) {
        var out = element(type, parentId, id, name, description, tags, metadata);
        out.setSoftwareSystemId(softwareSystemId);
        return out;
    }

    protected ElementRaw element(ElementType type, String parentId, String id, String name, String description, List<String> tags, Map<String, String> metadata) {
        var out = new ElementRaw();
        out.setType(type);
        out.setParentId(parentId);
        out.setId(id);
        out.setName(name);
        out.setDescription(description);
        out.setTags(tags);
        out.setMetadata(metadata);
        return out;
    }

    protected RelationRaw relation(String from, String to, String description, List<String> tags, Map<String, String> metadata) {
        var out = new RelationRaw();
        out.setFrom(from);
        out.setTo(to);
        out.setDescription(description);
        out.setTags(tags);
        out.setMetadata(metadata);
        return out;
    }
}
