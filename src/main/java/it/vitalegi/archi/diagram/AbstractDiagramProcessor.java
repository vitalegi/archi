package it.vitalegi.archi.diagram;

import it.vitalegi.archi.diagram.constant.DiagramFormat;
import it.vitalegi.archi.diagram.model.Diagram;
import it.vitalegi.archi.diagram.style.StyleHandler;
import it.vitalegi.archi.diagram.writer.C4PlantUMLWriter;
import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.Relation;
import it.vitalegi.archi.plantuml.LayoutDirection;
import it.vitalegi.archi.plantuml.PlantUmlExporter;
import it.vitalegi.archi.util.StringUtil;
import it.vitalegi.archi.workspace.Workspace;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractDiagramProcessor<E extends Diagram> implements DiagramProcessor {

    StyleHandler styleHandler;

    public AbstractDiagramProcessor(StyleHandler styleHandler) {
        this.styleHandler = styleHandler;
    }

    @Override
    public void validate(Diagram diagram) {
        // TODO validate String name
        doValidate(cast(diagram));
    }

    @Override
    public void render(Workspace workspace, Diagram diagram, Path basePath, DiagramFormat[] formats) {
        var pumlDiagram = createPuml(workspace, cast(diagram));
        var exporter = new PlantUmlExporter();
        exporter.export(basePath, diagram.getName(), formats, pumlDiagram);
    }

    protected abstract void doValidate(E diagram);

    protected abstract E cast(Diagram diagram);

    protected abstract String createPuml(Workspace workspace, E diagram);

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

    protected void writeHeader(Workspace workspace, E diagram, C4PlantUMLWriter writer) {
        writeStart(diagram, writer);
        writeProperties(diagram, writer);
        writeSkinParams(workspace, diagram, writer);
        writeDirection(diagram, writer);
        writeTitle(diagram, writer);
        writeIncludes(writer);
    }

    protected void writeFooter(E diagram, C4PlantUMLWriter writer) {
        writer.hideStereotypes();
        writer.enduml();
    }

    protected void writeStart(E diagram, C4PlantUMLWriter writer) {
        writer.startuml();
    }

    protected void writeProperties(E diagram, C4PlantUMLWriter writer) {
        writer.set("separator", "none");
    }

    protected void writeDirection(E diagram, C4PlantUMLWriter writer) {
        //TODO
        writer.direction(LayoutDirection.TOP_TO_BOTTOM);
    }

    protected void writeTitle(E diagram, C4PlantUMLWriter writer) {
        if (StringUtil.isNotNullOrEmpty(diagram.getTitle())) {
            writer.title(diagram.getTitle());
        }
    }

    protected void writeIncludes(C4PlantUMLWriter writer) {
        writer.include("<C4/C4>");
        writer.include("<C4/C4_Context>");
        writer.include("<C4/C4_Container>");
    }


    protected void writeStyles(E diagram, C4PlantUMLWriter writer) {
        writer.addElementTag("Element", "#ffffff", "#888888", "#000000", "", "", "solid");
        writer.addElementTag("Container", "#006daa", "#004c76", "#000000", "", "", "solid");
        writer.addRelTag("Relationship", "#707070", "#707070", "");
    }

    protected void writeSkinParams(Workspace workspace, E diagram, C4PlantUMLWriter writer) {
        var style = styleHandler.buildStyle(workspace, diagram);
        var skinParams = style.getSkinParams();
        for (var skinParam : skinParams) {
            writer.skinParam(skinParam.getKey(), skinParam.getValue());
        }
    }
}
