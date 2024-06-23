package it.vitalegi.archi.util;

import it.vitalegi.archi.model.element.ElementType;
import it.vitalegi.archi.model.element.PropertyEntries;
import it.vitalegi.archi.model.style.Style;
import it.vitalegi.archi.workspaceloader.model.DeploymentDiagramRaw;
import it.vitalegi.archi.workspaceloader.model.ElementRaw;
import it.vitalegi.archi.workspaceloader.model.FlowDiagramRaw;
import it.vitalegi.archi.workspaceloader.model.FlowRaw;
import it.vitalegi.archi.workspaceloader.model.LandscapeDiagramRaw;
import it.vitalegi.archi.workspaceloader.model.RelationRaw;
import it.vitalegi.archi.workspaceloader.model.SystemContextDiagramRaw;
import it.vitalegi.archi.workspaceloader.model.WorkspaceRaw;

import java.util.List;
import java.util.UUID;

public class WorkspaceModelBuilder {
    protected WorkspaceRaw workspace;

    public WorkspaceModelBuilder() {
        workspace = new WorkspaceRaw();
    }

    public WorkspaceModelBuilder person(String id) {
        return person(null, id);
    }

    public WorkspaceModelBuilder person(String parentId, String id) {
        return person(parentId, id, UUID.randomUUID().toString());
    }

    public WorkspaceModelBuilder person(String parentId, String id, String name) {
        workspace.getElements().add(element(ElementType.PERSON, parentId, id, name, null, null, null));
        return this;
    }

    public WorkspaceModelBuilder element(ElementRaw.ElementRawBuilder builder) {
        workspace.getElements().add(builder.build());
        return this;
    }


    public WorkspaceModelBuilder softwareSystem(String id) {
        return softwareSystem(null, id);
    }

    public WorkspaceModelBuilder softwareSystem(String parentId, String id) {
        return softwareSystem(parentId, id, UUID.randomUUID().toString());
    }

    public WorkspaceModelBuilder softwareSystem(String parentId, String id, String name) {
        workspace.getElements().add(element(ElementType.SOFTWARE_SYSTEM, parentId, id, name, null, null, null));
        return this;
    }

    public WorkspaceModelBuilder container(String id) {
        return container(null, id);
    }

    public WorkspaceModelBuilder container(String parentId, String id) {
        return container(parentId, id, UUID.randomUUID().toString());
    }

    public WorkspaceModelBuilder container(String parentId, String id, String name) {
        workspace.getElements().add(element(ElementType.CONTAINER, parentId, id, name, null, null, null));
        return this;
    }

    public WorkspaceModelBuilder component(ElementRaw.ElementRawBuilder builder) {
        workspace.getElements().add(builder.type(ElementType.COMPONENT).build());
        return this;
    }


    public WorkspaceModelBuilder group(String id) {
        return group(null, id);
    }

    public WorkspaceModelBuilder group(String parentId, String id) {
        return group(parentId, id, UUID.randomUUID().toString());
    }

    public WorkspaceModelBuilder group(String parentId, String id, String name) {
        workspace.getElements().add(element(ElementType.GROUP, parentId, id, name, null, null, null));
        return this;
    }

    public WorkspaceModelBuilder deploymentEnvironment(String id) {
        return deploymentEnvironment(null, id);
    }

    public WorkspaceModelBuilder deploymentEnvironment(String parentId, String id) {
        return deploymentEnvironment(parentId, id, UUID.randomUUID().toString());
    }

    public WorkspaceModelBuilder deploymentEnvironment(String parentId, String id, String name) {
        workspace.getElements().add(element(ElementType.DEPLOYMENT_ENVIRONMENT, parentId, id, name, null, null, null));
        return this;
    }

    public WorkspaceModelBuilder deploymentNode(String id) {
        return deploymentNode(null, id);
    }

    public WorkspaceModelBuilder deploymentNode(String parentId, String id) {
        return deploymentNode(parentId, id, UUID.randomUUID().toString());
    }

    public WorkspaceModelBuilder deploymentNode(String parentId, String id, String name) {
        workspace.getElements().add(element(ElementType.DEPLOYMENT_NODE, parentId, id, name, null, null, null));
        return this;
    }

    public WorkspaceModelBuilder containerInstance(String id, String containerId) {
        return containerInstance(null, id, containerId);
    }

    public WorkspaceModelBuilder containerInstance(String parentId, String id, String containerId) {
        return containerInstance(parentId, id, containerId, UUID.randomUUID().toString());
    }

    public WorkspaceModelBuilder containerInstance(String parentId, String id, String containerId, String name) {
        workspace.getElements().add(containerInstance(ElementType.CONTAINER_INSTANCE, parentId, id, name, null, null, null, containerId));
        return this;
    }

    public WorkspaceModelBuilder infrastructureNode(String id) {
        return infrastructureNode(null, id);
    }

    public WorkspaceModelBuilder infrastructureNode(String parentId, String id) {
        return infrastructureNode(parentId, id, UUID.randomUUID().toString());
    }

