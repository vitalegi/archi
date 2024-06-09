package it.vitalegi.archi.model.style;

import it.vitalegi.archi.util.MergeableCloneable;
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
public class SkinParam implements MergeableCloneable<SkinParam> {
    String key;
    String value;

    @Override
    public SkinParam duplicate() {
        return SkinParam.builder().key(key).value(value).build();
    }

    @Override
    public SkinParam merge(SkinParam other) {
        var out = this.duplicate();
        out.setValue(other.getValue());
        return out;
    }
}
