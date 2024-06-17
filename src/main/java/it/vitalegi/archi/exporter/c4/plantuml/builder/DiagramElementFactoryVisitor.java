package it.vitalegi.archi.exporter.c4.plantuml.builder;

import it.vitalegi.archi.exporter.c4.plantuml.writer.PlantumlShapeElementVisitor;
import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.model.diagram.Diagram;
import it.vitalegi.archi.model.diagramelement.C4DiagramElement;
import it.vitalegi.archi.model.diagramelement.C4DiagramElementProperty;
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
import it.vitalegi.archi.util.ModelPropertyUtil;
import it.vitalegi.archi.visitor.ElementVisitor;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DiagramElementFactoryVisitor implements ElementVisitor<C4DiagramElement> {

    AliasGenerator aliasGenerator;
    Diagram diagram;

    @Override
    public C4DiagramElement visitComponent(Component component) {
        return diagramElement(component);
    }

    @Override
    public C4DiagramElement visitContainer(Container container) {
        return diagramElement(container);
    }

    @Override
    public C4DiagramElement visitContainerInstance(ContainerInstance containerInstance) {
        return diagramElement(containerInstance);

    }

    @Override
    public C4DiagramElement visitDeploymentEnvironment(DeploymentEnvironment deploymentEnvironment) {
        return diagramElement(deploymentEnvironment);
    }

    @Override
    public C4DiagramElement visitDeploymentNode(DeploymentNode deploymentNode) {
        return diagramElement(deploymentNode);
    }

    @Override
    public C4DiagramElement visitGroup(Group group) {
        return diagramElement(group);
    }

    @Override
    public C4DiagramElement visitInfrastructureNode(InfrastructureNode infrastructureNode) {
        return diagramElement(infrastructureNode);
    }

    @Override
    public C4DiagramElement visitModel(Model model) {
        throw new IllegalArgumentException("Model is not allowed in any diagram");
    }

    @Override
    public C4DiagramElement visitPerson(Person person) {
        return diagramElement(person);
    }

    @Override
    public C4DiagramElement visitSoftwareSystem(SoftwareSystem softwareSystem) {
        return diagramElement(softwareSystem);
    }

    @Override
    public C4DiagramElement visitSoftwareSystemInstance(SoftwareSystemInstance softwareSystemInstance) {
        return diagramElement(softwareSystemInstance);
    }

    protected C4DiagramElement diagramElement(Element element) {
        var out = new C4DiagramElement();
        out.setId(aliasGenerator.generateAlias(element));
        out.setName(element.getName());
        out.setDescription(element.getDescription());
        out.setTags(element.getTags());
        out.setTechnologies(element.getTechnologies());
        out.setProperties(ModelPropertyUtil.properties(element.getMetadata()));

        var shape = new PlantumlShapeElementVisitor();
        out.setShape(element.visit(shape));
        return out;
    }
}
