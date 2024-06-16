package it.vitalegi.archi.exporter.c4.plantuml.builder;

import it.vitalegi.archi.model.Entity;
import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.DeploymentDiagram;
import it.vitalegi.archi.model.diagramelement.C4DiagramElement;
import it.vitalegi.archi.model.diagramelement.C4DiagramModel;
import it.vitalegi.archi.model.element.Container;
import it.vitalegi.archi.model.element.ContainerInstance;
import it.vitalegi.archi.model.element.DeploymentNode;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.element.SoftwareSystem;
import it.vitalegi.archi.model.element.SoftwareSystemInstance;
import it.vitalegi.archi.model.relation.Relation;
import it.vitalegi.archi.model.relation.Relations;
import it.vitalegi.archi.util.WorkspaceUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DeploymentDiagramModelBuilder extends C4ModelBuilder<DeploymentDiagram> {
    Set<Element> elementsInScope;

    public DeploymentDiagramModelBuilder(Workspace workspace, DeploymentDiagram diagram) {
        super(workspace, diagram);
        elementsInScope = new HashSet<>();
    }

    @Override
    public C4DiagramModel build() {
        elementsInScope.addAll(targetSoftwareSystems());
        elementsInScope.addAll(targetSoftwareSystemInstances());
        elementsInScope.addAll(targetContainers());
        elementsInScope.addAll(targetContainerInstances());

        var connectedElements = connectedElements(elementsInScope).distinct().toList();
        elementsInScope.addAll(connectedElements);

        var ancestors = getAncestorDeploymentNodes(elementsInScope);
        elementsInScope.addAll(ancestors);

        var relations = relationsInScope(elementsInScope);
        buildElements();
        buildRelations(relations);
        return model;
    }

    protected Set<SoftwareSystem> targetSoftwareSystems() {
        return targetSoftwareSystemInstances().stream().map(SoftwareSystemInstance::getSoftwareSystem).collect(Collectors.toSet());
    }

    protected Set<SoftwareSystemInstance> targetSoftwareSystemInstances() {
        var all = allSoftwareSystemInstances();
        if (diagram.isScopeAll()) {
            return new HashSet<>(all);
        }
        var target = diagram.getScope();
        return all.stream().filter(s -> Entity.equals(target, s.getSoftwareSystemId())) //
                .collect(Collectors.toSet());
    }

    protected Set<Container> targetContainers() {
        return targetContainerInstances().stream().map(ContainerInstance::getContainer).collect(Collectors.toSet());
    }

    protected Set<ContainerInstance> targetContainerInstances() {
        var all = allContainerInstances();
        if (diagram.isScopeAll()) {
            return new HashSet<>(all);
        }
        var target = diagram.getScope();
        return all.stream().filter(c -> Entity.equals(target, c.getContainer().getSoftwareSystem().getId())) //
                .collect(Collectors.toSet());
    }

    protected List<SoftwareSystemInstance> allSoftwareSystemInstances() {
        return WorkspaceUtil.getSoftwareSystemInstances(getEnvironmentElements().toList());
    }

    protected List<ContainerInstance> allContainerInstances() {
        return WorkspaceUtil.getContainerInstances(getEnvironmentElements().toList());
    }

    protected Stream<Element> getEnvironmentElements() {
        return diagram.deploymentEnvironment().getAllChildren();
    }

    protected Stream<Element> connectedElements(Set<Element> elements) {
        var relations = getRelationsManager();
        var from = elements.stream().flatMap(e -> relations.getRelationsTo(e).stream().map(Relation::getFrom));
        var to = elements.stream().flatMap(e -> relations.getRelationsFrom(e).stream().map(Relation::getTo));
        var connectedElementsVisitor = new DeploymentConnectedElementsVisitor(diagram);
        return Stream.concat(from, to).flatMap(e -> e.visit(connectedElementsVisitor));
    }

    protected Stream<Relation> relationsInScope(Set<Element> elements) {
        var relations = getRelationsManager();
        var from = elements.stream().flatMap(e -> relations.getRelationsTo(e).stream()).filter(r -> elements.contains(r.getFrom()));
        var to = elements.stream().flatMap(e -> relations.getRelationsFrom(e).stream()).filter(r -> elements.contains(r.getTo()));
        return Stream.concat(from, to).distinct();
    }

    protected Relations<?> getRelationsManager() {
        if (useImplicitRelations()) {
            return diagram.getModel().getRelationManager().getImplicit();
        }
        return diagram.getModel().getRelationManager().getDirect();
    }

    protected List<DeploymentNode> getAncestorDeploymentNodes(Set<? extends Element> elements) {
        var target = new ArrayList<DeploymentNode>();
        for (var element : elements) {
            var ancestors = WorkspaceUtil.getPathFromRoot(element);
            var ancestorGroups = WorkspaceUtil.getDeploymentNodes(ancestors);
            target.addAll(ancestorGroups);
        }
        return target.stream().distinct().collect(Collectors.toList());
    }

    protected boolean useImplicitRelations() {
        return diagram.getOptions().isInheritRelations();
    }

    protected void buildElements() {
        var topLevelElements = diagram.deploymentEnvironment().getElements();
        for (var element : topLevelElements) {
            buildElement(null, element);
        }
    }

    protected void buildElement(C4DiagramElement parent, Element element) {
        if (elementsInScope.contains(element)) {
            var e = addElement(parent, element);
            buildElementChildren(e, element);
        } else {
            buildElementChildren(parent, element);
        }
    }

    protected void buildElementChildren(C4DiagramElement parent, Element element) {
        if (WorkspaceUtil.isContainerInstance(element)) {
            buildElement(parent, WorkspaceUtil.toContainerInstance(element).getContainer());
        } else if (WorkspaceUtil.isSoftwareSystemInstance(element)) {
            buildElement(parent, WorkspaceUtil.toSoftwareSystemInstance(element).getSoftwareSystem());
        } else {
            for (var child : element.getElements()) {
                buildElement(parent, child);
            }
        }
    }

    protected void buildRelations(Stream<Relation> relations) {
        relations.flatMap(super::relation).forEach(model::addRelation);
    }
}
