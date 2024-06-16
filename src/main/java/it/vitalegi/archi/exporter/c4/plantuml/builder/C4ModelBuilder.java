package it.vitalegi.archi.exporter.c4.plantuml.builder;

import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.Diagram;
import it.vitalegi.archi.model.diagramelement.C4DiagramElement;
import it.vitalegi.archi.model.diagramelement.C4DiagramModel;
import it.vitalegi.archi.model.diagramelement.C4DiagramRelation;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.relation.Relation;
import it.vitalegi.archi.model.relation.Relations;

import java.util.Comparator;
import java.util.stream.Stream;

public abstract class C4ModelBuilder<E extends Diagram> {
    AliasGenerator aliasGenerator;
    Workspace workspace;
    E diagram;
    C4DiagramModel model;

    public C4ModelBuilder(Workspace workspace, E diagram) {
        this.workspace = workspace;
        this.diagram = diagram;
        aliasGenerator = new AliasGenerator();
        model = new C4DiagramModel();
    }

    public abstract C4DiagramModel build();


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
        var visitor = new C4DiagramRelationFactoryVisitor(fromAlias, toAlias);
        return relation.visit(visitor);
    }

    protected Relations<?> getRelationsManager() {
        if (useImplicitRelations()) {
            return diagram.getModel().getRelationManager().getImplicit();
        }
        return diagram.getModel().getRelationManager().getDirect();
    }

    protected boolean useImplicitRelations() {
        return diagram.getOptions().isInheritRelations();
    }

    protected void buildRelations(Stream<Relation> relations) {
        relations.flatMap(this::relation) //
                .sorted(Comparator.comparing(C4DiagramRelation::getFromAlias).thenComparing(C4DiagramRelation::getToAlias).thenComparing(C4DiagramRelation::getLabel)) //
                .forEach(model::addRelation);
    }
}
