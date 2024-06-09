package it.vitalegi.archi.model.builder;

import it.vitalegi.archi.model.Component;
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
import it.vitalegi.archi.model.SoftwareSystem;
import it.vitalegi.archi.model.SoftwareSystemInstance;
import it.vitalegi.archi.util.WorkspaceUtil;
import it.vitalegi.archi.workspaceloader.model.ElementRaw;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ElementFactory {
    Model model;
    ElementRaw source;

    public Element build() {
        if (source.getType() == null) {
            throw new NullPointerException("Element type is null");
        }
        return switch (source.getType()) {
            case PERSON -> toPerson();
            case SOFTWARE_SYSTEM -> toSoftwareSystem();
            case CONTAINER -> toContainer();
            case COMPONENT -> toComponent();
            case GROUP -> toGroup();
            case DEPLOYMENT_ENVIRONMENT -> toDeploymentEnvironment();
            case DEPLOYMENT_NODE -> toDeploymentNode();
            case CONTAINER_INSTANCE -> toContainerInstance();
            case SOFTWARE_SYSTEM_INSTANCE -> toSoftwareSystemInstance();
            case INFRASTRUCTURE_NODE -> toInfrastructureNode();
            case RELATION -> throw new IllegalArgumentException("RELATION is not a ");
        };
    }

    protected Exception invalidType(ElementType type) {
        return new IllegalArgumentException("Type " + type + " is not supported on elements");
    }

    protected Person toPerson() {
        var out = new Person(model);
        applyCommonProperties(source, out);
        return out;
    }

    protected SoftwareSystem toSoftwareSystem() {
        var out = new SoftwareSystem(model);
        applyCommonProperties(source, out);
        return out;
    }

    protected Container toContainer() {
        var out = new Container(model);
        applyCommonProperties(source, out);
        return out;
    }

    protected Component toComponent() {
        var out = new Component(model);
        applyCommonProperties(source, out);
        return out;
    }

    protected Group toGroup() {
        var out = new Group(model);
        applyCommonProperties(source, out);
        return out;
    }

    protected DeploymentEnvironment toDeploymentEnvironment() {
        var out = new DeploymentEnvironment(model);
        applyCommonProperties(source, out);
        return out;
    }

    protected DeploymentNode toDeploymentNode() {
        var out = new DeploymentNode(model);
        applyCommonProperties(source, out);
        return out;
    }

    protected ContainerInstance toContainerInstance() {
        var out = new ContainerInstance(model, source.getContainerId());
        applyCommonProperties(source, out);
        return out;
    }

    protected SoftwareSystemInstance toSoftwareSystemInstance() {
        var out = new SoftwareSystemInstance(model, source.getSoftwareSystemId());
        applyCommonProperties(source, out);
        return out;
    }


    protected InfrastructureNode toInfrastructureNode() {
        var out = new InfrastructureNode(model);
        applyCommonProperties(source, out);
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
}
