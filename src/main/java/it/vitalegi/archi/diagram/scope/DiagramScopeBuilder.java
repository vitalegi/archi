package it.vitalegi.archi.diagram.scope;

import it.vitalegi.archi.diagram.DiagramScope;
import it.vitalegi.archi.diagram.rule.RuleEntry;
import it.vitalegi.archi.diagram.rule.VisibilityRule;
import it.vitalegi.archi.diagram.rule.VisibilityRuleType;
import it.vitalegi.archi.model.diagram.Diagram;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.relation.Relation;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class DiagramScopeBuilder<E extends Diagram> {

    E diagram;

    public DiagramScopeBuilder(E diagram) {
        this.diagram = diagram;
    }

    public DiagramScope computeScope() {
        var rules = getVisibilityRules();
        var scope = new DiagramScope();
        var elements = diagram.getModel().getAllElements().stream().filter(this::isAllowed).collect(Collectors.toList());
        var relations = diagram.getModel().getRelations().getAll();
        log.debug("Diagram {}, start processing rules", diagram.getName());
        log.debug("Elements in perimeter: {}", elements.stream().map(Element::toShortString).collect(Collectors.toList()));
        for (var rule : rules) {
            applyRule(rule, scope, elements, relations);
        }
        log.debug("Diagram {}, end processing rules", diagram.getName());
        return scope;
    }


    protected abstract List<RuleEntry> getVisibilityRules();

    /**
     * Is the type of element allowed in this diagram?
     *
     * @param element
     * @return
     */
    protected abstract boolean isAllowed(Element element);


    protected void applyRule(RuleEntry rule, DiagramScope scope, List<Element> elements, List<Relation> relations) {
        if (rule.getType() == VisibilityRuleType.INCLUSION) {
            applyRuleInclusion(rule.getRule(), scope, elements, relations);
        } else if (rule.getType() == VisibilityRuleType.EXCLUSION) {
            applyRuleExclusion(rule.getRule(), scope, elements, relations);
        } else {
            throw new RuntimeException("Can't process rule, unknown RuleType. Rule: " + rule);
        }
    }

    protected void applyRuleInclusion(VisibilityRule rule, DiagramScope scope, List<Element> elements, List<Relation> relations) {
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

    protected void applyRuleExclusion(VisibilityRule rule, DiagramScope scope, List<Element> elements, List<Relation> relations) {
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

}
