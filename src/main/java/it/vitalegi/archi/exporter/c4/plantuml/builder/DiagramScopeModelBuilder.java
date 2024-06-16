package it.vitalegi.archi.exporter.c4.plantuml.builder;

import it.vitalegi.archi.diagram.DiagramScope;
import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.Diagram;
import it.vitalegi.archi.model.diagramelement.C4DiagramElement;
import it.vitalegi.archi.model.diagramelement.C4DiagramModel;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.relation.DirectRelation;

import java.util.List;

public class DiagramScopeModelBuilder extends C4ModelBuilder<Diagram> {
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
        buildRelations(diagram.getModel().getRelationManager().getDirect().getAll());
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

    protected void buildRelations(List<DirectRelation> relations) {
        for (var relation : relations) {
            buildRelation(relation);
        }
    }

    protected void buildRelation(DirectRelation relation) {
        if (diagramScope.isInScope(relation)) {
            relation(relation).forEach(model::addRelation);
        }
    }
}
