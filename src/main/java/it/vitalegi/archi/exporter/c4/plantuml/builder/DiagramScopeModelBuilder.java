package it.vitalegi.archi.exporter.c4.plantuml.builder;

import it.vitalegi.archi.diagram.DiagramScope;
import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.Diagram;
import it.vitalegi.archi.model.diagramelement.C4DiagramElement;
import it.vitalegi.archi.model.diagramelement.C4DiagramModel;
import it.vitalegi.archi.model.diagramelement.C4DiagramRelation;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.relation.Relation;

import java.util.List;
import java.util.stream.Stream;

public class DiagramScopeModelBuilder extends C4ModelBuilder {
    DiagramScope diagramScope;

    public DiagramScopeModelBuilder(Workspace workspace, Diagram diagram, DiagramScope diagramScope) {
        super(workspace, diagram);
        this.diagramScope = diagramScope;
    }

    public C4DiagramModel build() {
        var topLevelElements = diagram.getModel().getElements();
        for (var element : topLevelElements) {
            buildElement(null, element);
        }
        buildRelations(diagram.getModel().getRelations().getAll());
        return model;
    }

    protected void buildElement(C4DiagramElement parent, Element element) {
        if (diagramScope.isInScope(element)) {
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
        for (var relation : relations) {
            buildRelation(relation);
        }
    }

    protected void buildRelation(Relation relation) {
        if (diagramScope.isInScope(relation)) {
            relation(relation).forEach(model::addRelation);
        }
    }
}
