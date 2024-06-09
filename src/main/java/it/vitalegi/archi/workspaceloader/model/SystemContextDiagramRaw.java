package it.vitalegi.archi.workspaceloader.model;

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
public class SystemContextDiagramRaw extends DiagramRaw {
    String target;

    @Builder
    public SystemContextDiagramRaw(String name, String title, Style style, String target) {
        super(name, title, style);
        this.target = target;
    }

    @Override
    public <E> E visit(DiagramRawVisitor<E> visitor) {
        return visitor.visitSystemContextDiagramRaw(this);
    }

}