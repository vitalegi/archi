package it.vitalegi.archi.diagram.scope;

import it.vitalegi.archi.diagram.rule.RuleEntry;
import it.vitalegi.archi.model.diagram.SystemContextDiagram;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.element.ElementType;
import it.vitalegi.archi.util.WorkspaceUtil;

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
import static it.vitalegi.archi.diagram.rule.RuleEntry.isDescendantOf;
import static it.vitalegi.archi.diagram.rule.RuleEntry.typeIs;

public class SystemContextScopeBuilder extends DiagramScopeBuilder<SystemContextDiagram> {
    public SystemContextScopeBuilder(SystemContextDiagram diagram) {
        super(diagram);
    }

    @Override
    protected boolean isAllowed(Element element) {
        return WorkspaceUtil.isPerson(element) || WorkspaceUtil.isSoftwareSystem(element) || WorkspaceUtil.isContainer(element) || WorkspaceUtil.isGroup(element);
    }

    @Override
    protected List<RuleEntry> getVisibilityRules() {
        return Arrays.asList( //
                // target software system and its containers are in scope
                include(and(
                        typeIs(ElementType.CONTAINER, ElementType.SOFTWARE_SYSTEM),
                        isDescendantOf(diagram.getTarget()) //
                )), //
                // directly connected software systems and people are in scope
                include(and(
                        typeIs(ElementType.PERSON, ElementType.SOFTWARE_SYSTEM),
                        connectedToElementInScope() //
                )), //
                include(allRelations()), //
                include(anyDescendantInScope()), //
                exclude(anyRelationVertexOutOfScope()), //
                // relations to/from target software system should be removed
                exclude(isConnectedTo(diagram.getTarget()))
        );
    }
}
