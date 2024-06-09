package it.vitalegi.archi.workspaceloader;

import it.vitalegi.archi.diagram.DiagramProcessorFacade;
import it.vitalegi.archi.exception.CycleNotAllowedException;
import it.vitalegi.archi.model.Container;
import it.vitalegi.archi.model.ContainerInstance;
import it.vitalegi.archi.model.DeploymentEnvironment;
import it.vitalegi.archi.model.DeploymentNode;
import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.ElementType;
import it.vitalegi.archi.model.Group;
import it.vitalegi.archi.model.InfrastructureNode;
import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.model.Person;
import it.vitalegi.archi.model.Relation;
import it.vitalegi.archi.model.SoftwareSystem;
import it.vitalegi.archi.model.SoftwareSystemInstance;
import it.vitalegi.archi.style.model.Style;
import it.vitalegi.archi.util.StringUtil;
import it.vitalegi.archi.util.WorkspaceUtil;
import it.vitalegi.archi.workspace.Workspace;
import it.vitalegi.archi.workspaceloader.model.ElementRaw;
import it.vitalegi.archi.workspaceloader.model.RelationRaw;
import it.vitalegi.archi.workspaceloader.model.WorkspaceRaw;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
public class WorkspaceLoader {
    DiagramProcessorFacade diagramProcessorFacade;

    public WorkspaceLoader(DiagramProcessorFacade diagramProcessorFacade) {
        this.diagramProcessorFacade = diagramProcessorFacade;
    }

    public Workspace load(WorkspaceRaw in) {
        Workspace workspace = new Workspace();
        var model = workspace.getModel();
        log.debug("Load model");
        try {
            var pairs = new ArrayList<>(in.getElements().stream().map(e -> toPair(model, e)).collect(Collectors.toList()));
            while (!pairs.isEmpty()) {
                log.debug("New cycle");
                boolean anyProcessed = false;
                for (var i = 0; i < pairs.size(); i++) {
                    if (apply(workspace, pairs.get(i))) {
                        pairs.remove(i);
                        i--;
                        anyProcessed = true;
                    }
                }
                if (!anyProcessed) {
                    var unresolved = pairs.stream().map(ElementPair::getSource).map(s -> new CycleNotAllowedException.UnresolvedDependency(s.getId(), s.getParentId())).collect(Collectors.toList());
                    throw new CycleNotAllowedException(model.getAllElements().stream().map(Element::getId).collect(Collectors.toList()), unresolved);
                }
            }
            log.debug("Load relations");
            in.getRelations().stream().map(r -> toRelation(r, model)).forEach(model::addRelation);
            workspace.validate();

            log.debug("Load diagrams");
            var visitor = new DiagramRawMapperVisitor(model);
            in.getDiagrams().stream() //
                    .map(diagram -> diagram.visit(visitor)) //
                    .forEach(diagram -> workspace.getDiagrams().add(diagram));
            workspace.getDiagrams().getAll().forEach(diagramProcessorFacade::validate);

            loadGlobalStyle(workspace, in);

            return workspace;
        } catch (Throwable e) {
            model.getAllElements().forEach(element -> log.info("> {}: {}", element.toShortString(), element));
            throw e;
        }
    }

    protected boolean apply(Workspace out, ElementPair pair) {
        var parentId = pair.getSource().getParentId();
        if (StringUtil.isNullOrEmpty(parentId)) {
            out.getModel().addChild(pair.getOut());
            log.debug("Add actor to top level: {}", pair.getOut().getId());
            return true;
        }
        var parent = out.getModel().getElementById(parentId);
        if (parent == null) {
            return false;
        }
        parent.addChild(pair.getOut());
        return true;
    }

    protected ElementPair toPair(Model model, ElementRaw source) {
        if (isPerson(source)) {
            return new ElementPair(source, toPerson(model, source));
        }
        if (isSoftwareSystem(source)) {
            return new ElementPair(source, toSoftwareSystem(model, source));
        }
        if (isContainer(source)) {
            return new ElementPair(source, toContainer(model, source));
        }
        if (isGroup(source)) {
            return new ElementPair(source, toGroup(model, source));
        }
        if (isDeploymentEnvironment(source)) {
            return new ElementPair(source, toDeploymentEnvironment(model, source));
        }
        if (isDeploymentNode(source)) {
            return new ElementPair(source, toDeploymentNode(model, source));
        }
        if (isContainerInstance(source)) {
            return new ElementPair(source, toContainerInstance(model, source));
        }
        if (isSoftwareSystemInstance(source)) {
            return new ElementPair(source, toSoftwareSystemInstance(model, source));
        }
        if (isInfrastructureNode(source)) {
            return new ElementPair(source, toInfrastructureNode(model, source));
        }
        throw new RuntimeException("Can't process " + source);
    }

    protected boolean isPerson(ElementRaw element) {
        if (element.getType() == null) {
            throw new NullPointerException("Element type is null");
        }
        return element.getType() == ElementType.PERSON;
    }

