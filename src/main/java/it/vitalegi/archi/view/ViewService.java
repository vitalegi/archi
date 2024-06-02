package it.vitalegi.archi.view;

import it.vitalegi.archi.view.dto.View;
import it.vitalegi.archi.view.renderer.DeploymentViewRenderer;
import it.vitalegi.archi.view.renderer.ViewFormat;
import it.vitalegi.archi.view.renderer.ViewRenderer;
import it.vitalegi.archi.view.validator.DeploymentViewValidator;
import it.vitalegi.archi.view.validator.ViewValidator;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class ViewService implements ViewValidator, ViewRenderer {
    private static final List<ViewRenderer> RENDERERS = Arrays.asList(new DeploymentViewRenderer());
    private static final List<ViewValidator> VALIDATORS = Arrays.asList(new DeploymentViewValidator());

    @Override
    public void render(View view, Path basePath, ViewFormat[] formats) {
        RENDERERS.forEach(r -> r.render(view, basePath, formats));
    }

    @Override
    public void validate(View view) {
        VALIDATORS.forEach(v -> v.validate(view));
    }
}
