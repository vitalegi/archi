package it.vitalegi.archi.exporter.plantuml;

import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.element.ElementType;
import it.vitalegi.archi.model.diagram.LandscapeDiagram;
import it.vitalegi.archi.diagram.rule.RuleEntry;
import it.vitalegi.archi.diagram.rule.element.AnyDescendantOfElementInScopeRule;
import it.vitalegi.archi.diagram.rule.element.HasElementTypeRule;
import it.vitalegi.archi.diagram.rule.relation.AlwaysAllowRelationRule;
import it.vitalegi.archi.diagram.rule.relation.AnyRelationVertexOutOfScopeRule;
import it.vitalegi.archi.util.WorkspaceUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class LandscapeDiagramPlantumlExporter extends AbstractModelDiagramPlantumlExporter<LandscapeDiagram> {


    @Override
    public void validate(LandscapeDiagram diagram) {
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
