package it.vitalegi.archi.model.element;

import it.vitalegi.archi.model.Entity;
import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.util.WorkspaceUtil;
import it.vitalegi.archi.visitor.ElementVisitor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@Getter
@Setter
public abstract class Element extends Entity {
    Element parent;
    List<Element> elements;

    String name;
    String description;
    List<String> tags;
    List<String> technologies;
    Map<String, String> metadata;

    public Element(Model model) {
        super(model);
        tags = new ArrayList<>();
        metadata = new HashMap<>();
        elements = new ArrayList<>();
    }

    public abstract <E> E visit(ElementVisitor<E> visitor);

    public Stream<Element> getAllChildren() {
        return elements.stream().flatMap(e -> Stream.concat(Stream.of(e), e.getAllChildren()));
    }

    @Override
    public String toString() {
        return "Element{" + "parent=" + (parent != null ? parent.toShortString() : "null") + ", name='" + name + '\'' + ", description='" + description + '\'' + ", tags=" + tags + ", metadata=" + metadata + '}';
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


    public Component findComponentById(String id) {
        return WorkspaceUtil.findComponent(elements, id);
    }

    public List<Component> getComponents() {
        return WorkspaceUtil.getComponents(elements);
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

    public DeploymentEnvironment findDeploymentEnvironmentById(String id) {
        return WorkspaceUtil.findDeploymentEnvironment(elements, id);
    }

    public List<DeploymentEnvironment> getDeploymentEnvironments() {
        return WorkspaceUtil.getDeploymentEnvironments(elements);
    }

    public DeploymentNode findDeploymentNodeById(String id) {
        return WorkspaceUtil.findDeploymentNode(elements, id);
    }

    public List<DeploymentNode> getDeploymentNodes() {
        return WorkspaceUtil.getDeploymentNodes(elements);
    }


    public ContainerInstance findContainerInstanceById(String id) {
        return WorkspaceUtil.findContainerInstance(elements, id);
    }

    public List<ContainerInstance> getContainerInstances() {
        return WorkspaceUtil.getContainerInstances(elements);
    }


    public InfrastructureNode findInfrastructureNodeById(String id) {
        return WorkspaceUtil.findInfrastructureNode(elements, id);
    }

    public List<InfrastructureNode> getInfrastructureNodes() {
        return WorkspaceUtil.getInfrastructureNodes(elements);
    }

    public SoftwareSystemInstance findSoftwareSystemInstanceById(String id) {
        return WorkspaceUtil.findSoftwareSystemInstance(elements, id);
    }

    public List<SoftwareSystemInstance> getSoftwareSystemInstances() {
        return WorkspaceUtil.getSoftwareSystemInstances(elements);
    }
}
