package it.vitalegi.archi.view.validator;

import it.vitalegi.archi.exception.ElementNotFoundException;
import it.vitalegi.archi.util.StringUtil;
import it.vitalegi.archi.view.Scope;
import it.vitalegi.archi.view.ScopeLevel;
import it.vitalegi.archi.view.dto.DeploymentView;
import it.vitalegi.archi.view.dto.View;

import java.util.Arrays;

public class DeploymentViewValidator implements ViewValidator {

    private final static Scope SCOPE_VALIDATOR = new Scope(Arrays.asList(ScopeLevel.ALL, ScopeLevel.SOFTWARE_SYSTEM));

    @Override
    public void validate(View view) {
        if (view instanceof DeploymentView) {
            doValidate((DeploymentView) view);
        }
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

        SCOPE_VALIDATOR.validate(view, view.getScope());
    }

}
