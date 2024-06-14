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

public class C4ModelBuilder {
    AliasGenerator aliasGenerator;

    Workspace workspace;
    Diagram diagram;
    DiagramScope diagramScope;
    C4DiagramModel model;

    public C4ModelBuilder(Workspace workspace, Diagram diagram, DiagramScope diagramScope) {
        this.workspace = workspace;
        this.diagram = diagram;
        this.diagramScope = diagramScope;
        aliasGenerator = new AliasGenerator();
        model = new C4DiagramModel();
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

    protected C4DiagramElement addElement(C4DiagramElement parent, Element element) {
        var elementFactory = new DiagramElementFactoryVisitor(aliasGenerator);
        var e = element.visit(elementFactory);
        if (parent != null) {
            model.addElement(parent.getId(), e);
        } else {
            model.addElement(null, e);
        }
        return e;
    }

    protected Stream<C4DiagramRelation> relation(Relation relation) {
        var from = aliasGenerator.getConnectedAliases(relation.getFrom());
        var to = aliasGenerator.getConnectedAliases(relation.getTo());
        return from.stream().flatMap(fromAlias -> to.stream() //
                .map(toAlias -> relation(relation, fromAlias, toAlias)));
    }

    protected C4DiagramRelation relation(Relation relation, String fromAlias, String toAlias) {
        var out = new C4DiagramRelation();
        out.setFromAlias(fromAlias);
        out.setToAlias(toAlias);
        out.setDescription(relation.getDescription());
        out.setTags(relation.getTags());
        out.setLink(relation.getLink());
        out.setLabel(relation.getLabel());
        out.setSprite(relation.getSprite());
        out.setTechnology(relation.getTechnologies());
        return out;
    }
}
