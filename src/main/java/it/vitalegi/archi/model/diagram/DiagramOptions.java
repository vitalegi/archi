package it.vitalegi.archi.model.diagram;

import it.vitalegi.archi.exporter.c4.plantuml.constants.LayoutDirection;
import it.vitalegi.archi.util.MergeableCloneable;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DiagramOptions implements MergeableCloneable<DiagramOptions> {
    LayoutDirection direction;
    boolean inheritRelations;
    boolean hideRelationsText;

    @Builder
    public DiagramOptions(boolean inheritRelations, boolean hideRelationsText) {
        this.inheritRelations = inheritRelations;
        this.hideRelationsText = hideRelationsText;
    }

    public static DiagramOptions defaultOptions() {
        return builder().inheritRelations(false).hideRelationsText(false).build();
    }

    @Override
    public DiagramOptions duplicate() {
        var out = new DiagramOptions();
        out.direction = direction;
        out.inheritRelations = inheritRelations;
        out.hideRelationsText = hideRelationsText;
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
        out.inheritRelations = other.inheritRelations;
        out.hideRelationsText = other.hideRelationsText;
        return out;
    }
}
