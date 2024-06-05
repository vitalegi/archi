package it.vitalegi.archi.view;

import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.Relation;
import it.vitalegi.archi.plantuml.PlantUmlExporter;
import it.vitalegi.archi.view.constant.ViewFormat;
import it.vitalegi.archi.view.dto.DeploymentView;
import it.vitalegi.archi.view.dto.View;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractViewProcessor<E extends View> implements ViewProcessor {

    @Override
    public void validate(View view) {
        doValidate(cast(view));
    }

    @Override
    public void render(View view, Path basePath, ViewFormat[] formats) {
        var pumlDiagram = createPuml(cast(view));
        var exporter = new PlantUmlExporter();
        exporter.export(basePath, view.getName(), formats, pumlDiagram);
    }

    protected abstract void doValidate(E view);

    protected abstract E cast(View view);

    protected abstract String createPuml(E view);

    protected String formatTags(Element element) {
        return formatTags(element.getTags());
    }

    protected String formatTags(Relation relation) {
        return formatTags(relation.getTags());
    }

    protected String formatTags(List<String> tags) {
        if (tags == null) {
            return null;
        }
        return tags.stream().collect(Collectors.joining(","));
    }

    protected String getAlias(Element element) {
        var alias = element.getUniqueId();
        alias = alias.replace('-', '_');
        alias = alias.replace('.', '_');
        alias = alias.replace(' ', '_');
        return alias;
    }

}
