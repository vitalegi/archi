package it.vitalegi.archi.diagram;

import it.vitalegi.archi.diagram.dto.Diagram;
import it.vitalegi.archi.diagram.dto.DiagramScope;
import it.vitalegi.archi.diagram.rule.RuleEntry;
import it.vitalegi.archi.diagram.rule.VisibilityRule;
import it.vitalegi.archi.diagram.rule.VisibilityRuleType;
import it.vitalegi.archi.diagram.writer.C4PlantUMLWriter;
import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.Relation;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractModelDiagramProcessor<E extends Diagram> extends AbstractDiagramProcessor<E> {

    protected String createPuml(E diagram) {
        var writer = new C4PlantUMLWriter();
        writeHeader(diagram, writer);
        var scope = computeScope(diagram);
        writeStyles(diagram, writer);

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
        for (var rule : rules) {
            applyRule(diagram, rule, scope, elements, relations);
        }
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
                log.info("Diagram {}, add {} to scope via {}", diagram.getName(), element.toShortString(), rule);
            }
        }
        for (var relation : relations) {
            if (rule.match(scope, relation)) {
                scope.add(relation);
                log.info("Diagram {}, add {} to scope via {}", diagram.getName(), relation.toShortString(), rule);
            }
        }
    }

    protected void applyRuleExclusion(E diagram, VisibilityRule rule, DiagramScope scope, List<Element> elements, List<Relation> relations) {
        for (var element : elements) {
            if (rule.match(scope, element)) {
                scope.remove(element);
                log.info("Diagram {}, remove {} to scope via {}", diagram.getName(), element.toShortString(), rule);
            }
        }
        for (var relation : relations) {
            if (rule.match(scope, relation)) {
                scope.remove(relation);
                log.info("Diagram {}, remove {} to scope via {}", diagram.getName(), relation.toShortString(), rule);
            }
        }
    }

    protected void writeElements(E diagram, DiagramScope scope, C4PlantUMLWriter writer) {
        var topLevelElements = diagram.getModel().getElements();
        for (var element : topLevelElements) {
            writeElements(diagram, scope, element, writer);
        }
    }

    protected void writeRelations(E diagram, DiagramScope scope, C4PlantUMLWriter writer) {
        var relations = diagram.getModel().getRelations();
        for (var relation : relations.getAll()) {
            if (scope.isInScope(relation)) {
                writeRelation(relation, writer);
            }
        }
    }

    protected void writeElements(E diagram, DiagramScope scope, Element element, C4PlantUMLWriter writer) {
        if (!scope.isInScope(element)) {
            return;
        }
        if (hasAnyChildrenInScope(diagram, scope, element)) {
            writeAsBoundary(diagram, scope, element, writer);
        } else {
            writeAsContainer(diagram, scope, element, writer);
        }
    }

    protected boolean hasAnyChildrenInScope(E diagram, DiagramScope scope, Element element) {
        return element.getAllChildren().anyMatch(scope::isInScope);
    }

    protected void writeAsBoundary(E diagram, DiagramScope scope, Element element, C4PlantUMLWriter writer) {
        writer.boundaryStart(getAlias(element), element.getName(), formatTags(element));
        for (var child : element.getElements()) {
            writeElements(diagram, scope, child, writer);
        }
        writer.boundaryEnd();
    }

    protected void writeAsContainer(E diagram, DiagramScope scope, Element element, C4PlantUMLWriter writer) {
        writer.container(getAlias(element), element.getName(), null, element.getDescription(), null, formatTags(element), null, null);
    }

    protected void writeRelation(Relation relation, C4PlantUMLWriter writer) {
        writer.addRelation(getAlias(relation.getFrom()), getAlias(relation.getTo()), relation.getDescription(), null, formatTags(relation), null);
    }
}
