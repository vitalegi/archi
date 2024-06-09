package it.vitalegi.archi.style.model;

import it.vitalegi.archi.util.ListUtil;
import it.vitalegi.archi.util.Mergeable;
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
public class Style implements Mergeable<Style> {
    List<SkinParam> skinParams;
    Tags tags;

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
}
