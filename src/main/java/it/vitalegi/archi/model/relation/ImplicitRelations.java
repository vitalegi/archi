package it.vitalegi.archi.model.relation;

import it.vitalegi.archi.model.Entity;
import it.vitalegi.archi.model.element.ContainerInstance;
import it.vitalegi.archi.model.element.SoftwareSystemInstance;
import it.vitalegi.archi.util.WorkspaceUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImplicitRelations implements Relations<ImplicitRelation> {
    protected List<ImplicitRelation> relations = new ArrayList<>();
    protected Map<String, List<ImplicitRelation>> relationsFrom = new HashMap<>();
    protected Map<String, List<ImplicitRelation>> relationsTo = new HashMap<>();

    public void addRelation(DirectRelation relation) {
        buildImplicitRelations(relation).forEach(this::addImplicitRelation);
    }

    protected void addImplicitRelation(ImplicitRelation relation) {
        addRelation(relationsFrom, getId(relation.getFrom()), relation);
        addRelation(relationsTo, getId(relation.getTo()), relation);
        relations.add(relation);
    }

    @Override
    public List<ImplicitRelation> getAll() {
        return relations;
    }

    @Override
    public List<ImplicitRelation> getRelationsBetween(Entity entity1, Entity entity2) {
        return doGetRelationsBetween(entity1, entity2).collect(Collectors.toList());
    }

    protected Stream<ImplicitRelation> doGetRelationsBetween(Entity entity1, Entity entity2) {
        var direction1 = getRelationsFrom(entity1).stream().filter(r -> r.getTo().equals(entity2));
        var direction2 = getRelationsFrom(entity2).stream().filter(r -> r.getTo().equals(entity1));
        var concat = Stream.concat(direction1, direction2);
        if (WorkspaceUtil.isContainerInstance(entity1)) {
            return Stream.concat(concat, doGetRelationsBetween(((ContainerInstance) entity1).getContainer(), entity2));
        }
        if (WorkspaceUtil.isContainerInstance(entity2)) {
            return Stream.concat(concat, doGetRelationsBetween(entity1, ((ContainerInstance) entity2).getContainer()));
        }
        if (WorkspaceUtil.isSoftwareSystemInstance(entity1)) {
            return Stream.concat(concat, doGetRelationsBetween(((SoftwareSystemInstance) entity1).getSoftwareSystem(), entity2));
        }
        if (WorkspaceUtil.isSoftwareSystemInstance(entity2)) {
            return Stream.concat(concat, doGetRelationsBetween(entity1, ((SoftwareSystemInstance) entity2).getSoftwareSystem()));
        }
        return concat;
    }

    @Override
    public List<ImplicitRelation> getRelationsFrom(Entity entity) {
        return getRelations(relationsFrom, entity);
    }

    @Override
    public List<ImplicitRelation> getRelationsTo(Entity entity) {
        return getRelations(relationsTo, entity);
    }

    @Override
    public List<ImplicitRelation> getRelations(Entity entity) {
        var from = getRelationsFrom(entity);
        var to = getRelationsTo(entity);
        var out = new ArrayList<ImplicitRelation>();
        out.addAll(from);
        out.addAll(to);
        return out;
    }

    protected void addRelation(Map<String, List<ImplicitRelation>> relations, String key, ImplicitRelation relation) {
        if (!relations.containsKey(key)) {
            relations.put(key, new ArrayList<>());
        }
        relations.get(key).add(relation);
    }

    protected List<ImplicitRelation> getRelations(Map<String, List<ImplicitRelation>> relations, Entity entity) {
        var entries = relations.get(getId(entity));
        if (entries == null) {
            return Collections.emptyList();
        }
        return entries;
    }

    protected Stream<ImplicitRelation> buildImplicitRelations(DirectRelation relation) {
        var implicitBuilderVisitor = new GetSuitableElementsForImplicitRelationVisitor();
        var fromImplicit = relation.getFrom().visit(implicitBuilderVisitor);
        var toImplicit = relation.getTo().visit(implicitBuilderVisitor);
        return fromImplicit.stream().flatMap(from -> toImplicit.stream() //
                .map(to -> new ImplicitRelation(relation.getModel(), relation, from, to)));
    }

    protected String getId(Entity entity) {
        return entity.getUniqueId();
    }
}
