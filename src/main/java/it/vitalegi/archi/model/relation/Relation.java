package it.vitalegi.archi.model.relation;

import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.visitor.RelationVisitor;

public interface Relation {

    Element getFrom();

    void setFrom(Element element);

    Element getTo();

    void setTo(Element element);

    <E> E visit(RelationVisitor<E> visitor);
}
