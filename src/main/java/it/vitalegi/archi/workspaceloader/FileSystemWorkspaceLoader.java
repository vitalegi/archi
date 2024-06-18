package it.vitalegi.archi.workspaceloader;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import it.vitalegi.archi.model.style.Style;
import it.vitalegi.archi.workspaceloader.model.WorkspaceRaw;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class FileSystemWorkspaceLoader {
    public WorkspaceRaw load(Path path) {
        return getFiles(path) //
                .map(this::readFile) //
                .reduce(new WorkspaceRaw(), this::merge);
    }

    protected WorkspaceRaw merge(WorkspaceRaw w1, WorkspaceRaw w2) {
        var out = new WorkspaceRaw();

        addAll(out.getElements(), w1.getElements());
        addAll(out.getElements(), w2.getElements());

        addAll(out.getDiagrams(), w1.getDiagrams());
        addAll(out.getDiagrams(), w2.getDiagrams());

        addAll(out.getRelations(), w1.getRelations());
        addAll(out.getRelations(), w2.getRelations());

        addAll(out.getFlows(), w1.getFlows());
        addAll(out.getFlows(), w2.getFlows());

        out.setStyle(Style.merge(w1.getStyle(), w2.getStyle()));

        return out;
    }

    protected <E> void addAll(List<E> target, List<E> source) {
        if (source != null) {
            target.addAll(source);
        }
    }

    protected WorkspaceRaw readFile(Path path) {
        var mapper = yamlMapper();
        try {
            return mapper.readValue(path.toFile(), WorkspaceRaw.class);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file " + path, e);
        }
    }

    protected Stream<Path> getFiles(Path path) {
        try (var files = Files.find(path, 999, (p, bfa) -> bfa.isRegularFile())) {
            var out = new ArrayList<Path>(files.filter(this::isYaml).toList());
            return out.stream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean isYaml(Path path) {
        return path.getFileName().toString().toLowerCase().endsWith(".yaml");
    }

    protected YAMLMapper yamlMapper() {
        return YAMLMapper.builder() //
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS) //
                .findAndAddModules() //
                .build();
    }
}
