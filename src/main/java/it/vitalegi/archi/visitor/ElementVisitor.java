package it.vitalegi.archi.visitor;

import it.vitalegi.archi.model.Component;
import it.vitalegi.archi.model.Container;
import it.vitalegi.archi.model.ContainerInstance;
import it.vitalegi.archi.model.DeploymentEnvironment;
import it.vitalegi.archi.model.DeploymentNode;
import it.vitalegi.archi.model.Group;
import it.vitalegi.archi.model.InfrastructureNode;
import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.model.Person;
import it.vitalegi.archi.model.SoftwareSystem;
import it.vitalegi.archi.model.SoftwareSystemInstance;

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
