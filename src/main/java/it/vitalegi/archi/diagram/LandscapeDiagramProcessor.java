package it.vitalegi.archi.diagram;

import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.ElementType;
import it.vitalegi.archi.util.WorkspaceUtil;
import it.vitalegi.archi.diagram.dto.LandscapeDiagram;
import it.vitalegi.archi.diagram.dto.Diagram;
import it.vitalegi.archi.diagram.rule.VisibilityRule;
import it.vitalegi.archi.diagram.rule.VisibilityRuleType;
import it.vitalegi.archi.diagram.rule.element.AnyDescendantOfElementInScopeRule;
import it.vitalegi.archi.diagram.rule.element.HasElementTypeRule;
import it.vitalegi.archi.diagram.rule.relation.AlwaysAllowRelationRule;
import it.vitalegi.archi.diagram.rule.relation.AnyRelationVertexOutOfScopeRule;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class LandscapeDiagramProcessor extends AbstractModelDiagramProcessor<LandscapeDiagram> {


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
    protected List<VisibilityRule> getVisibilityRules(LandscapeDiagram diagram) {
        return Arrays.asList( //
                new HasElementTypeRule(VisibilityRuleType.INCLUSION, Arrays.asList(ElementType.SOFTWARE_SYSTEM, ElementType.PERSON)), //
                new AlwaysAllowRelationRule(VisibilityRuleType.INCLUSION), //
                new AnyDescendantOfElementInScopeRule(VisibilityRuleType.INCLUSION), //
                new AnyRelationVertexOutOfScopeRule(VisibilityRuleType.EXCLUSION) //
        );
    }
}
