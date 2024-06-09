package it.vitalegi.archi.exporter.plantuml;

import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.relation.Relation;
import it.vitalegi.archi.model.diagram.Diagram;
import it.vitalegi.archi.model.diagram.DiagramScope;
import it.vitalegi.archi.diagram.rule.RuleEntry;
import it.vitalegi.archi.diagram.rule.VisibilityRule;
import it.vitalegi.archi.diagram.rule.VisibilityRuleType;
import it.vitalegi.archi.model.Workspace;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractModelDiagramPlantumlExporter<E extends Diagram> extends AbstractDiagramPlantumlExporter<E> {

    @Override
    public String export(Workspace workspace, E diagram) {
        var writer = new C4PlantumlWriter();
        writeHeader(workspace, diagram, writer);
        writeStyles(workspace, diagram, writer);

        var scope = computeScope(diagram);
        writeElements(diagram, scope, writer);
        writeRelations(diagram, scope, writer);

        writeFooter(diagram, writer);
        return writer.build();
    }

    /**
     * Is the type of element allowed in this chart?
     *
     * @param diagram
     * @param element
     * @return
     */
    protected abstract boolean isAllowed(E diagram, Element element);

    protected List<RuleEntry> getVisibilityRules(E diagram) {
        return new ArrayList<>();
    }

    protected DiagramScope computeScope(E diagram) {
        var rules = getVisibilityRules(diagram);
        var scope = new DiagramScope();
        var elements = diagram.getModel().getAllElements().stream().filter(e -> isAllowed(diagram, e)).collect(Collectors.toList());
        var relations = diagram.getModel().getRelations().getAll();
        log.debug("Diagram {}, start processing rules", diagram.getName());
        log.debug("Elements in perimeter: {}", elements.stream().map(Element::toShortString).collect(Collectors.toList()));
        for (var rule : rules) {
            applyRule(diagram, rule, scope, elements, relations);
        }
        log.debug("Diagram {}, end processing rules", diagram.getName());
        return scope;
    }

    protected void applyRule(E diagram, RuleEntry rule, DiagramScope scope, List<Element> elements, List<Relation> relations) {
        if (rule.getType() == VisibilityRuleType.INCLUSION) {
            applyRuleInclusion(diagram, rule.getRule(), scope, elements, relations);
        } else if (rule.getType() == VisibilityRuleType.EXCLUSION) {
            applyRuleExclusion(diagram, rule.getRule(), scope, elements, relations);
        } else {
            throw new RuntimeException("Can't process rule, unknown RuleType. Rule: " + rule);
        }
    }

    protected void applyRuleInclusion(E diagram, VisibilityRule rule, DiagramScope scope, List<Element> elements, List<Relation> relations) {
        for (var element : elements) {
            if (rule.match(scope, element)) {
                scope.add(element);
                log.debug("Diagram {}, add {} to scope via {}", diagram.getName(), element.toShortString(), rule);
            }
        }
        for (var relation : relations) {
            if (rule.match(scope, relation)) {
                scope.add(relation);
                log.debug("Diagram {}, add {} to scope via {}", diagram.getName(), relation.toShortString(), rule);
            }
        }
    }

    protected void applyRuleExclusion(E diagram, VisibilityRule rule, DiagramScope scope, List<Element> elements, List<Relation> relations) {
        for (var element : elements) {
            if (rule.match(scope, element)) {
                scope.remove(element);
                log.debug("Diagram {}, remove {} to scope via {}", diagram.getName(), element.toShortString(), rule);
            }
        }
        for (var relation : relations) {
            if (rule.match(scope, relation)) {
                scope.remove(relation);
                log.debug("Diagram {}, remove {} to scope via {}", diagram.getName(), relation.toShortString(), rule);
            }
        }
    }

    protected void writeElements(E diagram, DiagramScope scope, C4PlantumlWriter writer) {
        var topLevelElements = diagram.getModel().getElements();
        for (var element : topLevelElements) {
            writeElements(diagram, scope, element, writer);
        }
    }

    protected void writeRelations(E diagram, DiagramScope scope, C4PlantumlWriter writer) {
        var relations = diagram.getModel().getRelations();
        for (var relation : relations.getAll()) {
            if (scope.isInScope(relation)) {
                writer.addRelation(relation);
            }
        }
    }

    protected void writeElements(E diagram, DiagramScope scope, Element element, C4PlantumlWriter writer) {
        if (!scope.isInScope(element)) {
            return;
        }
        if (hasAnyChildrenInScope(diagram, scope, element)) {
            writeAsBoundary(diagram, scope, element, writer);
        } else {
            writer.container(element);
        }
    }

    protected boolean hasAnyChildrenInScope(E diagram, DiagramScope scope, Element element) {
        return element.getAllChildren().anyMatch(scope::isInScope);
    }

    protected void writeAsBoundary(E diagram, DiagramScope scope, Element element, C4PlantumlWriter writer) {
        writer.boundaryStart(element);
        for (var child : element.getElements()) {
            writeElements(diagram, scope, child, writer);
        }
        writer.boundaryEnd();
    }
}
