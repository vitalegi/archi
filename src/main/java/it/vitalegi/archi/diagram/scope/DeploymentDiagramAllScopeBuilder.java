package it.vitalegi.archi.diagram.scope;

import it.vitalegi.archi.diagram.rule.RuleEntry;
import it.vitalegi.archi.model.diagram.DeploymentDiagram;
import it.vitalegi.archi.model.element.ElementType;

import java.util.Arrays;
import java.util.List;

import static it.vitalegi.archi.diagram.rule.RuleEntry.allRelations;
import static it.vitalegi.archi.diagram.rule.RuleEntry.anyDescendantInScope;
import static it.vitalegi.archi.diagram.rule.RuleEntry.anyRelationVertexOutOfScope;
import static it.vitalegi.archi.diagram.rule.RuleEntry.exclude;
import static it.vitalegi.archi.diagram.rule.RuleEntry.include;
import static it.vitalegi.archi.diagram.rule.RuleEntry.typeIs;

public class DeploymentDiagramAllScopeBuilder extends DeploymentDiagramScopeBuilder {
    public DeploymentDiagramAllScopeBuilder(DeploymentDiagram diagram) {
        super(diagram);
    }

    @Override
    protected List<RuleEntry> getVisibilityRules() {
        return Arrays.asList( //
                include(typeIs(ElementType.SOFTWARE_SYSTEM_INSTANCE, ElementType.DEPLOYMENT_NODE, ElementType.INFRASTRUCTURE_NODE, ElementType.CONTAINER_INSTANCE, ElementType.SOFTWARE_SYSTEM, ElementType.CONTAINER)), //
                include(allRelations()), //
                include(anyDescendantInScope()), //
                exclude(anyRelationVertexOutOfScope()) //
        );
    }
}
