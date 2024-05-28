package it.vitalegi.archi.workspace.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import it.vitalegi.archi.model.Container;
import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.Person;
import it.vitalegi.archi.model.SoftwareSystem;
import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.workspace.loader.model.ElementType;
import it.vitalegi.archi.workspace.loader.model.ElementYaml;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileSystemWorkspaceLoader {
    public Workspace load(Path path) {
        var ws = readFile(path);
        return map(ws);
    }

    protected it.vitalegi.archi.workspace.loader.model.Workspace readFile(Path path) {
        var mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        try {
            return mapper.readValue(path.toFile(), it.vitalegi.archi.workspace.loader.model.Workspace.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Workspace map(it.vitalegi.archi.workspace.loader.model.Workspace ws) {
        Workspace out = new Workspace();

        var people = ws.getElements().stream().filter(this::isPerson).map(this::toPerson).collect(Collectors.toList());
        out.getElements().addAll(people);

        var softwareSystems = ws.getElements().stream().filter(this::isSoftwareSystem).map(this::toSoftwareSystem).collect(Collectors.toList());
        out.getElements().addAll(softwareSystems);

        ws.getElements().stream().filter(this::isContainer).map(e -> toContainer(e, softwareSystems)).collect(Collectors.toList());

        return out;
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

    protected Person toPerson(ElementYaml element) {
        var out = new Person();
        applyCommonProperties(element, out);
        return out;
    }

    protected SoftwareSystem toSoftwareSystem(ElementYaml element) {
        var out = new SoftwareSystem();
        applyCommonProperties(element, out);
        out.setContainers(new ArrayList<>());
        return out;
    }

    protected Container toContainer(ElementYaml element, List<SoftwareSystem> softwareSystems) {
        var out = new Container();
        applyCommonProperties(element, out);
        var targetId = element.getParentId();
        var parent = softwareSystems.stream().filter(s -> Objects.equals(s.getId(), targetId)).findFirst().orElseThrow(() -> new NoSuchElementException("Can't find a software system with id " + targetId));
        parent.getContainers().add(out);
        out.setParent(parent);
        return out;
    }

    protected void applyCommonProperties(ElementYaml in, Element out) {
        out.setId(in.getId());
        out.setName(in.getName());
        out.setDescription(in.getDescription());
        out.setTags(in.getTags());
        out.setMetadata(in.getMetadata());
    }
}
