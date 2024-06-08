package it.vitalegi.archi.workspaceloader.visitor;

import it.vitalegi.archi.diagram.model.Diagram;
import it.vitalegi.archi.workspaceloader.model.DeploymentDiagramRaw;
import it.vitalegi.archi.workspaceloader.model.LandscapeDiagramRaw;
import it.vitalegi.archi.workspaceloader.model.SystemContextDiagramRaw;

public interface DiagramRawVisitor<E> {
    E visitLandscapeDiagramRaw(LandscapeDiagramRaw diagram);

    E visitSystemContextDiagramRaw(SystemContextDiagramRaw diagram);

    E visitDeploymentDiagramRaw(DeploymentDiagramRaw diagram);
}
