package it.vitalegi.archi.model.relation;

import it.vitalegi.archi.model.Entity;
import it.vitalegi.archi.model.element.ContainerInstance;
import it.vitalegi.archi.model.element.SoftwareSystem;
import it.vitalegi.archi.model.element.SoftwareSystemInstance;
import it.vitalegi.archi.util.WorkspaceUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Relations {
    protected List<Relation> relations = new ArrayList<>();
    protected Map<String, List<Relation>> relationsFrom = new HashMap<>();
    protected Map<String, List<Relation>> relationsTo = new HashMap<>();

    public void addRelation(Relation relation) {
        addRelation(relationsFrom, getId(relation.getFrom()), relation);
        addRelation(relationsTo, getId(relation.getTo()), relation);
        relations.add(relation);
    }

    public List<Relation> getAll() {
        return relations;
    }

    public List<Relation> getRelationsBetween(Entity entity1, Entity entity2) {
        return doGetRelationsBetween(entity1, entity2).collect(Collectors.toList());
    }

    protected Stream<Relation> doGetRelationsBetween(Entity entity1, Entity entity2) {
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

    public List<Relation> getRelationsFrom(Entity entity) {
        return getRelations(relationsFrom, entity);
    }

    public List<Relation> getRelationsTo(Entity entity) {
        return getRelations(relationsTo, entity);
    }

    public List<Relation> getImplicitsRelationsFrom(Entity entity) {
        return getImplicitRelations(relationsFrom, entity);
    }

    public List<Relation> getImplicitsRelationsTo(Entity entity) {
        return getImplicitRelations(relationsTo, entity);
    }

    protected void addRelation(Map<String, List<Relation>> relations, String key, Relation relation) {
        if (!relations.containsKey(key)) {
            relations.put(key, new ArrayList<>());
        }
        relations.get(key).add(relation);
    }

    protected List<Relation> getRelations(Map<String, List<Relation>> relations, Entity entity) {
        var out = relations.get(getId(entity));
        if (out != null) {
            return out;
        }
        return Collections.emptyList();
    }

    protected List<Relation> getImplicitRelations(Map<String, List<Relation>> relations, Entity entity) {
        if (WorkspaceUtil.isSoftwareSystem(entity)) {
            var out = new ArrayList<Relation>();
            var target = (SoftwareSystem) entity;
            // add all the containers' relations
            out.addAll(target.getContainers().stream().flatMap(c -> getRelations(relations, c).stream()).toList());
            // add all the containers' implicit relations
            out.addAll(target.getContainers().stream().flatMap(c -> getImplicitRelations(relations, c).stream()).toList());
            return out;
        }
        return Collections.emptyList();
    }

    protected String getId(Entity entity) {
        return entity.getUniqueId();
    }
}
