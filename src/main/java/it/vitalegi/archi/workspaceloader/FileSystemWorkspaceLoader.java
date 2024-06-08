package it.vitalegi.archi.workspaceloader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import it.vitalegi.archi.workspaceloader.model.WorkspaceRaw;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;

@Slf4j
public class FileSystemWorkspaceLoader {
    public WorkspaceRaw load(Path path) {
        var mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        try {
            return mapper.readValue(path.toFile(), WorkspaceRaw.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
