package it.vitalegi.archi.exporter.c4.plantuml.builder;

import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.model.diagram.DeploymentDiagram;
import it.vitalegi.archi.model.element.Component;
import it.vitalegi.archi.model.element.Container;
import it.vitalegi.archi.model.element.ContainerInstance;
import it.vitalegi.archi.model.element.DeploymentEnvironment;
import it.vitalegi.archi.model.element.DeploymentNode;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.element.Group;
import it.vitalegi.archi.model.element.InfrastructureNode;
import it.vitalegi.archi.model.element.Person;
import it.vitalegi.archi.model.element.SoftwareSystem;
import it.vitalegi.archi.model.element.SoftwareSystemInstance;
import it.vitalegi.archi.util.WorkspaceUtil;
import it.vitalegi.archi.visitor.ElementVisitor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Stream;

/**
 * the argument is the connected element found
 */
@Slf4j
@AllArgsConstructor
public class DeploymentConnectedElementsVisitor implements ElementVisitor<Stream<Element>> {

    DeploymentDiagram diagram;

    @Override
    public Stream<Element> visitComponent(Component component) {
        return Stream.empty();
    }

    /**
     * if container is in scope, then all container instances are. If no container instance found, remove also the container.
     *
     * @param container
     * @return
     */
    @Override
    public Stream<Element> visitContainer(Container container) {
        var env = diagram.deploymentEnvironment();
        var containerInstances = WorkspaceUtil.getContainerInstances(env.getAllChildren().toList()).stream().filter(c -> c.getContainer().equals(container)).toList();
        if (containerInstances.isEmpty()) {
            return Stream.empty();
        }
        return Stream.concat(Stream.of(container), containerInstances.stream());
    }

    /**
     * if ContainerInstance is in scope, also Container is in scope
     *
     * @param containerInstance
     * @return
     */
    @Override
    public Stream<Element> visitContainerInstance(ContainerInstance containerInstance) {
        return Stream.of(containerInstance, containerInstance.getContainer());
    }

    @Override
    public Stream<Element> visitDeploymentEnvironment(DeploymentEnvironment deploymentEnvironment) {
        return Stream.empty();
    }

    @Override
    public Stream<Element> visitDeploymentNode(DeploymentNode deploymentNode) {
        return Stream.of(deploymentNode);
    }

    @Override
    public Stream<Element> visitGroup(Group group) {
        return Stream.of(group);
    }

    @Override
    public Stream<Element> visitInfrastructureNode(InfrastructureNode infrastructureNode) {
        return Stream.of(infrastructureNode);
    }

    @Override
    public Stream<Element> visitModel(Model model) {
        return Stream.empty();
    }

    @Override
    public Stream<Element> visitPerson(Person person) {
        return Stream.empty();
    }

    /**
     * if SoftwareSystem is in scope, then all SoftwareSystemInstances are. If no SoftwareSystemInstance found, remove also the SoftwareSystem.
     *
     * @param softwareSystem
     * @return
     */
    @Override
    public Stream<Element> visitSoftwareSystem(SoftwareSystem softwareSystem) {
        var env = diagram.deploymentEnvironment();
        var instances = WorkspaceUtil.getSoftwareSystemInstances(env.getAllChildren().toList()).stream().filter(c -> c.getSoftwareSystem().equals(softwareSystem)).toList();
        if (instances.isEmpty()) {
            return Stream.empty();
        }
        return Stream.concat(Stream.of(softwareSystem), instances.stream());
    }

    /**
     * if ContainerInstance is in scope, also Container is in scope
     *
     * @param softwareSystemInstance
     * @return
     */
    @Override
    public Stream<Element> visitSoftwareSystemInstance(SoftwareSystemInstance softwareSystemInstance) {
        return Stream.of(softwareSystemInstance, softwareSystemInstance.getSoftwareSystem());
    }
}
