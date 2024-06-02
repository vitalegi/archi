package it.vitalegi.archi.view.renderer;

import it.vitalegi.archi.plantuml.LayoutDirection;
import it.vitalegi.archi.plantuml.PlantUmlExporter;
import it.vitalegi.archi.view.dto.View;
import it.vitalegi.archi.view.writer.C4PlantUMLWriter;

import java.nio.file.Path;

public class DeploymentViewRenderer implements ViewRenderer {
    @Override
    public void render(View view, Path basePath, ViewFormat[] formats) {
        var pumlDiagram = createPuml(view);
        var exporter = new PlantUmlExporter();
        exporter.export(basePath, view.getName(), formats, pumlDiagram);
    }

    protected String createPuml(View view) {
        var writer = new C4PlantUMLWriter();
        writer.startuml();
        writer.set("separator", "none");
        writer.title("TODO " + view.getName());
        writer.direction(LayoutDirection.TOP_TO_BOTTOM);

        writer.include("<C4/C4>");
        writer.include("<C4/C4_Context>");
        writer.include("<C4/C4_Container>");
        writer.include("<C4/C4_Deployment>");

        writer.addElementTag("Element", "#ffffff", "#888888", "#000000", "", "", "solid");
        writer.addElementTag("Container", "#006daa", "#004c76", "#000000", "", "", "solid");

        writer.addRelTag("Relationship", "#707070", "#707070", "");

        writer.hideStereotypes();
        writer.enduml();
        return writer.build();
    }
}
