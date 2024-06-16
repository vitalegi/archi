package it.vitalegi.archi.exporter.c4.plantuml;

import it.vitalegi.archi.exporter.c4.plantuml.constants.LayoutDirection;
import it.vitalegi.archi.exporter.c4.plantuml.writer.C4PlantumlWriter;
import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.Diagram;
import it.vitalegi.archi.model.diagramelement.C4DiagramElement;
import it.vitalegi.archi.model.diagramelement.C4DiagramModel;
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

    public String export(Workspace workspace, E diagram) {
        var writer = new C4PlantumlWriter();
        writeHeader(workspace, diagram, writer);
        writeStyles(workspace, diagram, writer);

        var model = buildModel(workspace, diagram);
        writeElements(diagram, model, writer);
        writeRelations(diagram, model, writer);

        writeFooter(diagram, writer);
        return writer.build();
    }

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

    protected abstract C4DiagramModel buildModel(Workspace workspace, E diagram);

    protected void writeElements(E diagram, C4DiagramModel model, C4PlantumlWriter writer) {
        var topLevelElements = model.getTopLevelElements();
        for (var element : topLevelElements) {
            writeElementsTree(diagram, model, element, writer);
        }
    }

    protected void writeElementsTree(E diagram, C4DiagramModel model, C4DiagramElement element, C4PlantumlWriter writer) {
        if (!element.getChildren().isEmpty()) {
            writeAsNode(diagram, model, element, writer);
        } else {
            writeAsLeaf(diagram, model, element, writer);
        }
    }

    protected void writeAsNode(E diagram, C4DiagramModel model, C4DiagramElement element, C4PlantumlWriter writer) {
        writer.boundaryStart(element);
        writeChildren(diagram, model, element, writer);
        writer.boundaryEnd();
    }

    protected void writeChildren(E diagram, C4DiagramModel model, C4DiagramElement element, C4PlantumlWriter writer) {
        for (var child : element.getChildren()) {
            writeElementsTree(diagram, model, child, writer);
        }
    }

    protected void writeAsLeaf(E diagram, C4DiagramModel model, C4DiagramElement element, C4PlantumlWriter writer) {
        writer.container(element);
    }

    protected void writeRelations(E diagram, C4DiagramModel model, C4PlantumlWriter writer) {
        for (var relation : model.getRelations()) {
            writer.addRelation(relation);
        }
    }
}
