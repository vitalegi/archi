package it.vitalegi.archi.exporter.c4.plantuml.writer;

import it.vitalegi.archi.diagram.C4Shape;
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
import it.vitalegi.archi.visitor.ElementVisitor;

public class PlantumlShapeElementVisitor implements ElementVisitor<String> {
    @Override
    public String visitComponent(Component component) {
        return C4Shape.RECTANGLE.getPlantumlShape();
    }

    @Override
    public String visitContainer(Container container) {
        return C4Shape.RECTANGLE.getPlantumlShape();
    }

    @Override
    public String visitContainerInstance(ContainerInstance containerInstance) {
        return C4Shape.RECTANGLE.getPlantumlShape();
    }

    @Override
    public String visitDeploymentEnvironment(DeploymentEnvironment deploymentEnvironment) {
        return null;
    }

    @Override
    public String visitDeploymentNode(DeploymentNode deploymentNode) {
        return null;
    }

    @Override
    public String visitGroup(Group group) {
        return null;
    }

    @Override
    public String visitInfrastructureNode(InfrastructureNode infrastructureNode) {
        return C4Shape.RECTANGLE.getPlantumlShape();
    }

    @Override
    public String visitModel(Model model) {
        return null;
    }

    @Override
    public String visitPerson(Person person) {
        return C4Shape.ACTOR.getPlantumlShape();
    }

    @Override
    public String visitSoftwareSystem(SoftwareSystem softwareSystem) {
        return C4Shape.RECTANGLE.getPlantumlShape();
    }

    @Override
    public String visitSoftwareSystemInstance(SoftwareSystemInstance softwareSystemInstance) {
        return C4Shape.RECTANGLE.getPlantumlShape();
    }
}
