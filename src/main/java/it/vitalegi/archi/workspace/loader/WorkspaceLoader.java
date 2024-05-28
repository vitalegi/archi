package it.vitalegi.archi.workspace.loader;

import it.vitalegi.archi.model.Container;
import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.Group;
import it.vitalegi.archi.model.Person;
import it.vitalegi.archi.model.SoftwareSystem;
import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.util.StringUtil;
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
        var pairs = new ArrayList<>(in.getElements().stream().map(this::toPair).collect(Collectors.toList()));
        while (!pairs.isEmpty()) {
            log.info("New cycle");
            boolean anyProcessed = false;
            for (var i = 0; i < pairs.size(); i++) {
                if (apply(out, pairs.get(i))) {
                    pairs.remove(i);
                    i--;
                    anyProcessed = true;
                }
            }
            if (!anyProcessed) {
                throw new RuntimeException("Deadlock: " + pairs);
            }
        }
        return out;
    }

    protected boolean apply(Workspace out, ElementPair pair) {
        var parentId = pair.getSource().getParentId();
        if (StringUtil.isNullOrEmpty(parentId)) {
            if (pair.getOut() instanceof Person || pair.getOut() instanceof SoftwareSystem || pair.getOut() instanceof Group) {
                out.getElements().add(pair.getOut());
                log.info("Add actor to top level: {}", pair.getOut().getId());
                return true;
            }
            log.error("Can't add element to top-level {}", pair.getOut());
            throw new IllegalArgumentException("Can't add " + pair.getOut().getClass().getSimpleName() + " (" + pair.getOut().getId() + ") to top-level");
        }
        var parent = out.findChildById(parentId);
        if (parent == null) {
            return false;
        }
        parent.addChild(pair.getOut());
        return true;
    }

    protected ElementPair toPair(ElementYaml source) {
        if (isPerson(source)) {
            return new ElementPair(source, toPerson(source));
        }
        if (isSoftwareSystem(source)) {
            return new ElementPair(source, toSoftwareSystem(source));
        }
        if (isContainer(source)) {
            return new ElementPair(source, toContainer(source));
        }
        if (isGroup(source)) {
            return new ElementPair(source, toGroup(source));
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

    protected Person toPerson(ElementYaml element) {
        var out = new Person();
        applyCommonProperties(element, out);
        return out;
    }

    protected SoftwareSystem toSoftwareSystem(ElementYaml element) {
        var out = new SoftwareSystem();
        applyCommonProperties(element, out);
        return out;
    }

    protected Container toContainer(ElementYaml element) {
        var out = new Container();
        applyCommonProperties(element, out);
        //var targetId = element.getParentId();
        //var parent = softwareSystems.stream().filter(s -> Objects.equals(s.getId(), targetId)).findFirst().orElseThrow(() -> new NoSuchElementException("Can't find a software system with id " + targetId));
        //parent.getContainers().add(out);
        return out;
    }

    protected Group toGroup(ElementYaml element) {
        var out = new Group();
        applyCommonProperties(element, out);
        return out;
    }

    protected void applyCommonProperties(ElementYaml in, Element out) {
        out.setId(in.getId());
        out.setName(in.getName());
        out.setDescription(in.getDescription());
        out.setTags(in.getTags());
        out.setMetadata(in.getMetadata());
    }

    @AllArgsConstructor
    @Data
    protected static class ElementPair {
        ElementYaml source;
        Element out;
    }
}
