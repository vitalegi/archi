package it.vitalegi.archi.model.builder;

import it.vitalegi.archi.exception.CycleNotAllowedException;
import it.vitalegi.archi.model.Model;
import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.Diagrams;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.relation.Relation;
import it.vitalegi.archi.model.style.Style;
import it.vitalegi.archi.util.StringUtil;
import it.vitalegi.archi.util.WorkspaceUtil;
import it.vitalegi.archi.workspaceloader.DiagramRawMapperVisitor;
import it.vitalegi.archi.workspaceloader.model.DiagramRaw;
import it.vitalegi.archi.workspaceloader.model.ElementRaw;
import it.vitalegi.archi.workspaceloader.model.RelationRaw;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Getter
@Slf4j
public class WorkspaceBuilder {

    Workspace workspace;
    RelationValidator relationValidator = new RelationValidator();

    public void buildWorkspace() {
        workspace = new Workspace();
        workspace.setModel(new Model());
        workspace.setStyle(new Style());
        workspace.setDiagrams(new Diagrams());
    }

    public void buildElements(List<ElementRaw> elements) {
        log.debug("Load model");
        var model = workspace.getModel();
        try {
            var pairs = new ArrayList<>(elements.stream().map(e -> toPair(model, e)).collect(Collectors.toList()));
            while (!pairs.isEmpty()) {
                log.debug("New cycle");
                boolean anyProcessed = false;
                for (var i = 0; i < pairs.size(); i++) {
                    if (apply(workspace, pairs.get(i))) {
                        pairs.remove(i);
                        i--;
                        anyProcessed = true;
                    }
                }
                if (!anyProcessed) {
                    var unresolved = pairs.stream().map(ElementPair::getSource).map(s -> new CycleNotAllowedException.UnresolvedDependency(s.getId(), s.getParentId())).collect(Collectors.toList());
                    throw new CycleNotAllowedException(model.getAllElements().stream().map(Element::getId).collect(Collectors.toList()), unresolved);
                }
            }
        } catch (Throwable e) {
            model.getAllElements().forEach(element -> log.info("> {}: {}", element.toShortString(), element));
            throw e;
        }
    }

    public void buildRelations(List<RelationRaw> relations) {
        log.debug("Load relations");
        var model = workspace.getModel();
        relations.stream().map(r -> toRelation(r, model)).forEach(r -> {
            relationValidator.checkAllowed(r);
            model.getRelations().addRelation(r);
        });
    }

    public void buildDiagrams(List<DiagramRaw> diagrams) {
        log.debug("Load diagrams");
        var model = workspace.getModel();
        var visitor = new DiagramRawMapperVisitor(model);
        diagrams.stream() //
                .map(diagram -> diagram.visit(visitor)) //
                .forEach(diagram -> workspace.getDiagrams().add(diagram));
    }

    protected void buildGlobalStyle(Style style) {
        log.debug("Load global styles");
        if (style != null) {
            workspace.setStyle(style.duplicate());
        } else {
            workspace.setStyle(Style.builder().build());
        }
    }

    protected boolean apply(Workspace out, ElementPair pair) {
        var parentId = pair.getSource().getParentId();
        var addChildToParentVisitor = new AddElementToParentVisitor(out, pair.getOut());
        if (StringUtil.isNullOrEmpty(parentId)) {
            out.getModel().visit(addChildToParentVisitor);
            log.debug("Add actor to top level: {}", pair.getOut().getId());
            return true;
        }
        var parent = out.getModel().getElementById(parentId);
        if (parent == null) {
            return false;
        }
        parent.visit(addChildToParentVisitor);
        return true;
    }

    protected ElementPair toPair(Model model, ElementRaw source) {
        var factory = new ElementFactory(model, source);
        var element = factory.build();
        return new ElementPair(source, element);
    }

    protected Relation toRelation(RelationRaw in, Model model) {
        var out = new Relation(model);
        out.setId(in.getId());
        var from = model.getElementById(in.getFrom());
        if (from == null) {
            throw new NoSuchElementException("Element " + in.getFrom() + " doesn't exist. " + in);
        }
        out.setFrom(from);
        var to = model.getElementById(in.getTo());
        if (to == null) {
            throw new NoSuchElementException("Element " + in.getTo() + " doesn't exist. " + in);
        }
        out.setTo(to);
        out.setDescription(in.getDescription());
        out.setTags(in.getTags());
        out.setMetadata(in.getMetadata());
        out.setLabel(in.getLabel());
        out.setSprite(in.getSprite());
        out.setLink(in.getLink());
        out.setTechnologies(in.getTechnologies());
        out.setUniqueId(WorkspaceUtil.createUniqueId(out));
        return out;
    }

    @AllArgsConstructor
    @Data
    protected static class ElementPair {
        ElementRaw source;
        Element out;
    }

}
