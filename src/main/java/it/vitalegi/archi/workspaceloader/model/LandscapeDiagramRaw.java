package it.vitalegi.archi.workspaceloader.model;

import it.vitalegi.archi.model.diagram.options.DiagramOptions;
import it.vitalegi.archi.model.style.Style;
import it.vitalegi.archi.visitor.DiagramRawVisitor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class LandscapeDiagramRaw extends DiagramRaw {
    @Builder
    public LandscapeDiagramRaw(String name, String title, Style style, DiagramOptions options) {
        super(name, title, style, options);
    }

    @Override
    public <E> E visit(DiagramRawVisitor<E> visitor) {
        return visitor.visitLandscapeDiagramRaw(this);
    }

}