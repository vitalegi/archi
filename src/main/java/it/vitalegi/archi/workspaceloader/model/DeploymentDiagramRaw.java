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