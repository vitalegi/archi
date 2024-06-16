package it.vitalegi.archi.exporter.c4.plantuml.builder;

import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.LandscapeDiagram;
import it.vitalegi.archi.model.diagram.SystemContextDiagram;
import it.vitalegi.archi.model.diagramelement.C4DiagramElement;
import it.vitalegi.archi.model.diagramelement.C4DiagramModel;
import it.vitalegi.archi.model.element.Container;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.element.Group;
import it.vitalegi.archi.model.element.Person;
import it.vitalegi.archi.model.element.SoftwareSystem;
import it.vitalegi.archi.model.relation.DirectRelation;
import it.vitalegi.archi.model.relation.ImplicitRelation;
import it.vitalegi.archi.model.relation.Relation;
import it.vitalegi.archi.model.relation.Relations;
import it.vitalegi.archi.util.WorkspaceUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SystemContextDiagramModelBuilder extends C4ModelBuilder<SystemContextDiagram> {
    Set<Element> elementsInScope;

    public SystemContextDiagramModelBuilder(Workspace workspace, SystemContextDiagram diagram) {
        super(workspace, diagram);
        elementsInScope = new HashSet<>();
    }

    @Override
    public C4DiagramModel build() {
        var targetSoftwareSystem = targetSoftwareSystem();
        var targetContainers = containers(targetSoftwareSystem);
        elementsInScope.add(targetSoftwareSystem);
        elementsInScope.addAll(targetContainers);

        var connectedPeople = elementsConnectedTo(elementsInScope).filter(WorkspaceUtil::isPerson).distinct().toList();
        var connectedSoftwareSystems = elementsConnectedTo(elementsInScope).filter(WorkspaceUtil::isSoftwareSystem).distinct().toList();
        elementsInScope.addAll(connectedPeople);
        elementsInScope.addAll(connectedSoftwareSystems);

        var ancestorGroups = getAncestorGroups(elementsInScope);
        elementsInScope.addAll(ancestorGroups);

        var relations = relationsInScope(elementsInScope);
        buildElements();
        buildRelations(relations);
        return model;
    }

    protected SoftwareSystem targetSoftwareSystem() {
        return (SoftwareSystem) diagram.getModel().getElementById(targetSoftwareSystemId());
    }

    protected String targetSoftwareSystemId() {
        return diagram.getTarget();
    }

    protected List<Container> containers(SoftwareSystem softwareSystem) {
        return softwareSystem.getAllChildren().filter(WorkspaceUtil::isContainer).map(WorkspaceUtil::toContainer).toList();
    }

    protected Stream<Element> elementsConnectedTo(Set<Element> elements) {
        var relations = getRelationsManager();
        var from = elements.stream().flatMap(e -> relations.getRelationsTo(e).stream().map(Relation::getFrom));
        var to = elements.stream().flatMap(e -> relations.getRelationsFrom(e).stream().map(Relation::getTo));
        return Stream.concat(from, to);
    }

    protected Stream<Relation> relationsInScope(Set<Element> elements) {
        var relations = getRelationsManager();
        var from = elements.stream().flatMap(e -> relations.getRelationsTo(e).stream()).filter(r -> elements.contains(r.getFrom()));
        var to = elements.stream().flatMap(e -> relations.getRelationsFrom(e).stream()).filter(r -> elements.contains(r.getTo()));
        return Stream.concat(from, to) //
                // skip if relation is directly connected to target software system
                .filter(r -> !r.getFrom().equals(targetSoftwareSystem())) //
                .filter(r -> !r.getTo().equals(targetSoftwareSystem())) //
                .distinct();
    }

    protected Relations<?> getRelationsManager() {
        if (useImplicitRelations()) {
            return diagram.getModel().getRelationManager().getImplicit();
        }
        return diagram.getModel().getRelationManager().getDirect();
    }


    protected SoftwareSystem findParentSoftwareSystem(Element child) {
        var path = WorkspaceUtil.getPathFromRoot(child);
        return WorkspaceUtil.getSoftwareSystems(path).stream().findFirst().orElse(null);
    }

    protected List<SoftwareSystem> getAllSoftwareSystemsInScope() {
        return WorkspaceUtil.getSoftwareSystems(workspace.getModel().getAllElements());
    }

    protected List<Group> getAncestorGroups(Set<? extends Element> elements) {
        var groups = new ArrayList<Group>();
        for (var element : elements) {
            var ancestors = WorkspaceUtil.getPathFromRoot(element);
            var ancestorGroups = WorkspaceUtil.getGroups(ancestors);
            groups.addAll(ancestorGroups);
        }
        return groups.stream().distinct().collect(Collectors.toList());
    }

    protected boolean useImplicitRelations() {
        return diagram.getOptions().isInheritRelations();
    }

    protected void buildElements() {
        var topLevelElements = diagram.getModel().getElements();
        for (var element : topLevelElements) {
            buildElement(null, element);
        }
    }

    protected void buildElement(C4DiagramElement parent, Element element) {
        if (elementsInScope.contains(element)) {
            var e = addElement(parent, element);
            for (var child : element.getElements()) {
                buildElement(e, child);
            }
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
