package it.vitalegi.archi.diagram.scope;

import it.vitalegi.archi.diagram.rule.RuleEntry;
import it.vitalegi.archi.diagram.rule.element.AnyDescendantOfElementInScopeRule;
import it.vitalegi.archi.diagram.rule.element.HasElementTypeRule;
import it.vitalegi.archi.diagram.rule.relation.AlwaysAllowRelationRule;
import it.vitalegi.archi.diagram.rule.relation.AnyRelationVertexOutOfScopeRule;
import it.vitalegi.archi.model.diagram.LandscapeDiagram;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.element.ElementType;
import it.vitalegi.archi.util.WorkspaceUtil;

import java.util.Arrays;
import java.util.List;

public class LandscapeDiagramScopeBuilder extends DiagramScopeBuilder<LandscapeDiagram> {
    public LandscapeDiagramScopeBuilder(LandscapeDiagram diagram) {
        super(diagram);
    }

    @Override
    protected boolean isAllowed(Element element) {
        return WorkspaceUtil.isPerson(element) || WorkspaceUtil.isSoftwareSystem(element) || WorkspaceUtil.isGroup(element);
    }

    @Override
    protected List<RuleEntry> getVisibilityRules() {
        return Arrays.asList( //
                RuleEntry.include(new HasElementTypeRule(Arrays.asList(ElementType.SOFTWARE_SYSTEM, ElementType.PERSON))), //
                RuleEntry.include(new AlwaysAllowRelationRule()), //
                RuleEntry.include(new AnyDescendantOfElementInScopeRule()), //
                RuleEntry.exclude(new AnyRelationVertexOutOfScopeRule()) //
        );
    }
}
