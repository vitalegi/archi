package it.vitalegi.archi.exporter.plantuml;

import it.vitalegi.archi.exporter.plantuml.constants.LayoutDirection;
import it.vitalegi.archi.exporter.plantuml.writer.C4PlantumlWriter;
import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.Diagram;
import it.vitalegi.archi.model.style.Style;
import it.vitalegi.archi.model.style.Tags;
import it.vitalegi.archi.util.StringUtil;

import java.util.ArrayList;

public abstract class AbstractDiagramPlantumlExporter<E extends Diagram> {

    protected static Style defaultStyle() {
        var style = new Style();
        style.setSkinParams(new ArrayList<>());
        style.setTags(new Tags());
        return style;
    }

    public abstract void validate(E diagram);

    public abstract String export(Workspace workspace, E diagram);

    protected void writeHeader(Workspace workspace, E diagram, C4PlantumlWriter writer) {
        writeStart(diagram, writer);
        writeProperties(diagram, writer);
        writeSkinParams(workspace, diagram, writer);
        writeDirection(diagram, writer);
        writeTitle(diagram, writer);
        writeIncludes(writer);
    }

    protected void writeFooter(E diagram, C4PlantumlWriter writer) {
        writer.hideStereotypes();
        writer.enduml();
    }

    protected void writeStart(E diagram, C4PlantumlWriter writer) {
        writer.startuml();
    }

    protected void writeProperties(E diagram, C4PlantumlWriter writer) {
        writer.set("separator", "none");
    }

    protected void writeDirection(E diagram, C4PlantumlWriter writer) {
        //TODO
        writer.direction(LayoutDirection.TOP_TO_BOTTOM);
    }

    protected void writeTitle(E diagram, C4PlantumlWriter writer) {
        if (StringUtil.isNotNullOrEmpty(diagram.getTitle())) {
            writer.title(diagram.getTitle());
        }
    }

    protected void writeIncludes(C4PlantumlWriter writer) {
        writer.include("<C4/C4>");
        writer.include("<C4/C4_Context>");
        writer.include("<C4/C4_Container>");
    }

    protected void writeStyles(Workspace workspace, E diagram, C4PlantumlWriter writer) {
        var style = buildStyle(workspace, diagram);
        style.getTags().getElements().forEach(writer::addElementTag);
        style.getTags().getRelations().forEach(writer::addRelTag);
    }

    protected void writeSkinParams(Workspace workspace, E diagram, C4PlantumlWriter writer) {
        var style = buildStyle(workspace, diagram);
        var skinParams = style.getSkinParams();
        for (var skinParam : skinParams) {
            writer.skinParam(skinParam.getKey(), skinParam.getValue());
        }
    }

    protected Style buildStyle(Workspace workspace, Diagram diagram) {
        var defaultStyle = defaultStyle();
        var style1 = workspace.getStyle();
        var style2 = diagram.getStyle();
        return defaultStyle.merge(style1).merge(style2);
    }

}
