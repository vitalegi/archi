package it.vitalegi.archi.workspace.loader;

import it.vitalegi.archi.exception.CycleNotAllowedException;
import it.vitalegi.archi.model.Container;
import it.vitalegi.archi.model.ContainerInstance;
import it.vitalegi.archi.model.DeploymentEnvironment;
import it.vitalegi.archi.model.DeploymentNode;
import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.Group;
import it.vitalegi.archi.model.InfrastructureNode;
import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.model.Person;
import it.vitalegi.archi.model.SoftwareSystem;
import it.vitalegi.archi.model.SoftwareSystemInstance;
import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.util.StringUtil;
import it.vitalegi.archi.util.WorkspaceUtil;
import it.vitalegi.archi.workspace.loader.model.ElementType;
import it.vitalegi.archi.workspace.loader.model.ElementYaml;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Slf4j
public class WorkspaceLoader {
    public Workspace load(it.vitalegi.archi.workspace.loader.model.Workspace in) {
        Workspace out = new Workspace();
        var pairs = new ArrayList<>(in.getElements().stream().map(e -> toPair(out.getModel(), e)).collect(Collectors.toList()));
        while (!pairs.isEmpty()) {
            log.debug("New cycle");
            boolean anyProcessed = false;
            for (var i = 0; i < pairs.size(); i++) {
                if (apply(out, pairs.get(i))) {
                    pairs.remove(i);
                    i--;
                    anyProcessed = true;
                }
            }
            if (!anyProcessed) {
                var unresolved = pairs.stream().map(ElementPair::getSource).map(s -> new CycleNotAllowedException.UnresolvedDependency(s.getId(), s.getParentId())).collect(Collectors.toList());
                throw new CycleNotAllowedException(out.getModel().getAllElements().stream().map(Element::getId).collect(Collectors.toList()), unresolved);
            }
        }
        out.validate();
        return out;
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

    protected ElementPair toPair(Model model, ElementYaml source) {
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

    protected boolean isPerson(ElementYaml element) {
        if (element.getType() == null) {
            throw new NullPointerException("Element type is null");
        }
        return element.getType() == ElementType.PERSON;
    }

    protected boolean isSoftwareSystem(ElementYaml element) {
        if (element.getType() == null) {
            throw new NullPointerException("Element type is null");
        }
        return element.getType() == ElementType.SOFTWARE_SYSTEM;
    }

    protected boolean isContainer(ElementYaml element) {
        if (element.getType() == null) {
            throw new NullPointerException("Element type is null");
        }
        return element.getType() == ElementType.CONTAINER;
    }

    protected boolean isGroup(ElementYaml element) {
        if (element.getType() == null) {
            throw new NullPointerException("Element type is null");
        }
        return element.getType() == ElementType.GROUP;
    }

    protected boolean isDeploymentEnvironment(ElementYaml element) {
        if (element.getType() == null) {
            throw new NullPointerException("Element type is null");
        }
        return element.getType() == ElementType.DEPLOYMENT_ENVIRONMENT;
    }

    protected boolean isDeploymentNode(ElementYaml element) {
        if (element.getType() == null) {
            throw new NullPointerException("Element type is null");
        }
        return element.getType() == ElementType.DEPLOYMENT_NODE;
    }

    protected boolean isContainerInstance(ElementYaml element) {
        if (element.getType() == null) {
            throw new NullPointerException("Element type is null");
        }
        return element.getType() == ElementType.CONTAINER_INSTANCE;
    }

    protected boolean isSoftwareSystemInstance(ElementYaml element) {
        if (element.getType() == null) {
            throw new NullPointerException("Element type is null");
        }
        return element.getType() == ElementType.SOFTWARE_SYSTEM_INSTANCE;
    }

    protected Person toPerson(Model model, ElementYaml element) {
        var out = new Person(model);
        applyCommonProperties(element, out);
        return out;
    }

    protected SoftwareSystem toSoftwareSystem(Model model, ElementYaml element) {
        var out = new SoftwareSystem(model);
        applyCommonProperties(element, out);
        return out;
    }

    protected Container toContainer(Model model, ElementYaml element) {
        var out = new Container(model);
        applyCommonProperties(element, out);
        return out;
    }

    protected Group toGroup(Model model, ElementYaml element) {
        var out = new Group(model);
        applyCommonProperties(element, out);
        return out;
    }

    protected DeploymentEnvironment toDeploymentEnvironment(Model model, ElementYaml element) {
        var out = new DeploymentEnvironment(model);
        applyCommonProperties(element, out);
        return out;
    }

    protected DeploymentNode toDeploymentNode(Model model, ElementYaml element) {
        var out = new DeploymentNode(model);
        applyCommonProperties(element, out);
        return out;
    }

    protected ContainerInstance toContainerInstance(Model model, ElementYaml element) {
        var out = new ContainerInstance(model, element.getContainerId());
        applyCommonProperties(element, out);
        return out;
    }

    protected SoftwareSystemInstance toSoftwareSystemInstance(Model model, ElementYaml element) {
        var out = new SoftwareSystemInstance(model, element.getSoftwareSystemId());
        applyCommonProperties(element, out);
        return out;
    }


    protected InfrastructureNode toInfrastructureNode(Model model, ElementYaml element) {
        var out = new InfrastructureNode(model);
        applyCommonProperties(element, out);
        return out;
    }


    protected void applyCommonProperties(ElementYaml in, Element out) {
        out.setId(in.getId());
        out.setName(in.getName());
        out.setDescription(in.getDescription());
        out.setTags(in.getTags());
        out.setMetadata(in.getMetadata());
        out.setUniqueId(WorkspaceUtil.createUniqueId(out));
    }

    protected boolean isInfrastructureNode(ElementYaml element) {
        if (element.getType() == null) {
            throw new NullPointerException("Element type is null");
        }
        return element.getType() == ElementType.INFRASTRUCTURE_NODE;
    }

    @AllArgsConstructor
    @Data
    protected static class ElementPair {
        ElementYaml source;
        Element out;
    }

}
