package it.vitalegi.archi.exporter.c4.plantuml.builder;

import it.vitalegi.archi.model.diagram.Diagram;
import it.vitalegi.archi.model.diagramelement.C4DiagramRelation;
import it.vitalegi.archi.model.diagramelement.RelationType;
import it.vitalegi.archi.model.relation.DirectRelation;
import it.vitalegi.archi.model.relation.ImplicitRelation;
import it.vitalegi.archi.util.ModelPropertyUtil;
import it.vitalegi.archi.visitor.RelationVisitor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class C4DiagramRelationFactoryVisitor implements RelationVisitor<C4DiagramRelation> {

    Diagram diagram;
    String fromAlias;
    String toAlias;

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
        if (isRelationTextEnabled()) {
            target.setLabel(relation.getLabel());
            target.setDescription(relation.getDescription());
            target.setTechnologies(relation.getTechnologies());
            target.setProperties(ModelPropertyUtil.properties(relation.getProperties()));
            target.setLink(relation.getLink());
        }
        target.setRelationType(RelationType.NORMAL);
        target.setTags(relation.getTags());
        target.setSprite(relation.getSprite());
    }

    protected boolean isRelationTextEnabled() {
        return !diagram.getOptionsAggregated().isHideRelationsText();
    }
}
