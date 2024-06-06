package it.vitalegi.archi.diagram;

import it.vitalegi.archi.diagram.dto.Diagram;
import it.vitalegi.archi.diagram.dto.SystemContextDiagram;
import it.vitalegi.archi.diagram.rule.AndRule;
import it.vitalegi.archi.diagram.rule.RuleEntry;
import it.vitalegi.archi.diagram.rule.element.AnyDescendantOfElementInScopeRule;
import it.vitalegi.archi.diagram.rule.element.HasElementTypeRule;
import it.vitalegi.archi.diagram.rule.element.IsDescendantOfRule;
import it.vitalegi.archi.diagram.rule.element.IsDirectlyConnectedToElementInScopeRule;
import it.vitalegi.archi.diagram.rule.relation.AlwaysAllowRelationRule;
import it.vitalegi.archi.diagram.rule.relation.AnyRelationVertexOutOfScopeRule;
import it.vitalegi.archi.exception.ElementNotFoundException;
import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.ElementType;
import it.vitalegi.archi.util.StringUtil;
import it.vitalegi.archi.util.WorkspaceUtil;
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
        if (StringUtil.isNullOrEmpty(diagram.getTarget())) {
            throw new IllegalArgumentException("Diagram " + diagram.getName() + ", missing target.");
        }
        var target = diagram.getModel().getElementById(diagram.getTarget());
        if (target == null) {
            throw new ElementNotFoundException(diagram.getTarget(), "invalid target on Diagram " + diagram.getName());
        }
        if (!WorkspaceUtil.isSoftwareSystem(target)) {
            throw new IllegalArgumentException("Diagram " + diagram.getName() + ", invalid target. Expected: SoftwareSystem, Actual: " + target.toShortString());
        }
    }

    @Override
    protected boolean isAllowed(SystemContextDiagram diagram, Element element) {
        return WorkspaceUtil.isPerson(element) || WorkspaceUtil.isSoftwareSystem(element) || WorkspaceUtil.isContainer(element) || WorkspaceUtil.isGroup(element);
    }

    @Override
    protected List<RuleEntry> getVisibilityRules(SystemContextDiagram diagram) {
        return Arrays.asList( //
                RuleEntry.include(new AndRule(
                        new HasElementTypeRule(ElementType.CONTAINER, ElementType.SOFTWARE_SYSTEM),
                        new IsDescendantOfRule(diagram.getTarget()) //
                )), //
                RuleEntry.include(new AndRule(
                        new HasElementTypeRule(ElementType.PERSON, ElementType.SOFTWARE_SYSTEM),
                        new IsDirectlyConnectedToElementInScopeRule() //
                )), //
                RuleEntry.include(new AlwaysAllowRelationRule()), //
                RuleEntry.include(new AnyDescendantOfElementInScopeRule()), //
                RuleEntry.exclude(new AnyRelationVertexOutOfScopeRule()) //
        );
    }
}
