package it.vitalegi.archi.exporter.c4.plantuml.builder;

import it.vitalegi.archi.model.diagramelement.C4DiagramRelation;
import it.vitalegi.archi.model.relation.DirectRelation;
import it.vitalegi.archi.model.relation.ImplicitRelation;
import it.vitalegi.archi.visitor.RelationVisitor;

public class C4DiagramRelationFactoryVisitor implements RelationVisitor<C4DiagramRelation> {

    String fromAlias;
    String toAlias;

    public C4DiagramRelationFactoryVisitor(String fromAlias, String toAlias) {
        this.fromAlias = fromAlias;
        this.toAlias = toAlias;
    }

    @Override
    public C4DiagramRelation visitDirectRelation(DirectRelation relation) {
        var out = new C4DiagramRelation();
        out.setFromAlias(fromAlias);
        out.setToAlias(toAlias);
        applyProperties(out, relation);
        return out;
    }

    @Override
    public C4DiagramRelation visitImplicitRelation(ImplicitRelation relation) {
        var out = new C4DiagramRelation();
        out.setFromAlias(fromAlias);
        out.setToAlias(toAlias);
        applyProperties(out, relation.getSource());
        return out;
    }

    protected void applyProperties(C4DiagramRelation target, DirectRelation relation) {
        target.setDescription(relation.getDescription());
        target.setTags(relation.getTags());
        target.setLink(relation.getLink());
        target.setLabel(relation.getLabel());
        target.setSprite(relation.getSprite());
        target.setTechnologies(relation.getTechnologies());
    }

}
