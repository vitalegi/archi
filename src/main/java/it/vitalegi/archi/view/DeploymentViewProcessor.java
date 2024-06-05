package it.vitalegi.archi.view;

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
import it.vitalegi.archi.plantuml.PlantUmlExporter;
import it.vitalegi.archi.util.StringUtil;
import it.vitalegi.archi.util.WorkspaceUtil;
import it.vitalegi.archi.view.constant.ViewFormat;
import it.vitalegi.archi.view.dto.DeploymentView;
import it.vitalegi.archi.view.dto.View;
import it.vitalegi.archi.view.scope.Scope;
import it.vitalegi.archi.view.writer.C4PlantUMLWriter;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class DeploymentViewProcessor extends AbstractViewProcessor<DeploymentView> {

    public boolean accept(View view) {
        return view instanceof DeploymentView;
    }

    protected void doValidate(DeploymentView view) {
        var env = view.getEnvironment();
        if (StringUtil.isNullOrEmpty(env)) {
            throw new NullPointerException("Environment is mandatory on DeploymentView. Error on " + view.getName());
        }
        var deploymentEnvironment = view.getModel().findDeploymentEnvironmentById(env);
        if (deploymentEnvironment == null) {
            throw new ElementNotFoundException(env, "required on view " + view.getName());
        }
        doValidateScope(view);
    }

    protected void doValidateScope(DeploymentView view) {
        if (isScopeAll(view)) {
            return;
        }
        if (isScopeSoftwareSystem(view)) {
            return;
        }
        throw new ElementNotFoundException(view.getScope(), "Scope " + view.getScope() + " on view " + view.getName() + " is invalid. Check if all objects exist.");
    }

    protected DeploymentView cast(View view) {
        return (DeploymentView) view;
    }

    @Override
    protected String createPuml(DeploymentView view) {
        var writer = new C4PlantUMLWriter();
        writer.startuml();
        writer.set("separator", "none");
        if (StringUtil.isNotNullOrEmpty(view.getTitle())) {
            writer.title(view.getTitle());
        }
        writer.direction(LayoutDirection.TOP_TO_BOTTOM);

        writer.include("<C4/C4>");
        writer.include("<C4/C4_Context>");
        writer.include("<C4/C4_Container>");
        writer.include("<C4/C4_Deployment>");

        var deploymentEnvironment = getDeploymentEnvironment(view);
        var elements = getElementsInScope(view);
        var relations = getRelationsInScope(view, elements);

        writer.addElementTag("Element", "#ffffff", "#888888", "#000000", "", "", "solid");
        writer.addElementTag("Container", "#006daa", "#004c76", "#000000", "", "", "solid");

        writer.addRelTag("Relationship", "#707070", "#707070", "");

        deploymentEnvironment.getElements().forEach(element -> addElementTreeToPuml(elements, element, writer));

        relations.forEach(relation -> addRelationToPuml(relation, writer));

        writer.hideStereotypes();
        writer.enduml();
        return writer.build();
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

    protected List<Element> getElementsInScope(DeploymentView view) {
        var perimeter = getElementsPerimeter(view).collect(Collectors.toList());
        var baseSet = getBaseSet(view, perimeter);
        return getElementsInScope(view, perimeter, baseSet);
    }

    protected Stream<Element> getElementsPerimeter(DeploymentView view) {
        var deploymentEnvironment = getDeploymentEnvironment(view);
        return deploymentEnvironment.getAllChildren().distinct();
    }

    protected List<Element> getBaseSet(DeploymentView view, List<Element> perimeter) {
        return perimeter.stream().filter(e -> isInBaseScope(view, e)).collect(Collectors.toList());
    }
    protected List<Element> getElementsInScope(DeploymentView view, List<Element> perimeter, List<Element> baseSet) {
        return perimeter.stream().filter(e -> isInScope(view, baseSet, e)).collect(Collectors.toList());
    }

    protected List<Relation> getRelationsInScope(DeploymentView view, List<Element> elementsInScope) {
        log.debug("Elements in scope: {}", elementsInScope.stream().map(Element::toShortString).collect(Collectors.joining(", ")));
        return view.getModel().getRelations().getAll().stream().filter(r -> isInScope(view, elementsInScope, r)).collect(Collectors.toList());
    }

    protected boolean isScopeAll(DeploymentView view) {
        return Scope.isScopeAll(view.getScope());
    }

    protected boolean isScopeSoftwareSystem(DeploymentView view) {
        var scope = view.getScope();
        if (StringUtil.isNullOrEmpty(scope)) {
            return false;
        }
        var softwareSystem = WorkspaceUtil.findSoftwareSystem(view.getModel().getAllElements(), scope);
        return softwareSystem != null;
    }

    protected DeploymentEnvironment getDeploymentEnvironment(DeploymentView view) {
        var model = view.getModel();
        return WorkspaceUtil.getDeploymentEnvironment(model.getDeploymentEnvironments(), view.getEnvironment());
    }

    protected boolean isInBaseScope(DeploymentView view, Element element) {
        if (isScopeAll(view)) {
            return WorkspaceUtil.isContainerInstance(element) || WorkspaceUtil.isSoftwareSystemInstance(element) || WorkspaceUtil.isContainer(element) || WorkspaceUtil.isSoftwareSystem(element);
        }
        if (isScopeSoftwareSystem(view)) {
            if (WorkspaceUtil.isContainer(element)) {
                var curr = (Container) element;
                return Entity.equals(curr.getSoftwareSystem().getId(), view.getScope());
            }
            if (WorkspaceUtil.isContainerInstance(element)) {
                var curr = (ContainerInstance) element;
                var container = curr.getContainer();
                return Entity.equals(container.getSoftwareSystem().getId(), view.getScope());
            }
            if (WorkspaceUtil.isSoftwareSystem(element)) {
                var curr = (SoftwareSystem) element;
                return Entity.equals(curr.getId(), view.getScope());
            }
            if (WorkspaceUtil.isSoftwareSystemInstance(element)) {
                var curr = (SoftwareSystemInstance) element;
                var softwareSystem = curr.getSoftwareSystem();
                return Entity.equals(softwareSystem.getId(), view.getScope());
            }
        }
        return false;
    }


    protected boolean isInScope(DeploymentView view, List<Element> baseScope, Element element) {
        // if it's already in scope, keep it
        if (baseScope.contains(element)) {
            log.debug("View {}, {} is in scope", view.getName(), element.toShortString());
            return true;
        }
        // if it is parent of anything in scope, it's in scope
        var children = element.getAllChildren();
        var firstMatchedChild = children.filter(baseScope::contains).findFirst().orElse(null);
        if (firstMatchedChild != null) {
            log.debug("View {}, {} is in scope because it's parent of {}", view.getName(), element.toShortString(), firstMatchedChild.toShortString());
            return true;
        }
        // if there's a direct connection with anything in scope, it's in scope
        if (baseScope.stream().anyMatch(scope -> hasDistance1(view.getModel(), scope, element))) {
            log.debug("View {}, {} is in scope because it's connected to at least one element in scope", view.getName(), element.toShortString());
            return true;
        }
        return false;
    }

    protected boolean hasDistance1(Model model, Element element1, Element element2) {
        var relations = model.getRelations().getRelationsBetween(element1, element2);
        log.debug("Relations between {} and {}: {}", element1.toShortString(), element2.toShortString(), relations.size());
        return !relations.isEmpty();
    }

    protected boolean isInScope(DeploymentView view, List<Element> elementsInScope, Relation relation) {
        if (!elementsInScope.contains(relation.getFrom())) {
            return false;
        }
        if (!elementsInScope.contains(relation.getTo())) {
            return false;
        }
        return true;
    }

}
