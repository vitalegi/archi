package it.vitalegi.archi.workspaceloader.model;

import it.vitalegi.archi.style.Style;
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
    public LandscapeDiagramRaw(String name, String title, Style style) {
        super(name, title, style);
    }
    @Override
    public <E> E visit(DiagramRawVisitor<E> visitor) {
        return visitor.visitLandscapeDiagramRaw(this);
    }

}