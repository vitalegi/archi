package it.vitalegi.archi.exporter.c4.plantuml.builder;

import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.Diagram;
import it.vitalegi.archi.model.diagramelement.C4DiagramElement;
import it.vitalegi.archi.model.diagramelement.C4DiagramModel;
import it.vitalegi.archi.model.diagramelement.C4DiagramRelation;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.relation.DirectRelation;
import it.vitalegi.archi.model.relation.ImplicitRelation;
import it.vitalegi.archi.model.relation.Relation;
import it.vitalegi.archi.model.relation.Relations;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
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
        removeDuplicatedInheritedRelations(relations).flatMap(this::relation) //
                .sorted(Comparator.comparing(C4DiagramRelation::getFromAlias).thenComparing(C4DiagramRelation::getToAlias).thenComparing(C4DiagramRelation::getLabel)) //
                .forEach(model::addRelation);
    }

    protected Stream<Relation> removeDuplicatedInheritedRelations(Stream<Relation> relations) {
        var map = relations.collect(Collectors.toMap(r -> r.getFrom().getUniqueId() + "_" + r.getTo().getUniqueId(), r -> {
            var arr = new ArrayList<Relation>();
            arr.add(r);
            return arr;
        }, (oldList, newList) -> {
            oldList.addAll(newList);
            return oldList;
        }));
        return map.values().stream().flatMap(this::_removeDuplicatedInheritedRelations);
    }

    protected Stream<Relation> _removeDuplicatedInheritedRelations(List<Relation> relations) {
        if (relations.isEmpty()) {
            return Stream.empty();
        }
        var directRelations = relations.stream().filter(r -> r instanceof DirectRelation).toList();
        var implicitRelations = relations.stream().filter(r -> r instanceof ImplicitRelation).toList();
        if (!directRelations.isEmpty()) {
            return directRelations.stream();
        }
        if (!implicitRelations.isEmpty()) {
            return Stream.of(implicitRelations.get(0));
        }
        throw new RuntimeException("Cannot process " + relations);
    }
}
