package it.vitalegi.archi.model.style;

import it.vitalegi.archi.util.ListUtil;
import it.vitalegi.archi.util.MergeableCloneable;
import it.vitalegi.archi.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Comparator;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Style implements MergeableCloneable<Style> {
    List<SkinParam> skinParams;
    Tags tags;

    public static Style merge(Style style1, Style style2) {
        if (style1 == null && style2 == null) {
            return null;
        }
        if (style1 != null && style2 != null) {
            return style1.merge(style2);
        }
        if (style1 != null) {
            return style1;
        }
        return style2;
    }

    public void validate() {

    }

    @Override
    public Style merge(Style other) {
        var out = new Style();
        if (other == null) {
            other = new Style();
        }
        out.setSkinParams(ListUtil.merge(this.getSkinParams(), other.getSkinParams(), Comparator.comparing(SkinParam::getKey)));
        out.setTags(ObjectUtil.safeMerge(tags, other.tags));
        return out;
    }

    @Override
    public Style duplicate() {
        var out = new Style();
        if (skinParams != null) {
            out.setSkinParams(ListUtil.duplicate(skinParams));
        }
        if (tags != null) {
            out.setTags(tags.duplicate());
        }
        return out;
    }
}
