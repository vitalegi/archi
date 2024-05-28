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
        assertNotNull(a);

        var b = workspace.getPerson("B");
        assertNotNull(b);

        assertEquals(1, workspace.getSoftwareSystems().size());
        var c = workspace.getSoftwareSystem("C");
        assertEquals(1, c.getContainers().size());
        assertEquals("c1", c.getContainers().get(0).getId());

        assertEquals(1, workspace.getSoftwareSystem("C").getContainers().size());
        var c1 = workspace.getSoftwareSystem("C").findContainerById("c1");
        assertNotNull(c1);

        assertNotNull(workspace.getGroup("group1"));

        assertNotNull(workspace.getSoftwareSystem("C").findGroupById("group2"));

        assertNotNull(workspace.getSoftwareSystem("C").findGroupById("group2").findGroupById("group3"));

    }
}
