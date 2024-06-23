package it.vitalegi.archi.exporter.c4.plantuml.builder;

import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.Diagram;
import it.vitalegi.archi.model.diagram.options.HiddenRelations;
import it.vitalegi.archi.model.diagramelement.C4DiagramElement;
import it.vitalegi.archi.model.diagramelement.C4DiagramModel;
import it.vitalegi.archi.model.diagramelement.C4DiagramRelation;
import it.vitalegi.archi.model.diagramelement.RelationType;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.relation.DirectRelation;
import it.vitalegi.archi.model.relation.ImplicitRelation;
import it.vitalegi.archi.model.relation.Relation;
import it.vitalegi.archi.model.relation.Relations;
import it.vitalegi.archi.util.BooleanUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
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
        var elementFactory = new DiagramElementFactoryVisitor(aliasGenerator, diagram);
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
        var visitor = new C4DiagramRelationFactoryVisitor(diagram, fromAlias, toAlias);
        return relation.visit(visitor);
    }

    protected Relations<?> getRelationsManager() {
        if (useImplicitRelations()) {
            return diagram.getModel().getRelationManager().getImplicit();
        }
        return diagram.getModel().getRelationManager().getDirect();
    }

    protected boolean useImplicitRelations() {
        return BooleanUtil.isTrue(diagram.getOptionsAggregated().getInheritRelations());
    }

    protected Comparator<C4DiagramRelation> relationComparator() {
        return Comparator.comparing(C4DiagramRelation::getFromAlias).thenComparing(C4DiagramRelation::getToAlias).thenComparing(C4DiagramRelation::getLabel);
    }

    protected Stream<Relation> removeDuplicatedInheritedRelations(Stream<? extends Relation> relations) {
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

    private Stream<Relation> _removeDuplicatedInheritedRelations(List<Relation> relations) {
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

    protected void buildHiddenRelations(Set<Element> elementsInScope) {
        var hiddenRelations = diagram.getOptionsAggregated().getHiddenRelations();
        if (hiddenRelations == null) {
            return;
        }
        hiddenRelations.stream().flatMap(r -> buildHiddenRelations(r, elementsInScope)).forEach(model::addRelation);
    }

    protected Stream<C4DiagramRelation> buildHiddenRelations(HiddenRelations hiddenRelations, Set<Element> elementsInScope) {
        var idsInScope = elementsInScope.stream().map(Element::getId).toList();
        var toBeConnected = hiddenRelations.getElements().stream().filter(idsInScope::contains).toList();
        if (toBeConnected.size() < 2) {
            return Stream.empty();
        }
        var out = new ArrayList<C4DiagramRelation>();
        var it = toBeConnected.iterator();
        var first = it.next();
        while (it.hasNext()) {
            var second = it.next();
            out.addAll(hiddenRelations(first, second).toList());
            first = second;
        }
        return out.stream();
    }

    protected Stream<C4DiagramRelation> hiddenRelations(String from, String to) {
        var fromElement = workspace.getModel().getElementById(from);
        var toElement = workspace.getModel().getElementById(to);
        var fromAliases = aliasGenerator.getConnectedAliases(fromElement);
        var toAliases = aliasGenerator.getConnectedAliases(toElement);
        if (fromAliases.size() != 1 || toAliases.size() != 1) {
            log.info("Diagram {}, building hidden relations from {} and {}, found {} and {} aliases. Don't add hidden relations.", diagram.getName(), from, to, fromAliases.size(), toAliases.size());
            return Stream.empty();
        }
        return Stream.of(hiddenRelation(fromAliases.get(0), toAliases.get(0)));
    }

    protected C4DiagramRelation hiddenRelation(String aliasFrom, String aliasTo) {
        var relation = new C4DiagramRelation();
        relation.setFromAlias(aliasFrom);
        relation.setToAlias(aliasTo);
        relation.setRelationType(RelationType.HIDDEN);
        return relation;
    }
}
