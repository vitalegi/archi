package it.vitalegi.archi.visitor;

import it.vitalegi.archi.diagram.model.DeploymentDiagram;
import it.vitalegi.archi.diagram.model.LandscapeDiagram;
import it.vitalegi.archi.diagram.model.SystemContextDiagram;

public interface DiagramVisitor<E> {
    E visitLandscapeDiagram(LandscapeDiagram diagram);

    E visitSystemContextDiagram(SystemContextDiagram diagram);

    E visitDeploymentDiagram(DeploymentDiagram diagram);
}
