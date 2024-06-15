package it.vitalegi.archi.exporter.c4.plantuml.builder;

import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.LandscapeDiagram;
import it.vitalegi.archi.model.diagramelement.C4DiagramElement;
import it.vitalegi.archi.model.diagramelement.C4DiagramModel;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.element.Group;
import it.vitalegi.archi.model.element.Person;
import it.vitalegi.archi.model.element.SoftwareSystem;
import it.vitalegi.archi.model.relation.DirectRelation;
import it.vitalegi.archi.model.relation.ImplicitRelation;
import it.vitalegi.archi.model.relation.Relation;
import it.vitalegi.archi.util.WorkspaceUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LandscapeDiagramModelBuilder extends C4ModelBuilder {
    Set<Element> elementsInScope;

    public LandscapeDiagramModelBuilder(Workspace workspace, LandscapeDiagram diagram) {
        super(workspace, diagram);
        elementsInScope = new HashSet<>();
    }

    @Override
    public C4DiagramModel build() {
        var softwareSystems = getAllSoftwareSystemsInScope();
        elementsInScope.addAll(softwareSystems);
        var people = getAllPeople();
        elementsInScope.addAll(people);
        var ancestorGroups = getAncestorGroups(softwareSystems);
        elementsInScope.addAll(ancestorGroups);

        var elements = new ArrayList<Element>();
        elements.addAll(softwareSystems);
        elements.addAll(people);

        var relations = new ArrayList<Relation>(getExplicitRelationsInScope(elements));
        if (useImplicitRelations()) {
            relations.addAll(getImplicitRelationsInScope(elements));
        }
        buildElements();
        buildRelations(relations);
        return model;
    }

    protected Set<DirectRelation> getExplicitRelationsInScope(List<Element> elements) {
        return elements.stream() //
                .flatMap(e -> workspace.getModel().getRelationManager().getDirect().getRelations(e).stream()) //
                .filter(r -> elementsInScope.contains(r.getFrom())) //
                .filter(r -> elementsInScope.contains(r.getTo())) //
                .collect(Collectors.toSet());
    }

    protected Set<ImplicitRelation> getImplicitRelationsInScope(List<Element> elements) {
        return elements.stream() //
                .flatMap(e -> workspace.getModel().getRelationManager().getImplicit().getRelations(e).stream()) //
                .filter(r -> elementsInScope.contains(r.getFrom())) //
                .filter(r -> elementsInScope.contains(r.getTo())) //
                .collect(Collectors.toSet());
    }


    protected SoftwareSystem findParentSoftwareSystem(Element child) {
        var path = WorkspaceUtil.getPathFromRoot(child);
        return WorkspaceUtil.getSoftwareSystems(path).stream().findFirst().orElse(null);
    }

    protected List<SoftwareSystem> getAllSoftwareSystemsInScope() {
        return WorkspaceUtil.getSoftwareSystems(workspace.getModel().getAllElements());
    }

    protected List<Person> getAllPeople() {
        return WorkspaceUtil.getPeople(workspace.getModel().getAllElements());
    }

    protected List<Group> getAncestorGroups(List<? extends Element> elements) {
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

    protected void buildRelations(List<Relation> relations) {
        relations.stream().flatMap(super::relation).forEach(model::addRelation);
    }
}
