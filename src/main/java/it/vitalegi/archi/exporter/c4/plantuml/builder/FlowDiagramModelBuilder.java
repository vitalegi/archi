package it.vitalegi.archi.exporter.c4.plantuml.builder;

import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.FlowDiagram;
import it.vitalegi.archi.model.diagramelement.C4DiagramElement;
import it.vitalegi.archi.model.diagramelement.C4DiagramModel;
import it.vitalegi.archi.model.element.Container;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.element.SoftwareSystem;
import it.vitalegi.archi.model.flow.Flow;
import it.vitalegi.archi.model.relation.Relation;
import it.vitalegi.archi.util.WorkspaceUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class FlowDiagramModelBuilder extends C4ModelBuilder<FlowDiagram> {
    Set<Element> elementsInScope;

    public FlowDiagramModelBuilder(Workspace workspace, FlowDiagram diagram) {
        super(workspace, diagram);
        elementsInScope = new HashSet<>();
    }

    @Override
    public C4DiagramModel build() {
        elementsInScope.addAll(targetElements());
        elementsInScope.addAll(ancestors(elementsInScope));

        var relations = relationsInScope(elementsInScope);
        buildElements();
        buildRelations(relations);
        buildHiddenRelations(elementsInScope);
        return model;
    }

    protected List<Element> targetElements() {
        var flow = flow();
        return flow.getSteps().stream().flatMap(step -> Stream.of(step.getFrom(), step.getTo())).toList();
    }

    protected Flow flow() {
        return diagram.getModel().findFlowById(diagram.getFlow());
    }


    protected Stream<? extends Relation> relationsInScope(Set<Element> elements) {
        var flow = flow();
        return flow.getSteps().stream() //
                .filter(s -> elements.contains(s.getFrom())) //
                .filter(s -> elements.contains(s.getTo()));
    }

    protected List<Element> ancestors(Set<? extends Element> elements) {
        var ancestors = new ArrayList<Element>();
        for (var element : elements) {
            var pathFromRoot = WorkspaceUtil.getPathFromRoot(element);
            ancestors.addAll(pathFromRoot);
        }
        return ancestors;
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
    protected void buildRelations(Stream<? extends Relation> relations) {
        relations.flatMap(this::relation) //
                .sorted(relationComparator()) //
                .forEach(model::addRelation);
    }
}
