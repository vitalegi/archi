package it.vitalegi.archi.diagram.scope;

import it.vitalegi.archi.diagram.rule.RuleEntry;
import it.vitalegi.archi.model.diagram.LandscapeDiagram;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.element.ElementType;
import it.vitalegi.archi.util.WorkspaceUtil;

import java.util.Arrays;
import java.util.List;

import static it.vitalegi.archi.diagram.rule.RuleEntry.allRelations;
import static it.vitalegi.archi.diagram.rule.RuleEntry.anyDescendantInScope;
import static it.vitalegi.archi.diagram.rule.RuleEntry.anyRelationVertexOutOfScope;
import static it.vitalegi.archi.diagram.rule.RuleEntry.exclude;
import static it.vitalegi.archi.diagram.rule.RuleEntry.include;
import static it.vitalegi.archi.diagram.rule.RuleEntry.typeIs;

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
                include(typeIs(ElementType.SOFTWARE_SYSTEM, ElementType.PERSON)), //
                include(allRelations()), //
                include(anyDescendantInScope()), //
                exclude(anyRelationVertexOutOfScope()) //
        );
    }
}