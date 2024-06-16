package it.vitalegi.archi.model.diagram;

import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.model.element.DeploymentEnvironment;
import it.vitalegi.archi.util.StringUtil;
import it.vitalegi.archi.util.WorkspaceUtil;
import it.vitalegi.archi.visitor.DiagramVisitor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeploymentDiagram extends Diagram {
    public static final String ALL = "*";

    String scope;
    String environment;

    public DeploymentDiagram(Model model) {
        super(model);
    }

    @Override
    public <E> E visit(DiagramVisitor<E> visitor) {
        return visitor.visitDeploymentDiagram(this);
    }


    public boolean isScopeAll() {
        return StringUtil.isNullOrEmpty(scope) || ALL.equals(scope.trim());
    }

    public boolean isScopeSoftwareSystem() {
        if (StringUtil.isNullOrEmpty(scope)) {
            return false;
        }
        var softwareSystem = WorkspaceUtil.findSoftwareSystem(getModel().getAllElements(), scope);
        return softwareSystem != null;
    }

    public DeploymentEnvironment deploymentEnvironment() {
        return getModel().findDeploymentEnvironmentById(getEnvironment());
    }
}