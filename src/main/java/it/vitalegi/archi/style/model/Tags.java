package it.vitalegi.archi.style.model;

import it.vitalegi.archi.util.ListUtil;
import it.vitalegi.archi.util.MergeableCloneable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
@ToString
public class Tags implements MergeableCloneable<Tags> {

    List<ElementTag> elements;
    List<RelationTag> relations;

    public Tags() {
        elements = new ArrayList<>();
        relations = new ArrayList<>();
    }

    @Override
    public Tags duplicate() {
        return Tags.builder() //
                .elements(ListUtil.duplicate(this.elements)) //
                .relations(ListUtil.duplicate(this.relations)) //
                .build();
    }

    @Override
    public Tags merge(Tags other) {
        var out = this.duplicate();
        if (other == null) {
            return out;
        }
        out.elements = ListUtil.merge(out.elements, other.elements, Comparator.comparing(ElementTag::getAlias));
        out.relations = ListUtil.merge(out.relations, other.relations, Comparator.comparing(RelationTag::getAlias));
        return out;
    }
}
