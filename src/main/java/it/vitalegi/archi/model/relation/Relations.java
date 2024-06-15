package it.vitalegi.archi.model.relation;

import it.vitalegi.archi.model.Entity;

import java.util.List;

public interface Relations<E extends Relation> {

    List<E> getAll();

    List<E> getRelationsBetween(Entity entity1, Entity entity2);

    List<E> getRelations(Entity entity);

    List<E> getRelationsFrom(Entity entity);

    List<E> getRelationsTo(Entity entity);
}
