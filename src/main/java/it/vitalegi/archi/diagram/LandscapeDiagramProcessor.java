package it.vitalegi.archi.diagram;

import it.vitalegi.archi.diagram.model.Diagram;
import it.vitalegi.archi.diagram.model.LandscapeDiagram;
import it.vitalegi.archi.diagram.rule.RuleEntry;
import it.vitalegi.archi.diagram.rule.element.AnyDescendantOfElementInScopeRule;
import it.vitalegi.archi.diagram.rule.element.HasElementTypeRule;
import it.vitalegi.archi.diagram.rule.relation.AlwaysAllowRelationRule;
import it.vitalegi.archi.diagram.rule.relation.AnyRelationVertexOutOfScopeRule;
import it.vitalegi.archi.diagram.style.StyleHandler;
import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.ElementType;
import it.vitalegi.archi.util.WorkspaceUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class LandscapeDiagramProcessor extends AbstractModelDiagramProcessor<LandscapeDiagram> {
    
    public LandscapeDiagramProcessor(StyleHandler styleHandler) {
        super(styleHandler);
    }

    @Override
    public boolean accept(Diagram diagram) {
        return diagram instanceof LandscapeDiagram;
    }

    @Override
    protected LandscapeDiagram cast(Diagram diagram) {
        return (LandscapeDiagram) diagram;
    }

    @Override
    protected void doValidate(LandscapeDiagram diagram) {
    }

    @Override
    protected boolean isAllowed(LandscapeDiagram diagram, Element element) {
        return WorkspaceUtil.isPerson(element) || WorkspaceUtil.isSoftwareSystem(element) || WorkspaceUtil.isGroup(element);
    }

    @Override
    protected List<RuleEntry> getVisibilityRules(LandscapeDiagram diagram) {
        return Arrays.asList( //
                RuleEntry.include(new HasElementTypeRule(Arrays.asList(ElementType.SOFTWARE_SYSTEM, ElementType.PERSON))), //
                RuleEntry.include(new AlwaysAllowRelationRule()), //
                RuleEntry.include(new AnyDescendantOfElementInScopeRule()), //
                RuleEntry.exclude(new AnyRelationVertexOutOfScopeRule()) //
        );
    }
}