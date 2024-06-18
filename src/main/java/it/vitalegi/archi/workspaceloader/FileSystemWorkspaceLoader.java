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

        if (w1.getElements() != null) {
            out.getElements().addAll(w1.getElements());
        }
        if (w2.getElements() != null) {
            out.getElements().addAll(w2.getElements());
        }

        if (w1.getDiagrams() != null) {
            out.getDiagrams().addAll(w1.getDiagrams());
        }
        if (w2.getDiagrams() != null) {
            out.getDiagrams().addAll(w2.getDiagrams());
        }

        if (w1.getRelations() != null) {
            out.getRelations().addAll(w1.getRelations());
        }
        if (w2.getRelations() != null) {
            out.getRelations().addAll(w2.getRelations());
        }

        out.setStyle(Style.merge(w1.getStyle(), w2.getStyle()));

        return out;
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
