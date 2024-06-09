package it.vitalegi.archi.model.builder;

import it.vitalegi.archi.exporter.plantuml.PlantumlDiagramValidatorVisitor;
import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.workspaceloader.model.WorkspaceRaw;

public class WorkspaceDirector {

    WorkspaceBuilder workspaceBuilder;

    public WorkspaceDirector() {
        workspaceBuilder = new WorkspaceBuilder();
    }

    public WorkspaceDirector makeWorkspace(WorkspaceRaw in) {
        workspaceBuilder.buildWorkspace();
        workspaceBuilder.buildElements(in.getElements());
        workspaceBuilder.buildRelations(in.getRelations());
        workspaceBuilder.buildGlobalStyle(in.getStyle());
        workspaceBuilder.buildDiagrams(in.getDiagrams());

        var workspace = workspaceBuilder.getWorkspace();
        workspace.validate();
        var diagramValidator = new PlantumlDiagramValidatorVisitor();
        workspace.getDiagrams().getAll().forEach(diagram -> diagram.visit(diagramValidator));
        workspace.getStyle().validate();
        return this;
    }

    public Workspace build() {
        return workspaceBuilder.getWorkspace();
    }
}
