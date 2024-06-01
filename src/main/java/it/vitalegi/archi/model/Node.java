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


}
