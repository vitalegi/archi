package it.vitalegi.archi.diagram;

import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.ElementType;
import it.vitalegi.archi.util.WorkspaceUtil;
import it.vitalegi.archi.diagram.dto.SystemContextDiagram;
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
public class SystemContextDiagramProcessor extends AbstractModelDiagramProcessor<SystemContextDiagram> {


    @Override
    public boolean accept(Diagram diagram) {
        return diagram instanceof SystemContextDiagram;
    }

    @Override
    protected SystemContextDiagram cast(Diagram diagram) {
        return (SystemContextDiagram) diagram;
    }

    @Override
    protected void doValidate(SystemContextDiagram diagram) {
    }

    @Override
    protected boolean isAllowed(SystemContextDiagram diagram, Element element) {
        return WorkspaceUtil.isSoftwareSystem(element) || WorkspaceUtil.isPerson(element) || WorkspaceUtil.isGroup(element);
    }

    @Override
    protected List<VisibilityRule> getVisibilityRules(SystemContextDiagram diagram) {
        return Arrays.asList( //
                new HasElementTypeRule(VisibilityRuleType.INCLUSION, Arrays.asList(ElementType.SOFTWARE_SYSTEM, ElementType.PERSON)), //
                new AlwaysAllowRelationRule(VisibilityRuleType.INCLUSION), //
                new AnyDescendantOfElementInScopeRule(VisibilityRuleType.INCLUSION), //
                new AnyRelationVertexOutOfScopeRule(VisibilityRuleType.EXCLUSION) //
        );
    }
}
