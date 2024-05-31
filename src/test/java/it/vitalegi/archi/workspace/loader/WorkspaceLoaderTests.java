package it.vitalegi.archi.workspace.loader;

import it.vitalegi.archi.exception.CycleNotAllowedException;
import it.vitalegi.archi.exception.ElementNotAllowedException;
import it.vitalegi.archi.util.WorkspaceLoaderBuilder;
import it.vitalegi.archi.util.WorkspaceUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Path;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class WorkspaceLoaderTests {
    @Test
    void when_load_given_validWorkspace_thenLoads() throws IOException {
        FileSystemWorkspaceLoader loader = new FileSystemWorkspaceLoader();
        var workspace = new WorkspaceLoader().load(loader.load(Path.of("src", "test", "resources", "workspace1.yaml")));

        assertNotNull(workspace);
        assertNotNull(workspace.getModel().getElements());
        assertEquals(2, workspace.getModel().getPeople().size());
        var a = workspace.getModel().findPersonById("A");
        assertNotNull(a);

        var b = workspace.getModel().findPersonById("B");
        assertNotNull(b);

        assertEquals(1, workspace.getModel().getSoftwareSystems().size());
        var c = workspace.getModel().findSoftwareSystemById("C");
        assertEquals(1, c.getContainers().size());
        assertEquals("c1", c.getContainers().get(0).getId());

        assertEquals(1, workspace.getModel().findSoftwareSystemById("C").getContainers().size());
        var c1 = workspace.getModel().findSoftwareSystemById("C").findContainerById("c1");
        assertNotNull(c1);

        assertNotNull(workspace.getModel().findGroupById("group1"));

        assertNotNull(workspace.getModel().findSoftwareSystemById("C").findGroupById("group2"));

        assertNotNull(workspace.getModel().findSoftwareSystemById("C").findGroupById("group2").findGroupById("group3"));
    }

    @Test
    void when_load_given_personOnRoot_thenSucceed() {
        var loader = new WorkspaceLoader();
        var ws = loader.load(builder().person("A").build());
        assertEquals("A", ws.getModel().findPersonById("A").getId());
    }

    @Test
    void when_load_given_softwareSystemOnRoot_thenSucceed() {
        var loader = new WorkspaceLoader();
        var ws = loader.load(builder().softwareSystem("A").build());
        assertEquals("A", ws.getModel().findSoftwareSystemById("A").getId());
    }

    @Test
    void when_load_given_groupOnRoot_thenSucceed() {
        var loader = new WorkspaceLoader();
        var ws = loader.load(builder().group("A").build());
        assertEquals("A", ws.getModel().findGroupById("A").getId());
    }

    @Test
    void when_load_given_containerOnRoot_thenFail() {
        var loader = new WorkspaceLoader();
        var config = builder().container("A").build();
        var e = Assertions.assertThrows(ElementNotAllowedException.class, () -> loader.load(config));
        assertEquals("Can't add Container (A) to Model", e.getMessage());
    }

    @Test
    void when_load_given_childOnPerson_thenFail() {
        var loader = new WorkspaceLoader();
        var config = builder().person("A").person("A", "B").build();
        var e = Assertions.assertThrows(ElementNotAllowedException.class, () -> loader.load(config));
        assertEquals("Can't add Person (B) to Person (A)", e.getMessage());
    }

    @Test
    void when_load_given_softwareSystemChildOfGroup_thenSucceed() {
        var loader = new WorkspaceLoader();
        var ws = loader.load(builder().group("A").softwareSystem("A", "B").build());
        assertNotNull(ws.getModel().findGroupById("A"));
        assertFalse(ws.getModel().findGroupById("A").getSoftwareSystems().isEmpty());
        assertEquals("B", ws.getModel().findGroupById("A").getSoftwareSystems().get(0).getId());
    }

    @Test
    void when_load_given_groupChildOfGroup_thenSucceed() {
        var loader = new WorkspaceLoader();
        var ws = loader.load(builder().group("A").group("A", "B").build());
        assertNotNull(ws.getModel().findGroupById("A"));
        assertFalse(ws.getModel().findGroupById("A").getGroups().isEmpty());
        assertEquals("B", ws.getModel().findGroupById("A").getGroups().get(0).getId());
    }

    @Test
    void when_load_given_softwareSystemChildOfGroupOfSoftwareSystem_thenFail() {
        var loader = new WorkspaceLoader();
        var config = builder().group("A", "B").softwareSystem("A").softwareSystem("B", "C").build();
        var e = Assertions.assertThrows(ElementNotAllowedException.class, () -> loader.load(config));
        assertEquals("Can't add SoftwareSystem (C) to Group (B)", e.getMessage());
    }

    @Test
    void when_load_given_containerChildOfGroupChildOfSoftwareSystem_thenSucceed() {
        var loader = new WorkspaceLoader();
        var ws = loader.load(builder().group("A", "B").container("B", "C").softwareSystem("A").build());
        var ss = ws.getModel().findSoftwareSystemById("A");
        assertNotNull(ss);
        var g = WorkspaceUtil.findGroup(ss.getGroups(), "B");
        assertNotNull(g);
        assertEquals(1, g.getElements().size());
        var c = WorkspaceUtil.findContainer(g.getContainers(), "C");
        assertNotNull(c);
        assertEquals("C", c.getId());
    }

    @Test
    void when_load_given_chainOfGroups_thenSucceed() {
        var loader = new WorkspaceLoader();
        var config = builder().group("A").group("A", "B").group("B", "C").build();
        var ws = loader.load(config);
        var g1 = ws.getModel().findGroupById("A");
        assertNotNull(g1);
        var g2 = WorkspaceUtil.getGroup(g1.getGroups(), "B");
        assertNotNull(g2);
        var g3 = WorkspaceUtil.getGroup(g2.getGroups(), "C");
        assertNotNull(g3);
    }

    @Test
    void when_load_given_deploymentEnvironment_thenSucceed() {
        var loader = new WorkspaceLoader();
        var config = builder().deploymentEnvironment("A").build();
        var ws = loader.load(config);
        var a = ws.getModel().findDeploymentEnvironmentById("A");
        assertNotNull(a);
    }


    @Test
    void when_load_given_deploymentNodeOnDeploymentEnvironment_thenSucceed() {
        var loader = new WorkspaceLoader();
        var config = builder().deploymentEnvironment("A").deploymentNode("A", "B").build();
        var ws = loader.load(config);
        var a = ws.getModel().findDeploymentEnvironmentById("A");
        assertNotNull(a);
        var b = a.findDeploymentNodeById("B");
        assertNotNull(b);
    }

    @Test
    void when_load_given_deploymentNodeOnRoot_thenFail() {
        var loader = new WorkspaceLoader();
        var config = builder().deploymentNode("A").build();
        var e = Assertions.assertThrows(ElementNotAllowedException.class, () -> loader.load(config));
        assertEquals("Can't add DeploymentNode (A) to Model", e.getMessage());
    }

    @Test
    void when_load_given_containerInstanceOnRoot_thenFail() {
        var loader = new WorkspaceLoader();
        var config = builder().containerInstance("A", "c").build();
        var e = Assertions.assertThrows(ElementNotAllowedException.class, () -> loader.load(config));
        assertEquals("Can't add ContainerInstance (A) to Model", e.getMessage());
    }

    @Test
    void when_load_given_containerInstanceOnDeploymentEnvironment_thenFail() {
        var loader = new WorkspaceLoader();
        var config = builder().deploymentEnvironment("A").containerInstance("A", "B", "c").build();
        var e = Assertions.assertThrows(ElementNotAllowedException.class, () -> loader.load(config));
        assertEquals("Can't add ContainerInstance (B) to DeploymentEnvironment (A)", e.getMessage());
    }

    @Test
    void when_load_given_containerOnContainerInstance_thenSucceed() {
        var loader = new WorkspaceLoader();
        var config = builder().deploymentEnvironment("A").deploymentNode("A", "B").containerInstance("B", "C", "c").softwareSystem("SS").container("SS", "c").build();
        var ws = loader.load(config);
        var a = ws.getModel().findDeploymentEnvironmentById("A");
        assertNotNull(a);
        var b = a.findDeploymentNodeById("B");
        assertNotNull(b);
        var c = b.findContainerInstanceById("C");
        assertNotNull(c);
        assertEquals("c", c.getContainerId());
    }

    @Test
    void when_load_given_containerInstanceWithNoContainer_thenFail() {
        var loader = new WorkspaceLoader();
        var config = builder().deploymentEnvironment("A").deploymentNode("A", "B").containerInstance("B", "C", "").build();
        var e = Assertions.assertThrows(IllegalArgumentException.class, () -> loader.load(config));
        assertEquals("containerId is missing on ContainerInstance (C)", e.getMessage());
    }

    @Test
    void when_load_given_containerInstanceWithMissingContainer_thenFail() {
        var loader = new WorkspaceLoader();
        var config = builder().deploymentEnvironment("A").deploymentNode("A", "B").containerInstance("B", "C", "c").build();
        var e = Assertions.assertThrows(NoSuchElementException.class, () -> loader.load(config));
        assertEquals("Container c doesn't exist. Dependency is unsatisfied for ContainerInstance (C)", e.getMessage());
    }
    @Test
    void when_load_given_containerInstanceWithSomethingNotAContainer_thenFail() {
        var loader = new WorkspaceLoader();
        var config = builder().deploymentEnvironment("A").deploymentNode("A", "B").softwareSystem("ss").containerInstance("B", "C", "ss").build();
        var e = Assertions.assertThrows(IllegalArgumentException.class, () -> loader.load(config));
        assertEquals("Dependency is unsatisfied for ContainerInstance (C). Expected a Container; Actual: SoftwareSystem (ss)", e.getMessage());
    }
    @Test
    void when_load_given_cycle_thenFail() {
        var loader = new WorkspaceLoader();
        var config = builder().group("A", "B").group("B", "A").group("C").group("D").group(null).group("A", "E").group("A", null).build();
        var e = Assertions.assertThrows(CycleNotAllowedException.class, () -> loader.load(config));
        assertEquals("Unresolved dependencies. Known: C, D, null. Unresolved: A: B; B: A; E: A; null: A", e.getMessage());
    }

    protected WorkspaceLoaderBuilder builder() {
        return new WorkspaceLoaderBuilder();
    }
}
