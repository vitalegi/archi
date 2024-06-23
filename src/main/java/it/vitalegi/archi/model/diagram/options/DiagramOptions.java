package it.vitalegi.archi.model.diagram.options;

import it.vitalegi.archi.exporter.c4.plantuml.constants.LayoutDirection;
import it.vitalegi.archi.util.ListUtil;
import it.vitalegi.archi.util.MergeableCloneable;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
public class DiagramOptions implements MergeableCloneable<DiagramOptions> {
    LayoutDirection direction;
    Boolean inheritRelations;
    Boolean hideRelationsText;
    List<HiddenRelations> hiddenRelations;

    public static DiagramOptions merge(DiagramOptions option1, DiagramOptions option2) {
        if (option1 == null && option2 == null) {
            return null;
        }
        if (option1 != null && option2 != null) {
            return option1.merge(option2);
        }
        if (option1 != null) {
            return option1;
        }
        return option2;
    }

    public DiagramOptions() {
        hiddenRelations = new ArrayList<>();
    }

    @Builder
    public DiagramOptions(Boolean inheritRelations, Boolean hideRelationsText) {
        this();
        this.inheritRelations = inheritRelations;
        this.hideRelationsText = hideRelationsText;
    }

    public static DiagramOptions defaultOptions() {
        return builder().inheritRelations(false).hideRelationsText(false).build();
    }

    @Override
    public DiagramOptions duplicate() {
        var out = new DiagramOptions();
        out.direction = this.direction;
        out.inheritRelations = this.inheritRelations;
        out.hideRelationsText = this.hideRelationsText;
        out.hiddenRelations = ListUtil.duplicate(this.hiddenRelations);
        return out;
    }

    @Override
    public DiagramOptions merge(DiagramOptions other) {
        var out = this.duplicate();
        if (other == null) {
            return out;
        }
        if (other.direction != null) {
            out.direction = other.direction;
        }
        if (other.inheritRelations != null) {
            out.inheritRelations = other.inheritRelations;
        }
        if (other.hideRelationsText != null) {
            out.hideRelationsText = other.hideRelationsText;
        }
        out.hiddenRelations = ListUtil.merge(this.hiddenRelations, other.hiddenRelations, Comparator.comparing(HiddenRelations::getId));
        return out;
    }
}