    protected boolean isSoftwareSystem(ElementRaw element) {
        if (element.getType() == null) {
            throw new NullPointerException("Element type is null");
        }
        return element.getType() == ElementType.SOFTWARE_SYSTEM;
    }

    protected boolean isContainer(ElementRaw element) {
        if (element.getType() == null) {
            throw new NullPointerException("Element type is null");
        }
        return element.getType() == ElementType.CONTAINER;
    }

    protected boolean isGroup(ElementRaw element) {
        if (element.getType() == null) {
            throw new NullPointerException("Element type is null");
        }
        return element.getType() == ElementType.GROUP;
    }

    protected boolean isDeploymentEnvironment(ElementRaw element) {
        if (element.getType() == null) {
            throw new NullPointerException("Element type is null");
        }
        return element.getType() == ElementType.DEPLOYMENT_ENVIRONMENT;
    }

    protected boolean isDeploymentNode(ElementRaw element) {
        if (element.getType() == null) {
            throw new NullPointerException("Element type is null");
        }
        return element.getType() == ElementType.DEPLOYMENT_NODE;
    }

    protected boolean isContainerInstance(ElementRaw element) {
        if (element.getType() == null) {
            throw new NullPointerException("Element type is null");
        }
        return element.getType() == ElementType.CONTAINER_INSTANCE;
    }

    protected boolean isSoftwareSystemInstance(ElementRaw element) {
        if (element.getType() == null) {
            throw new NullPointerException("Element type is null");
        }
        return element.getType() == ElementType.SOFTWARE_SYSTEM_INSTANCE;
    }

    protected Person toPerson(Model model, ElementRaw element) {
        var out = new Person(model);
        applyCommonProperties(element, out);
        return out;
    }

    protected SoftwareSystem toSoftwareSystem(Model model, ElementRaw element) {
        var out = new SoftwareSystem(model);
        applyCommonProperties(element, out);
        return out;
    }

    protected Container toContainer(Model model, ElementRaw element) {
        var out = new Container(model);
        applyCommonProperties(element, out);
        return out;
    }

    protected Group toGroup(Model model, ElementRaw element) {
        var out = new Group(model);
        applyCommonProperties(element, out);
        return out;
    }

    protected DeploymentEnvironment toDeploymentEnvironment(Model model, ElementRaw element) {
        var out = new DeploymentEnvironment(model);
        applyCommonProperties(element, out);
        return out;
    }

    protected DeploymentNode toDeploymentNode(Model model, ElementRaw element) {
        var out = new DeploymentNode(model);
        applyCommonProperties(element, out);
        return out;
    }

    protected ContainerInstance toContainerInstance(Model model, ElementRaw element) {
        var out = new ContainerInstance(model, element.getContainerId());
        applyCommonProperties(element, out);
        return out;
    }

    protected SoftwareSystemInstance toSoftwareSystemInstance(Model model, ElementRaw element) {
        var out = new SoftwareSystemInstance(model, element.getSoftwareSystemId());
        applyCommonProperties(element, out);
        return out;
    }


    protected InfrastructureNode toInfrastructureNode(Model model, ElementRaw element) {
        var out = new InfrastructureNode(model);
        applyCommonProperties(element, out);
        return out;
    }


    protected void applyCommonProperties(ElementRaw in, Element out) {
        out.setId(in.getId());
        out.setName(in.getName());
        out.setDescription(in.getDescription());
        out.setTags(in.getTags());
        out.setMetadata(in.getMetadata());
        out.setUniqueId(WorkspaceUtil.createUniqueId(out));
    }

    protected boolean isInfrastructureNode(ElementRaw element) {
        if (element.getType() == null) {
            throw new NullPointerException("Element type is null");
        }
        return element.getType() == ElementType.INFRASTRUCTURE_NODE;
    }

    protected Relation toRelation(RelationRaw in, Model model) {
        var out = new Relation(model);
        out.setId(in.getId());
        var from = model.getElementById(in.getFrom());
        if (from == null) {
            throw new NoSuchElementException("Element " + in.getFrom() + " doesn't exist. " + in);
        }
        out.setFrom(from);
        var to = model.getElementById(in.getTo());
        if (to == null) {
            throw new NoSuchElementException("Element " + in.getTo() + " doesn't exist. " + in);
        }
        out.setTo(to);
        out.setDescription(in.getDescription());
        out.setTags(in.getTags());
        out.setMetadata(in.getMetadata());
        out.setUniqueId(WorkspaceUtil.createUniqueId(out));
        return out;
    }

    @AllArgsConstructor
    @Data
    protected static class ElementPair {
        ElementRaw source;
        Element out;
    }

    protected void loadGlobalStyle(Workspace workspace, WorkspaceRaw in) {
        log.debug("Load global styles");
        if (in.getStyle() != null) {
            workspace.setStyle(in.getStyle());
        } else {
            workspace.setStyle(Style.builder().build());
        }
        workspace.getStyle().validate();
    }

}
