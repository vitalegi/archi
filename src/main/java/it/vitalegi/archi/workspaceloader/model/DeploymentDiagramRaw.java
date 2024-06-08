package it.vitalegi.archi.workspaceloader.model;

import it.vitalegi.archi.diagram.model.Diagram;
import it.vitalegi.archi.style.model.Style;
import it.vitalegi.archi.workspaceloader.visitor.DiagramRawVisitor;
import lombok.AllArgsConstructor;
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
public class DeploymentDiagramRaw extends DiagramRaw {
    String scope;
    String environment;

    @Builder
    public DeploymentDiagramRaw(String name, String title, Style style, String scope, String environment) {
        super(name, title, style);
        this.scope = scope;
        this.environment = environment;
    }

    @Override
    public <E> E visit(DiagramRawVisitor<E> visitor) {
        return visitor.visitDeploymentDiagramRaw(this);
    }
}