package it.vitalegi.archi.model.element;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import it.vitalegi.archi.util.ListUtil;
import it.vitalegi.archi.util.MergeableCloneable;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@JsonDeserialize(using = PropertyEntriesDeserializer.class)
public class PropertyEntries implements MergeableCloneable<PropertyEntries> {
    List<PropertyEntry> properties;

    public PropertyEntries() {
        properties = new ArrayList<>();
    }

    public PropertyEntries(List<PropertyEntry> properties) {
        this.properties = properties;
    }

    @Override
    public PropertyEntries duplicate() {
        return new PropertyEntries(ListUtil.duplicate(properties));
    }

    @Override
    public PropertyEntries merge(PropertyEntries other) {
        return new PropertyEntries(ListUtil.merge(this.properties, other.properties, Comparator.comparing(PropertyEntry::getKey)));
    }
}
