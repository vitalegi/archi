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

public class DirectRelations implements Relations<DirectRelation> {
    protected List<DirectRelation> relations = new ArrayList<>();
    protected Map<String, List<DirectRelation>> relationsFrom = new HashMap<>();
    protected Map<String, List<DirectRelation>> relationsTo = new HashMap<>();

    public void addRelation(DirectRelation relation) {
        addRelation(relationsFrom, getId(relation.getFrom()), relation);
        addRelation(relationsTo, getId(relation.getTo()), relation);
        relations.add(relation);
    }

    @Override
    public List<DirectRelation> getAll() {
        return relations;
    }

    @Override
    public List<DirectRelation> getRelationsBetween(Entity entity1, Entity entity2) {
        return doGetRelationsBetween(entity1, entity2).collect(Collectors.toList());
    }

    protected Stream<DirectRelation> doGetRelationsBetween(Entity entity1, Entity entity2) {
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
    public List<DirectRelation> getRelations(Entity entity) {
        var from = getRelationsFrom(entity);
        var to = getRelationsTo(entity);
        var out = new ArrayList<DirectRelation>();
        out.addAll(from);
        out.addAll(to);
        return out;
    }

    @Override
    public List<DirectRelation> getRelationsFrom(Entity entity) {
        return getRelations(relationsFrom, entity);
    }

    @Override
    public List<DirectRelation> getRelationsTo(Entity entity) {
        return getRelations(relationsTo, entity);
    }

    protected void addRelation(Map<String, List<DirectRelation>> relations, String key, DirectRelation relation) {
        if (!relations.containsKey(key)) {
            relations.put(key, new ArrayList<>());
        }
        relations.get(key).add(relation);
    }

    protected List<DirectRelation> getRelations(Map<String, List<DirectRelation>> relations, Entity entity) {
        var out = relations.get(getId(entity));
        if (out != null) {
            return out;
        }
        return Collections.emptyList();
    }

    protected String getId(Entity entity) {
        return entity.getUniqueId();
    }
}
