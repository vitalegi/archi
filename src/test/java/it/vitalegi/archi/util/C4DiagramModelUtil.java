package it.vitalegi.archi.util;

import it.vitalegi.archi.model.diagramelement.C4DiagramElement;
import it.vitalegi.archi.model.diagramelement.C4DiagramModel;
import it.vitalegi.archi.model.diagramelement.C4DiagramRelation;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class C4DiagramModelUtil {
    C4DiagramModel model;

    public C4DiagramModelUtil(C4DiagramModel model) {
        this.model = model;
    }

    public C4DiagramElement findByAlias(String alias) {
        return getAllElements().filter(e -> alias.equals(e.getId())).findFirst().orElse(null);
    }

    public C4DiagramElement findByAliasesPath(String... aliases) {
        if (aliases.length == 0) {
            return null;
        }
        return findByAliasesPath(model.getTopLevelElements(), aliases);
    }


    public C4DiagramElement findByAliasesPath(List<C4DiagramElement> siblings, String... aliases) {
        if (aliases.length == 0) {
            return null;
        }
        var alias = aliases[0];
        if (aliases.length == 1) {
            return siblings.stream() //
                    .filter(e -> e.getId().equals(alias)) //
                    .findFirst().orElse(null);
        }
        var upcomingAliases = Arrays.copyOfRange(aliases, 1, aliases.length);
        return siblings.stream() //
                .filter(e -> e.getId().equals(alias)) //
                .map(e -> findByAliasesPath(e.getChildren(), upcomingAliases)) //
                .filter(Objects::nonNull) //
                .findFirst().orElse(null);
    }

    public long countElements() {
        return getAllElements().count();
    }

    public Stream<C4DiagramElement> getAllElements() {
        return model.getTopLevelElements().stream().flatMap(e -> Stream.concat(Stream.of(e), getAllChildren(e)));
    }

    public Stream<C4DiagramElement> getAllChildren(C4DiagramElement element) {
        return element.getChildren().stream().flatMap(child -> Stream.concat(Stream.of(child), getAllChildren(child)));
    }

    public long countRelations() {
        return model.getRelations().size();
    }

    public List<C4DiagramRelation> findRelations(String fromAlias, String toAlias) {
        return model.getRelations().stream() //
                .filter(r -> r.getFromAlias().equals(fromAlias)) //
                .filter(r -> r.getToAlias().equals(toAlias)) //
                .toList();
    }

    public List<C4DiagramRelation> findAllRelations() {
        return model.getRelations();
    }
}
