package it.vitalegi.archi.model.diagram.options;

import it.vitalegi.archi.util.MergeableCloneable;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class HiddenRelations implements MergeableCloneable<HiddenRelations> {
    String id;
    List<String> elements;

    public HiddenRelations() {
        elements = new ArrayList<>();
    }

    @Override
    public HiddenRelations duplicate() {
        var out = new HiddenRelations();
        out.id = this.id;
        if (elements != null) {
            out.elements.addAll(this.elements);
        }
        return out;
    }

    @Override
    public HiddenRelations merge(HiddenRelations other) {
        var out = this.duplicate();
        if (other == null) {
            return out;
        }
        if (out.elements != null) {
            out.elements.addAll(other.elements);
            out.elements = out.elements.stream().distinct().collect(Collectors.toList());
        }
        return out;
    }
}
