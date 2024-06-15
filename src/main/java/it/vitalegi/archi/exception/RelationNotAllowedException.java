package it.vitalegi.archi.exception;

import it.vitalegi.archi.model.relation.DirectRelation;

public class RelationNotAllowedException extends Error {
    DirectRelation relation;

    public RelationNotAllowedException(DirectRelation relation) {
        super(format(relation) + ", " + relation);
        this.relation = relation;
    }

    public RelationNotAllowedException(String message, DirectRelation relation) {
        super(format(relation) + ": " + message + ", " + relation);
        this.relation = relation;
    }

    protected static String format(DirectRelation relation) {
        return "Relation from " + relation.getFrom().toShortString() + " to " + relation.getTo().toShortString() + " is not allowed";
    }
}