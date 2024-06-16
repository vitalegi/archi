package it.vitalegi.archi.model.diagram;

import it.vitalegi.archi.exporter.c4.plantuml.constants.LayoutDirection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DiagramOptions {
    LayoutDirection direction;
    boolean inheritRelations;

    @Builder
    public DiagramOptions(boolean inheritRelations) {
        this.inheritRelations = inheritRelations;
    }

    public static DiagramOptions defaultOptions() {
        return builder().inheritRelations(false).build();
    }
}
