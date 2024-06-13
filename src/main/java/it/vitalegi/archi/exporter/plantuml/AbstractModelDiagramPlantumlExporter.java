package it.vitalegi.archi.exporter.plantuml;

import it.vitalegi.archi.diagram.scope.DiagramScopeBuilder;
import it.vitalegi.archi.exporter.plantuml.builder.C4ModelBuilder;
import it.vitalegi.archi.exporter.plantuml.writer.C4PlantumlWriter;
import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.Diagram;
import it.vitalegi.archi.model.diagramelement.C4DiagramElement;
import it.vitalegi.archi.model.diagramelement.C4DiagramModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractModelDiagramPlantumlExporter<E extends Diagram> extends AbstractDiagramPlantumlExporter<E> {

    @Override
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

    protected C4DiagramModel buildModel(Workspace workspace, E diagram) {
        var scope = diagramScope(workspace, diagram).computeScope();
        return new C4ModelBuilder(workspace, diagram, scope).build();
    }

    protected abstract DiagramScopeBuilder<E> diagramScope(Workspace workspace, E diagram);

    protected void writeElements(E diagram, C4DiagramModel model, C4PlantumlWriter writer) {
        var topLevelElements = model.getTopLevelElements();
        for (var element : topLevelElements) {
            writeElementsTree(diagram, model, element, writer);
        }
    }

    protected void writeElementsTree(E diagram, C4DiagramModel model, C4DiagramElement element, C4PlantumlWriter writer) {
        if (!element.getChildren().isEmpty()) {
            writeAsBoundary(diagram, model, element, writer);
        } else {
            writer.container(element);
        }
    }

    protected void writeAsBoundary(E diagram, C4DiagramModel model, C4DiagramElement element, C4PlantumlWriter writer) {
        writer.boundaryStart(element);
        for (var child : element.getChildren()) {
            writeElementsTree(diagram, model, child, writer);
        }
        writer.boundaryEnd();
    }

    protected void writeRelations(E diagram, C4DiagramModel model, C4PlantumlWriter writer) {
        for (var relation : model.getRelations()) {
            writer.addRelation(relation);
        }
    }
}
