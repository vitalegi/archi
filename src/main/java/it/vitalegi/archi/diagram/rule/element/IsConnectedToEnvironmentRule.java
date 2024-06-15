package it.vitalegi.archi.diagram.rule.element;

import it.vitalegi.archi.diagram.DiagramScope;
import it.vitalegi.archi.diagram.rule.AbstractVisibilityRule;
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
import it.vitalegi.archi.model.relation.DirectRelation;
import it.vitalegi.archi.util.WorkspaceUtil;
import it.vitalegi.archi.visitor.ElementVisitor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class IsConnectedToEnvironmentRule extends AbstractVisibilityRule implements ElementVisitor<Boolean> {

    String environment;

    public IsConnectedToEnvironmentRule(String environment) {
        this.environment = environment;
    }

    public boolean match(DiagramScope diagramScope, Element element) {
        if (element.visit(this)) {
            log.debug("Element {} is connected to environment {}, match.", element.toShortString(), environment);
            return true;
        }
        return false;
    }

    public boolean match(DiagramScope diagramScope, DirectRelation relation) {
        return false;
    }


    @Override
    public Boolean visitComponent(Component component) {
        return false;
    }

    @Override
    public Boolean visitContainer(Container container) {
        var deploymentEnvironment = container.getModel().findDeploymentEnvironmentById(environment);
        var children = deploymentEnvironment.getAllChildren().collect(Collectors.toList());
        var instances = WorkspaceUtil.getContainerInstances(children);
        return instances.stream().anyMatch(i -> i.getContainer().equals(container));
    }

    @Override
    public Boolean visitContainerInstance(ContainerInstance containerInstance) {
        return isChildOfEnvironment(containerInstance);
    }

    @Override
    public Boolean visitDeploymentEnvironment(DeploymentEnvironment deploymentEnvironment) {
        return false;
    }

    @Override
    public Boolean visitDeploymentNode(DeploymentNode deploymentNode) {
        return isChildOfEnvironment(deploymentNode);
    }

    @Override
    public Boolean visitGroup(Group group) {
        return isChildOfEnvironment(group);
    }

    @Override
    public Boolean visitInfrastructureNode(InfrastructureNode infrastructureNode) {
        return isChildOfEnvironment(infrastructureNode);
    }

    @Override
    public Boolean visitModel(Model model) {
        return false;
    }

    @Override
    public Boolean visitPerson(Person person) {
        return false;
    }

    @Override
    public Boolean visitSoftwareSystem(SoftwareSystem softwareSystem) {
        var deploymentEnvironment = softwareSystem.getModel().findDeploymentEnvironmentById(environment);
        var children = deploymentEnvironment.getAllChildren().collect(Collectors.toList());
        var instances = WorkspaceUtil.getSoftwareSystemInstances(children);
        return instances.stream().anyMatch(i -> i.getSoftwareSystem().equals(softwareSystem));
    }

    @Override
    public Boolean visitSoftwareSystemInstance(SoftwareSystemInstance softwareSystemInstance) {
        return false;
    }

    protected boolean isChildOfEnvironment(Element element) {
        var deploymentEnvironment = WorkspaceUtil.findDeploymentEnvironment(WorkspaceUtil.getPathFromRoot(element), environment);
        return deploymentEnvironment != null;
    }

    @Override
    public String toString() {
        return "is connected to environment " + environment;
    }
}
