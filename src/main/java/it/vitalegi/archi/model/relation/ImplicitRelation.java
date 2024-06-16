package it.vitalegi.archi.model.relation;

import it.vitalegi.archi.model.Entity;
import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.element.ElementType;
import it.vitalegi.archi.util.Cloneable;
import it.vitalegi.archi.util.WorkspaceUtil;
import it.vitalegi.archi.visitor.RelationVisitor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ImplicitRelation extends Entity implements Cloneable<ImplicitRelation>, Relation {
    @EqualsAndHashCode.Exclude
    DirectRelation source;
    @EqualsAndHashCode.Exclude
    Element from;
    @EqualsAndHashCode.Exclude
    Element to;

    public ImplicitRelation(Model model) {
        super(model);
        super.setUniqueId(UUID.randomUUID().toString());
    }

    public ImplicitRelation(Model model, DirectRelation source, Element from, Element to) {
        super(model);

        var additionalId = WorkspaceUtil.createUniqueId(from) + "_" + WorkspaceUtil.createUniqueId(to);
        setId(additionalId + "_" + source.getId());
        setUniqueId(additionalId + "_" + source.getUniqueId());

        this.source = source;
        this.from = from;
        this.to = to;
    }

    @Override
    public ElementType getElementType() {
        return ElementType.IMPLICIT_RELATION;
    }

    @Override
    public ImplicitRelation duplicate() {
        var out = new ImplicitRelation(model);
        out.model = model;
        out.setId(getId());
        out.setUniqueId(getUniqueId());

        out.from = from;
        out.to = to;
        out.source = source.duplicate();
        return out;
    }

    @Override
    public <E> E visit(RelationVisitor<E> visitor) {
        return visitor.visitImplicitRelation(this);
    }
}
