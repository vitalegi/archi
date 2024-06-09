package it.vitalegi.archi.visitor;

import it.vitalegi.archi.model.diagram.DeploymentDiagram;
import it.vitalegi.archi.model.diagram.LandscapeDiagram;
import it.vitalegi.archi.model.diagram.SystemContextDiagram;

public interface DiagramVisitor<E> {
    E visitLandscapeDiagram(LandscapeDiagram diagram);

    E visitSystemContextDiagram(SystemContextDiagram diagram);

    E visitDeploymentDiagram(DeploymentDiagram diagram);
}