    public WorkspaceModelBuilder infrastructureNode(String parentId, String id, String name) {
        workspace.getElements().add(element(ElementType.INFRASTRUCTURE_NODE, parentId, id, name, null, null, null));
        return this;
    }

    public WorkspaceRaw build() {
        return workspace;
    }

    protected ElementRaw containerInstance(ElementType type, String parentId, String id, String name, String description, List<String> tags, PropertyEntries properties, String containerId) {
        var out = element(type, parentId, id, name, description, tags, properties);
        out.setContainerId(containerId);
        return out;
    }

    public WorkspaceModelBuilder softwareSystemInstance(String id, String softwareSystemId) {
        return softwareSystemInstance(null, id, softwareSystemId);
    }

    public WorkspaceModelBuilder softwareSystemInstance(String parentId, String id, String softwareSystemId) {
        return softwareSystemInstance(parentId, id, softwareSystemId, UUID.randomUUID().toString());
    }

    public WorkspaceModelBuilder softwareSystemInstance(String parentId, String id, String softwareSystemId, String name) {
        workspace.getElements().add(softwareSystemInstance(ElementType.SOFTWARE_SYSTEM_INSTANCE, parentId, id, name, null, null, null, softwareSystemId));
        return this;
    }

    public WorkspaceModelBuilder relation(String from, String to) {
        workspace.getRelations().add(relation(from, to, null, null, null));
        return this;
    }

    public WorkspaceModelBuilder relation(RelationRaw.RelationRawBuilder builder) {
        workspace.getRelations().add(builder.build());
        return this;
    }

    protected ElementRaw softwareSystemInstance(ElementType type, String parentId, String id, String name, String description, List<String> tags, PropertyEntries properties, String softwareSystemId) {
        var out = element(type, parentId, id, name, description, tags, properties);
        out.setSoftwareSystemId(softwareSystemId);
        return out;
    }

    public WorkspaceModelBuilder deploymentDiagram(String scope, String environment, String name) {
        var diagram = new DeploymentDiagramRaw();
        diagram.setScope(scope);
        diagram.setEnvironment(environment);
        diagram.setName(name);
        workspace.getDiagrams().add(diagram);
        return this;
    }

    public WorkspaceModelBuilder systemContextDiagram(String name, String target) {
        return systemContextDiagram(name, target, null);
    }

    public WorkspaceModelBuilder systemContextDiagram(String name, String target, String title) {
        var diagram = new SystemContextDiagramRaw();
        diagram.setName(name);
        diagram.setTarget(target);
        diagram.setTitle(title);
        workspace.getDiagrams().add(diagram);
        return this;
    }

    public WorkspaceModelBuilder deploymentDiagram(DeploymentDiagramRaw.DeploymentDiagramRawBuilder builder) {
        workspace.getDiagrams().add(builder.build());
        return this;
    }

    public WorkspaceModelBuilder landscapeDiagram(LandscapeDiagramRaw.LandscapeDiagramRawBuilder builder) {
        workspace.getDiagrams().add(builder.build());
        return this;
    }

    public WorkspaceModelBuilder systemContextDiagram(SystemContextDiagramRaw.SystemContextDiagramRawBuilder builder) {
        workspace.getDiagrams().add(builder.build());
        return this;
    }

    public WorkspaceModelBuilder flowDiagram(FlowDiagramRaw.FlowDiagramRawBuilder builder) {
        workspace.getDiagrams().add(builder.build());
        return this;
    }

    public WorkspaceModelBuilder landscapeDiagram(String name) {
        return landscapeDiagram(name, null);
    }

    public WorkspaceModelBuilder landscapeDiagram(String name, String title) {
        var diagram = new LandscapeDiagramRaw();
        diagram.setName(name);
        diagram.setTitle(title);
        workspace.getDiagrams().add(diagram);
        return this;
    }

    public WorkspaceModelBuilder globalStyle(Style.StyleBuilder styleBuilder) {
        workspace.setStyle(styleBuilder.build());
        return this;
    }

    public WorkspaceModelBuilder globalStyle(Style style) {
        workspace.setStyle(style);
        return this;
    }

    public WorkspaceModelBuilder flow(FlowRaw.FlowRawBuilder builder) {
        workspace.getFlows().add(builder.build());
        return this;
    }

    protected ElementRaw element(ElementType type, String parentId, String id, String name, String description, List<String> tags, PropertyEntries properties) {
        var out = new ElementRaw();
        out.setType(type);
        out.setParentId(parentId);
        out.setId(id);
        out.setName(name);
        out.setDescription(description);
        out.setTags(tags);
        out.setProperties(properties);
        return out;
    }

    protected RelationRaw relation(String from, String to, String description, List<String> tags, PropertyEntries properties) {
        var out = new RelationRaw();
        out.setFrom(from);
        out.setTo(to);
        out.setDescription(description);
        out.setTags(tags);
        out.setProperties(properties);
        return out;
    }
}
