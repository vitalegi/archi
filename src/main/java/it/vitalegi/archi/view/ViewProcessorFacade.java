package it.vitalegi.archi.view;

import it.vitalegi.archi.view.constant.ViewFormat;
import it.vitalegi.archi.view.dto.View;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ViewProcessorFacade {
    private static final List<ViewProcessor> VIEW_PROCESSORS = Arrays.asList(new DeploymentViewProcessor());

    public void render(View view, Path basePath, ViewFormat[] formats) {
        getAcceptedViewProcessors(view).forEach(r -> r.render(view, basePath, formats));
    }


    public void validate(View view) {
        getAcceptedViewProcessors(view).forEach(v -> v.validate(view));
    }

    protected List<ViewProcessor> getAcceptedViewProcessors(View view) {
        return VIEW_PROCESSORS.stream().filter(p -> p.accept(view)).collect(Collectors.toList());
    }
}
