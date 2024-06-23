package it.vitalegi.archi.exporter.c4.plantuml.builder;

import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.LandscapeDiagram;
import it.vitalegi.archi.model.diagramelement.C4DiagramElement;
import it.vitalegi.archi.model.diagramelement.C4DiagramModel;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.element.Group;
import it.vitalegi.archi.model.element.Person;
import it.vitalegi.archi.model.element.SoftwareSystem;
import it.vitalegi.archi.model.relation.Relation;
import it.vitalegi.archi.util.WorkspaceUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LandscapeDiagramModelBuilder extends C4ModelBuilder<LandscapeDiagram> {
    Set<Element> elementsInScope;

    public LandscapeDiagramModelBuilder(Workspace workspace, LandscapeDiagram diagram) {
        super(workspace, diagram);
        elementsInScope = new HashSet<>();
    }

    @Override
    public C4DiagramModel build() {
        elementsInScope.addAll(getAllSoftwareSystemsInScope());
        elementsInScope.addAll(getAllPeople());
        elementsInScope.addAll(getAncestorGroups(elementsInScope));

        var relations = relationsInScope(elementsInScope);
        var list = relations.toList();
        buildElements();
        buildRelations(list.stream());
        buildHiddenRelations(elementsInScope);
        return model;
    }

    protected Stream<Relation> relationsInScope(Set<Element> elements) {
        var relations = getRelationsManager();
        var from = elements.stream().flatMap(e -> relations.getRelationsTo(e).stream()).filter(r -> elements.contains(r.getFrom()));
        var to = elements.stream().flatMap(e -> relations.getRelationsFrom(e).stream()).filter(r -> elements.contains(r.getTo()));
        return Stream.concat(from, to).distinct();
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

    protected List<Group> getAncestorGroups(Set<? extends Element> elements) {
        var groups = new ArrayList<Group>();
        for (var element : elements) {
            var ancestors = WorkspaceUtil.getPathFromRoot(element);
            var ancestorGroups = WorkspaceUtil.getGroups(ancestors);
            groups.addAll(ancestorGroups);
        }
        return groups.stream().distinct().collect(Collectors.toList());
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
            buildElementChildren(e, element);
        } else {
            buildElementChildren(parent, element);
        }
    }

    protected void buildElementChildren(C4DiagramElement parent, Element element) {
        for (var child : element.getElements()) {
            buildElement(parent, child);
        }
    }

    protected void buildRelations(Stream<Relation> relations) {
        removeDuplicatedInheritedRelations(relations).flatMap(this::relation) //
                .sorted(relationComparator()) //
                .forEach(model::addRelation);
    }
}
