package it.vitalegi.archi.visitor;

import it.vitalegi.archi.model.relation.DirectRelation;
import it.vitalegi.archi.model.relation.ImplicitRelation;

public interface RelationVisitor<E> {
    E visitDirectRelation(DirectRelation relation);

    E visitImplicitRelation(ImplicitRelation relation);
}
