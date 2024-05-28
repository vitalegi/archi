package it.vitalegi.archi.workspace.loader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class FileSystemWorkspaceLoaderTests {
    @Test
    void when_load_given_validWorkspace_thenLoads() throws IOException {
        FileSystemWorkspaceLoader loader = new FileSystemWorkspaceLoader();
        var workspace = loader.load(Path.of("src", "test", "resources", "workspace1.yaml"));
        assertNotNull(workspace);
        assertNotNull(workspace.getElements());
        assertEquals(2, workspace.getPeople().size());
        var a = workspace.getPerson("A");
        var b = workspace.getPerson("B");
        assertEquals(1, workspace.getSoftwareSystems().size());
        var c = workspace.getSoftwareSystem("C");
        assertEquals(1, c.getContainers().size());
        assertEquals("c1", c.getContainers().get(0).getId());
        assertEquals("C", c.getContainers().get(0).getParent().getId());

        assertEquals(1, workspace.getContainers().size());
        var c1 = workspace.getContainer("c1");
    }
}
