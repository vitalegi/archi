package it.vitalegi.archi.style.model;

import it.vitalegi.archi.util.MergeableCloneable;
import it.vitalegi.archi.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class RelationTag implements MergeableCloneable<RelationTag> {
    String alias;

    @Override
    public RelationTag duplicate() {
        var out = new RelationTag();
        out.alias = this.alias;
        return out;
    }

    @Override
    public RelationTag merge(RelationTag other) {
        var out = this.duplicate();
        out.alias = ObjectUtil.getFirstNotNull(this.alias, other.alias);
        return out;
    }
}
