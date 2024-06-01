package it.vitalegi.archi.exception;

import it.vitalegi.archi.model.Relation;

public class RelationNotAllowedException extends Error {
    Relation relation;

    public RelationNotAllowedException(Relation relation) {
        super(format(relation) + ", " + relation);
        this.relation = relation;
    }

    public RelationNotAllowedException(String message, Relation relation) {
        super(format(relation) + ": " + message + ", " + relation);
        this.relation = relation;
    }

    protected static String format(Relation relation) {
        return "Relation from " + relation.getFrom().toShortString() + " to " + relation.getTo().toShortString() + " is not allowed";
    }
}