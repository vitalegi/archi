package it.vitalegi.archi.visitor;

import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.model.element.Component;
import it.vitalegi.archi.model.element.Container;
import it.vitalegi.archi.model.element.ContainerInstance;
import it.vitalegi.archi.model.element.DeploymentEnvironment;
import it.vitalegi.archi.model.element.DeploymentNode;
import it.vitalegi.archi.model.element.Group;
import it.vitalegi.archi.model.element.InfrastructureNode;
import it.vitalegi.archi.model.element.Person;
import it.vitalegi.archi.model.element.SoftwareSystem;
import it.vitalegi.archi.model.element.SoftwareSystemInstance;

public interface ElementVisitor<E> {
    E visitComponent(Component component);

    E visitContainer(Container container);

    E visitContainerInstance(ContainerInstance containerInstance);

    E visitDeploymentEnvironment(DeploymentEnvironment deploymentEnvironment);

    E visitDeploymentNode(DeploymentNode deploymentNode);

    E visitGroup(Group group);

    E visitInfrastructureNode(InfrastructureNode infrastructureNode);

    E visitModel(Model model);

    E visitPerson(Person person);

    E visitSoftwareSystem(SoftwareSystem softwareSystem);

    E visitSoftwareSystemInstance(SoftwareSystemInstance softwareSystemInstance);
}
