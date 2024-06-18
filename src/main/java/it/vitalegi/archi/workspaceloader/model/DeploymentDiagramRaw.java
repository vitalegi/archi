package it.vitalegi.archi.workspaceloader.model;

import it.vitalegi.archi.model.diagram.DeploymentDiagram;
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
public class DeploymentDiagramRaw extends DiagramRaw {
    String scope;
    String environment;

    @Builder
    public DeploymentDiagramRaw(String name, String title, Style style, DiagramOptions options, String scope, String environment) {
        super(name, title, style, options);
        this.scope = scope;
        this.environment = environment;
    }

    public static DeploymentDiagramRawBuilder all(String name, String environment) {
        return builder().name(name).environment(environment).scope(DeploymentDiagram.ALL);
    }

    public static DeploymentDiagramRawBuilder scoped(String name, String environment, String softwareSystemId) {
        return builder().name(name).environment(environment).scope(softwareSystemId);
    }

    @Override
    public <E> E visit(DiagramRawVisitor<E> visitor) {
        return visitor.visitDeploymentDiagramRaw(this);
    }
}