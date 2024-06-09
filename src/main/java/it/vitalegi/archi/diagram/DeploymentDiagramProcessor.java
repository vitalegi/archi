package it.vitalegi.archi.diagram;

import it.vitalegi.archi.diagram.style.StyleHandler;
import it.vitalegi.archi.exception.ElementNotFoundException;
import it.vitalegi.archi.model.Container;
import it.vitalegi.archi.model.ContainerInstance;
import it.vitalegi.archi.model.DeploymentEnvironment;
import it.vitalegi.archi.model.DeploymentNode;
import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.Entity;
import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.model.Relation;
import it.vitalegi.archi.model.SoftwareSystem;
import it.vitalegi.archi.model.SoftwareSystemInstance;
import it.vitalegi.archi.plantuml.LayoutDirection;
import it.vitalegi.archi.util.StringUtil;
import it.vitalegi.archi.util.WorkspaceUtil;
import it.vitalegi.archi.diagram.model.DeploymentDiagram;
import it.vitalegi.archi.diagram.model.Diagram;
import it.vitalegi.archi.diagram.scope.Scope;
import it.vitalegi.archi.diagram.writer.C4PlantUMLWriter;
import it.vitalegi.archi.workspace.Workspace;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class DeploymentDiagramProcessor extends AbstractDiagramProcessor<DeploymentDiagram> {

    public DeploymentDiagramProcessor(StyleHandler styleHandler) {
        super(styleHandler);
    }

    public boolean accept(Diagram diagram) {
        return diagram instanceof DeploymentDiagram;
    }

    protected void doValidate(DeploymentDiagram diagram) {
        var env = diagram.getEnvironment();
        if (StringUtil.isNullOrEmpty(env)) {
            throw new NullPointerException("Environment is mandatory on DeploymentDiagram. Error on " + diagram.getName());
        }
        var deploymentEnvironment = diagram.getModel().findDeploymentEnvironmentById(env);
        if (deploymentEnvironment == null) {
            throw new ElementNotFoundException(env, "required on diagram " + diagram.getName());
        }
        doValidateScope(diagram);
    }

    protected void doValidateScope(DeploymentDiagram diagram) {
        if (isScopeAll(diagram)) {
            return;
        }
        if (isScopeSoftwareSystem(diagram)) {
            return;
        }
        throw new ElementNotFoundException(diagram.getScope(), "Scope " + diagram.getScope() + " on diagram " + diagram.getName() + " is invalid. Check if all objects exist.");
    }

    protected DeploymentDiagram cast(Diagram diagram) {
        return (DeploymentDiagram) diagram;
    }

    @Override
    protected String createPuml(Workspace workspace, DeploymentDiagram diagram) {
        var writer = new C4PlantUMLWriter();
        writeHeader(workspace, diagram, writer);
        writeStyles(diagram, writer);

        var deploymentEnvironment = getDeploymentEnvironment(diagram);
        var elements = getElementsInScope(diagram);
        var relations = getRelationsInScope(diagram, elements);

        deploymentEnvironment.getElements().forEach(element -> addElementTreeToPuml(elements, element, writer));

        relations.forEach(relation -> addRelationToPuml(relation, writer));

        writeFooter(diagram, writer);
        return writer.build();
    }

    @Override
    protected void writeIncludes(C4PlantUMLWriter writer) {
        writer.include("<C4/C4>");
        writer.include("<C4/C4_Context>");
        writer.include("<C4/C4_Container>");
        writer.include("<C4/C4_Deployment>");
    }
    protected void addElementTreeToPuml(List<Element> elementsInScope, Element element, C4PlantUMLWriter writer) {
        if (!elementsInScope.contains(element)) {
            return;
        }
        if (WorkspaceUtil.isDeploymentNode(element)) {
            var deploymentNode = (DeploymentNode) element;
            writer.deploymentNodeStart(getAlias(element), element.getName(), null, element.getDescription(), "", formatTags(element), "");
            deploymentNode.getElements().forEach(child -> addElementTreeToPuml(elementsInScope, child, writer));
            writer.deploymentNodeEnd();
            return;
        }
        if (WorkspaceUtil.isInfrastructureNode(element)) {
            writer.container(getAlias(element), element.getName(), null, element.getDescription(), "", formatTags(element), "", "");
            return;
        }
        if (WorkspaceUtil.isContainerInstance(element)) {
            writer.deploymentNodeStart(getAlias(element), element.getName(), null, element.getDescription(), "", formatTags(element), "");
            var containerInstance = (ContainerInstance) element;
            var container = containerInstance.getContainer();
            writer.container(getAlias(container), container.getName(), null, container.getDescription(), "", formatTags(container), "", "");
            writer.deploymentNodeEnd();
            return;
        }
        if (WorkspaceUtil.isSoftwareSystemInstance(element)) {
            writer.deploymentNodeStart(getAlias(element), element.getName(), null, element.getDescription(), "", formatTags(element), "");
            var softwareSystemInstance = (SoftwareSystemInstance) element;
            var softwareSystem = softwareSystemInstance.getSoftwareSystem();
            writer.container(getAlias(softwareSystem), softwareSystem.getName(), null, softwareSystem.getDescription(), "", formatTags(softwareSystem), "", "");
            writer.deploymentNodeEnd();
            return;
        }
        throw new RuntimeException("Unable to process " + element.toShortString());
    }

    protected void addRelationToPuml(Relation relation, C4PlantUMLWriter writer) {
        writer.addRelation(getAlias(relation.getFrom()), getAlias(relation.getTo()), relation.getDescription(), null, formatTags(relation), null);
    }

    protected List<Element> getElementsInScope(DeploymentDiagram diagram) {
        var perimeter = getElementsPerimeter(diagram).collect(Collectors.toList());
        var baseSet = getBaseSet(diagram, perimeter);
        return getElementsInScope(diagram, perimeter, baseSet);
    }

    protected Stream<Element> getElementsPerimeter(DeploymentDiagram diagram) {
        var deploymentEnvironment = getDeploymentEnvironment(diagram);
        return deploymentEnvironment.getAllChildren().distinct();
    }

    protected List<Element> getBaseSet(DeploymentDiagram diagram, List<Element> perimeter) {
        return perimeter.stream().filter(e -> isInBaseScope(diagram, e)).collect(Collectors.toList());
    }
    protected List<Element> getElementsInScope(DeploymentDiagram diagram, List<Element> perimeter, List<Element> baseSet) {
        return perimeter.stream().filter(e -> isInScope(diagram, baseSet, e)).collect(Collectors.toList());
    }

    protected List<Relation> getRelationsInScope(DeploymentDiagram diagram, List<Element> elementsInScope) {
        log.debug("Elements in scope: {}", elementsInScope.stream().map(Element::toShortString).collect(Collectors.joining(", ")));
        return diagram.getModel().getRelations().getAll().stream().filter(r -> isInScope(diagram, elementsInScope, r)).collect(Collectors.toList());
    }

    protected boolean isScopeAll(DeploymentDiagram diagram) {
        return Scope.isScopeAll(diagram.getScope());
    }

    protected boolean isScopeSoftwareSystem(DeploymentDiagram diagram) {
        var scope = diagram.getScope();
        if (StringUtil.isNullOrEmpty(scope)) {
            return false;
        }
        var softwareSystem = WorkspaceUtil.findSoftwareSystem(diagram.getModel().getAllElements(), scope);
        return softwareSystem != null;
    }

    protected DeploymentEnvironment getDeploymentEnvironment(DeploymentDiagram diagram) {
        var model = diagram.getModel();
        return WorkspaceUtil.getDeploymentEnvironment(model.getDeploymentEnvironments(), diagram.getEnvironment());
    }

    protected boolean isInBaseScope(DeploymentDiagram diagram, Element element) {
        if (isScopeAll(diagram)) {
            return WorkspaceUtil.isContainerInstance(element) || WorkspaceUtil.isSoftwareSystemInstance(element) || WorkspaceUtil.isContainer(element) || WorkspaceUtil.isSoftwareSystem(element);
        }
        if (isScopeSoftwareSystem(diagram)) {
            if (WorkspaceUtil.isContainer(element)) {
                var curr = (Container) element;
                return Entity.equals(curr.getSoftwareSystem().getId(), diagram.getScope());
            }
            if (WorkspaceUtil.isContainerInstance(element)) {
                var curr = (ContainerInstance) element;
                var container = curr.getContainer();
                return Entity.equals(container.getSoftwareSystem().getId(), diagram.getScope());
            }
            if (WorkspaceUtil.isSoftwareSystem(element)) {
                var curr = (SoftwareSystem) element;
                return Entity.equals(curr.getId(), diagram.getScope());
            }
            if (WorkspaceUtil.isSoftwareSystemInstance(element)) {
                var curr = (SoftwareSystemInstance) element;
                var softwareSystem = curr.getSoftwareSystem();
                return Entity.equals(softwareSystem.getId(), diagram.getScope());
            }
        }
        return false;
    }


    protected boolean isInScope(DeploymentDiagram diagram, List<Element> baseScope, Element element) {
        // if it's already in scope, keep it
        if (baseScope.contains(element)) {
            log.debug("Diagram {}, {} is in scope", diagram.getName(), element.toShortString());
            return true;
        }
        // if it is parent of anything in scope, it's in scope
        var children = element.getAllChildren();
        var firstMatchedChild = children.filter(baseScope::contains).findFirst().orElse(null);
        if (firstMatchedChild != null) {
            log.debug("Diagram {}, {} is in scope because it's parent of {}", diagram.getName(), element.toShortString(), firstMatchedChild.toShortString());
            return true;
        }
        // if there's a direct connection with anything in scope, it's in scope
        if (baseScope.stream().anyMatch(scope -> hasDistance1(diagram.getModel(), scope, element))) {
            log.debug("Diagram {}, {} is in scope because it's connected to at least one element in scope", diagram.getName(), element.toShortString());
            return true;
        }
        return false;
    }

    protected boolean hasDistance1(Model model, Element element1, Element element2) {
        var relations = model.getRelations().getRelationsBetween(element1, element2);
        log.debug("Relations between {} and {}: {}", element1.toShortString(), element2.toShortString(), relations.size());
        return !relations.isEmpty();
    }

    protected boolean isInScope(DeploymentDiagram diagram, List<Element> elementsInScope, Relation relation) {
        if (!elementsInScope.contains(relation.getFrom())) {
            return false;
        }
        if (!elementsInScope.contains(relation.getTo())) {
            return false;
        }
        return true;
    }

}
