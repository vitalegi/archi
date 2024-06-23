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
public class FlowDiagramRaw extends DiagramRaw {
    String flow;

    @Builder
    public FlowDiagramRaw(String name, String title, Style style, DiagramOptions options, String flow) {
        super(name, title, style, options);
        this.flow = flow;
    }

    @Override
    public <E> E visit(DiagramRawVisitor<E> visitor) {
        return visitor.visitFlowDiagramRaw(this);
    }
}