package it.vitalegi.archi.diagram.scope;

import it.vitalegi.archi.diagram.rule.RuleEntry;
import it.vitalegi.archi.model.diagram.DeploymentDiagram;

import java.util.Arrays;
import java.util.List;

import static it.vitalegi.archi.diagram.rule.RuleEntry.allRelations;
import static it.vitalegi.archi.diagram.rule.RuleEntry.and;
import static it.vitalegi.archi.diagram.rule.RuleEntry.anyDescendantInScope;
import static it.vitalegi.archi.diagram.rule.RuleEntry.anyRelationVertexOutOfScope;
import static it.vitalegi.archi.diagram.rule.RuleEntry.connectedToElementInScope;
import static it.vitalegi.archi.diagram.rule.RuleEntry.exclude;
import static it.vitalegi.archi.diagram.rule.RuleEntry.include;
import static it.vitalegi.archi.diagram.rule.RuleEntry.isConnectedTo;
import static it.vitalegi.archi.diagram.rule.RuleEntry.isConnectedToEnvironment;
import static it.vitalegi.archi.diagram.rule.RuleEntry.isDescendantOf;
import static it.vitalegi.archi.diagram.rule.RuleEntry.not;
import static it.vitalegi.archi.diagram.rule.RuleEntry.typeIs;
import static it.vitalegi.archi.model.element.ElementType.CONTAINER;
import static it.vitalegi.archi.model.element.ElementType.DEPLOYMENT_NODE;
import static it.vitalegi.archi.model.element.ElementType.INFRASTRUCTURE_NODE;
import static it.vitalegi.archi.model.element.ElementType.SOFTWARE_SYSTEM;

public class DeploymentDiagramSoftwareSystemScopeBuilder extends DeploymentDiagramScopeBuilder {


    public DeploymentDiagramSoftwareSystemScopeBuilder(DeploymentDiagram diagram) {
        super(diagram);
    }

    @Override
    protected List<RuleEntry> getVisibilityRules() {
        return Arrays.asList( //
                // target software system and its containers are in scope
                include( //
                        and(typeIs(CONTAINER, SOFTWARE_SYSTEM), isDescendantOf(getSoftwareSystemId())) //
                ), //

                // directly connected software systems / containers are in scope
                include(and(typeIs(SOFTWARE_SYSTEM, CONTAINER), connectedToElementInScope() //
                )), //
                exclude(and( //
                        typeIs(SOFTWARE_SYSTEM, CONTAINER), //
                        not(isConnectedToEnvironment(diagram.getEnvironment()))) //
                ), //

                include(anyDescendantInScope()), //

                include(and(typeIs(INFRASTRUCTURE_NODE), connectedToElementInScope())), //
                include(allRelations()), //
                include(anyDescendantInScope()), //
                exclude(anyRelationVertexOutOfScope()), //
                // relations to/from target software system should be removed
                exclude(isConnectedTo(getSoftwareSystemId())), //
                exclude(and(typeIs(DEPLOYMENT_NODE), not(anyDescendantInScope()))) //
        );
    }

    protected String getSoftwareSystemId() {
        return diagram.getScope();
    }
}
