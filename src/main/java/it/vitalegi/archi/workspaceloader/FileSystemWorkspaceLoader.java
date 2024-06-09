package it.vitalegi.archi.workspaceloader;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import it.vitalegi.archi.workspaceloader.model.WorkspaceRaw;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;

@Slf4j
public class FileSystemWorkspaceLoader {
    public WorkspaceRaw load(Path path) {
        var mapper = YAMLMapper.builder() //
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS) //
                .findAndAddModules() //
                .build();
        try {
            return mapper.readValue(path.toFile(), WorkspaceRaw.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
