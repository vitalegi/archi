package it.vitalegi.archi.diagram.scope;

import it.vitalegi.archi.diagram.rule.AndRule;
import it.vitalegi.archi.diagram.rule.RuleEntry;
import it.vitalegi.archi.diagram.rule.element.AnyDescendantOfElementInScopeRule;
import it.vitalegi.archi.diagram.rule.element.HasElementTypeRule;
import it.vitalegi.archi.diagram.rule.element.IsDescendantOfRule;
import it.vitalegi.archi.diagram.rule.element.IsDirectlyConnectedToElementInScopeRule;
import it.vitalegi.archi.diagram.rule.relation.AlwaysAllowRelationRule;
import it.vitalegi.archi.diagram.rule.relation.AnyRelationVertexOutOfScopeRule;
import it.vitalegi.archi.diagram.rule.relation.IsConnectedToRule;
import it.vitalegi.archi.model.diagram.SystemContextDiagram;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.element.ElementType;
import it.vitalegi.archi.util.WorkspaceUtil;

import java.util.Arrays;
import java.util.List;

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
                RuleEntry.include(new AndRule(
                        new HasElementTypeRule(ElementType.CONTAINER, ElementType.SOFTWARE_SYSTEM),
                        new IsDescendantOfRule(diagram.getTarget()) //
                )), //
                // directly connected software systems and people are in scope
                RuleEntry.include(new AndRule(
                        new HasElementTypeRule(ElementType.PERSON, ElementType.SOFTWARE_SYSTEM),
                        new IsDirectlyConnectedToElementInScopeRule() //
                )), //
                RuleEntry.include(new AlwaysAllowRelationRule()), //
                RuleEntry.include(new AnyDescendantOfElementInScopeRule()), //
                RuleEntry.exclude(new AnyRelationVertexOutOfScopeRule()), //
                // relations to/from target software system should be removed
                RuleEntry.exclude(new IsConnectedToRule(diagram.getTarget()))
        );
    }
}
