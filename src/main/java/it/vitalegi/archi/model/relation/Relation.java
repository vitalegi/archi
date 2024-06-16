package it.vitalegi.archi.model.relation;

import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.visitor.RelationVisitor;

public interface Relation {

    Element getFrom();

    Element getTo();

    void setFrom(Element element);

    void setTo(Element element);

    <E> E visit(RelationVisitor<E> visitor);
}
