package it.vitalegi.archi.model.element;

import it.vitalegi.archi.util.MergeableCloneable;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PropertyEntry implements MergeableCloneable<PropertyEntry> {
    String key;
    String value;

    @Builder
    public PropertyEntry(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public PropertyEntry duplicate() {
        return new PropertyEntry(key, value);
    }

    @Override
    public PropertyEntry merge(PropertyEntry other) {
        return new PropertyEntry(other.key, other.value);
    }
}
