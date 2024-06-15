package it.vitalegi.archi.model.relation;

import it.vitalegi.archi.model.Model;
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
import it.vitalegi.archi.visitor.ElementVisitor;

import java.util.ArrayList;
import java.util.List;

public class GetSuitableElementsForImplicitRelationVisitor implements ElementVisitor<List<Element>> {

    @Override
    public List<Element> visitComponent(Component component) {
        var arr = listOf(component);
        arr.addAll(component.getContainer().visit(this));
        return arr;
    }

    @Override
    public List<Element> visitContainer(Container container) {
        var arr = listOf(container);
        arr.addAll(container.getSoftwareSystem().visit(this));
        return arr;
    }

    @Override
    public List<Element> visitContainerInstance(ContainerInstance containerInstance) {
        return listOf(containerInstance);
    }

    @Override
    public List<Element> visitDeploymentEnvironment(DeploymentEnvironment deploymentEnvironment) {
        return listOf(deploymentEnvironment);
    }

    @Override
    public List<Element> visitDeploymentNode(DeploymentNode deploymentNode) {
        return listOf(deploymentNode);

    }

    @Override
    public List<Element> visitGroup(Group group) {
        return listOf(group);
    }

    @Override
    public List<Element> visitInfrastructureNode(InfrastructureNode infrastructureNode) {
        return listOf(infrastructureNode);
    }

    @Override
    public List<Element> visitModel(Model model) {
        return new ArrayList<>();
    }

    @Override
    public List<Element> visitPerson(Person person) {
        return listOf(person);
    }

    @Override
    public List<Element> visitSoftwareSystem(SoftwareSystem softwareSystem) {
        return listOf(softwareSystem);
    }

    @Override
    public List<Element> visitSoftwareSystemInstance(SoftwareSystemInstance softwareSystemInstance) {
        return listOf(softwareSystemInstance);
    }

    protected List<Element> listOf(Element e) {
        var arr = new ArrayList<Element>();
        arr.add(e);
        return arr;
    }
}
