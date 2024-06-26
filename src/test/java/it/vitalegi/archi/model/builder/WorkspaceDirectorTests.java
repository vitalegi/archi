package it.vitalegi.archi.model.builder;

import it.vitalegi.archi.exception.CycleNotAllowedException;
import it.vitalegi.archi.exception.ElementNotAllowedException;
import it.vitalegi.archi.exception.NonUniqueIdException;
import it.vitalegi.archi.exception.RelationNotAllowedException;
import it.vitalegi.archi.util.ModelTestUtil;
import it.vitalegi.archi.util.StyleTestUtil;
import it.vitalegi.archi.util.WorkspaceModelBuilder;
import it.vitalegi.archi.util.WorkspaceUtil;
import it.vitalegi.archi.workspaceloader.model.DeploymentDiagramRaw;
import it.vitalegi.archi.workspaceloader.model.ElementRaw;
import it.vitalegi.archi.workspaceloader.model.FlowRaw;
import it.vitalegi.archi.workspaceloader.model.FlowStepRaw;
import it.vitalegi.archi.workspaceloader.model.LandscapeDiagramRaw;
import it.vitalegi.archi.workspaceloader.model.RelationRaw;
import it.vitalegi.archi.workspaceloader.model.SystemContextDiagramRaw;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static it.vitalegi.archi.util.WorkspaceTestUtil.load;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class WorkspaceDirectorTests {

    static WorkspaceDirector loader() {
        return ModelTestUtil.defaultLoader();
    }

    @Test
    void when_load_given_cycle_thenFail() {

        var config = builder() //
                .group("A", "B") //
                .group("B", "A") //
                .group("C") //
                .group("D") //
                .group(null) //
                .group("A", "E") //
                .group("A", null) //
                .build();

        var e = Assertions.assertThrows(CycleNotAllowedException.class, () -> load(config));
        assertEquals("Unresolved dependencies. Known: C, D, null. Unresolved: A: B; B: A; E: A; null: A", e.getMessage());
    }

    @Test
    void when_load_given_duplicateIds_thenFail() {

        var config = builder() //
                .softwareSystem("a") //
                .softwareSystem("b") //
                .container("b", "a") //
                .build();

        var e = Assertions.assertThrows(NonUniqueIdException.class, () -> load(config));
        assertEquals("ID a is defined more than once", e.getMessage());
    }

    @Test
    void when_load_given_nullIds_thenSucceed() {

        var config = builder().softwareSystem(null).softwareSystem(null).build();
        var ws = load(config);
        assertEquals(2, ws.getModel().getSoftwareSystems().size());
    }

    @Test
    void when_load_given_globalStyle_thenGlobalStyleIsAvailable() {

        var config = builder() //
                .group("A", "B") //
                .group("B", "A") //
                .group("C") //
                .group("D") //
                .group(null) //
                .group("A", "E") //
                .group("A", null) //
                .build();

        var e = Assertions.assertThrows(CycleNotAllowedException.class, () -> load(config));
        assertEquals("Unresolved dependencies. Known: C, D, null. Unresolved: A: B; B: A; E: A; null: A", e.getMessage());
    }


    @Test
    void when_load_given_elementWithTechnologies_then_technologiesArePreserved() {
        var config = builder() //
                .element(ElementRaw.softwareSystem().id("A").technologies(Arrays.asList("T1", "T2"))) //
                .build();

        var ws = load(config);
        var a = ws.getModel().getElementById("A");
        assertEquals(Arrays.asList("T1", "T2"), a.getTechnologies());
    }

    @Test
    void when_load_given_relationWithTechnologies_then_technologiesArePreserved() {
        var config = builder() //
                .element(ElementRaw.softwareSystem().id("A")) //
                .element(ElementRaw.softwareSystem().id("B")) //
                .relation(RelationRaw.builder().from("A").to("B").technologies(Arrays.asList("T1", "T2"))) //
                .build();

        var ws = load(config);
        var a = ws.getModel().getElementById("A");
        var relations = ws.getModel().getRelationManager().getDirect().getAll();
        assertEquals(1, relations.size());
        assertEquals(Arrays.asList("T1", "T2"), relations.get(0).getTechnologies());
    }

    protected WorkspaceModelBuilder builder() {
        return ModelTestUtil.defaultBuilder();
    }

    @Nested
    class Person {
        @Test
        void when_load_given_personOnRoot_thenSucceed() {

            var ws = load(builder().person("A").build());
            assertEquals("A", ws.getModel().findPersonById("A").getId());
        }

        @Test
        void when_load_given_childOnPerson_thenFail() {

            var config = builder().person("A").person("A", "B").build();
            var e = Assertions.assertThrows(ElementNotAllowedException.class, () -> load(config));
            assertEquals("Can't add Person (B) to Person (A)", e.getMessage());
        }

    }

    @Nested
    class SoftwareSystem {

        @Test
        void when_load_given_softwareSystemOnRoot_thenSucceed() {

            var ws = load(builder().softwareSystem("A").build());
            assertEquals("A", ws.getModel().findSoftwareSystemById("A").getId());
        }

        @Test
        void when_load_given_softwareSystemChildOfGroup_thenSucceed() {

            var ws = load(builder() //
                    .group("A") //
                    .softwareSystem("A", "B") //
                    .build());

            assertNotNull(ws.getModel().findGroupById("A"));
            assertFalse(ws.getModel().findGroupById("A").getSoftwareSystems().isEmpty());
            assertEquals("B", ws.getModel().findGroupById("A").getSoftwareSystems().get(0).getId());
        }

        @Test
        void when_load_given_softwareSystemChildOfGroupOfSoftwareSystem_thenFail() {

            var config = builder() //
                    .group("A", "B") //
                    .softwareSystem("A") //
                    .softwareSystem("B", "C") //
                    .build();

            var e = Assertions.assertThrows(ElementNotAllowedException.class, () -> load(config));
            assertEquals("Can't add SoftwareSystem (C) to Group (B)", e.getMessage());
        }
    }

    @Nested
    class Container {
        @Test
        void when_load_given_containerOnRoot_thenFail() {

            var config = builder().container("A").build();
            var e = Assertions.assertThrows(ElementNotAllowedException.class, () -> load(config));
            assertEquals("Can't add Container (A) to Model", e.getMessage());
        }

        @Test
        void when_load_given_containerChildOfGroupChildOfSoftwareSystem_thenSucceed() {

            var ws = load(builder() //
                    .group("A", "B") //
                    .container("B", "C") //
                    .softwareSystem("A") //
                    .build());

            var ss = ws.getModel().findSoftwareSystemById("A");
            assertNotNull(ss);
            var g = WorkspaceUtil.findGroup(ss.getGroups(), "B");
            assertNotNull(g);
            assertEquals(1, g.getElements().size());
            var c = WorkspaceUtil.findContainer(g.getContainers(), "C");
            assertNotNull(c);
            assertEquals("C", c.getId());
        }
    }

    @Nested
    class Component {
        @Test
        void when_load_given_componentOnRoot_thenFail() {

            var config = builder().component(ElementRaw.builder().id("A")).build();
            var e = Assertions.assertThrows(ElementNotAllowedException.class, () -> load(config));
            assertEquals("Can't add Component (A) to Model", e.getMessage());
        }

        @Test
        void when_load_given_componentChildOfGroupChildOfContainer_thenSucceed() {
            var ws = load(builder() //
                    .softwareSystem("A") //
                    .group("A", "G1") //
                    .container("G1", "C") //
                    .group("C", "G2") //
                    .component(ElementRaw.builder().parentId("G2").id("component")) //
                    .build());

            var ss = ws.getModel().findSoftwareSystemById("A");
            assertNotNull(ss);
            var g1 = WorkspaceUtil.findGroup(ss.getGroups(), "G1");
            assertNotNull(g1);
            var c = WorkspaceUtil.findContainer(g1.getContainers(), "C");
            assertNotNull(c);
            var g2 = WorkspaceUtil.findGroup(c.getGroups(), "G2");
            assertNotNull(g2);
            var component = WorkspaceUtil.findComponent(g2.getComponents(), "component");
            assertNotNull(component);
            assertEquals("component", component.getId());
        }
    }

    @Nested
    class Group {
        @Test
        void when_load_given_groupOnRoot_thenSucceed() {

            var ws = load(builder().group("A").build());
            assertEquals("A", ws.getModel().findGroupById("A").getId());
        }

        @Test
        void when_load_given_groupChildOfGroup_thenSucceed() {

            var ws = load(builder() //
                    .group("A") //
                    .group("A", "B") //
                    .build());

            assertNotNull(ws.getModel().findGroupById("A"));
            assertFalse(ws.getModel().findGroupById("A").getGroups().isEmpty());
            assertEquals("B", ws.getModel().findGroupById("A").getGroups().get(0).getId());
        }

        @Test
        void when_load_given_chainOfGroups_thenSucceed() {

            var config = builder() //
                    .group("A") //
                    .group("A", "B") //
                    .group("B", "C") //
                    .build();

            var ws = load(config);
            var g1 = ws.getModel().findGroupById("A");
            assertNotNull(g1);
            var g2 = WorkspaceUtil.getGroup(g1.getGroups(), "B");
            assertNotNull(g2);
            var g3 = WorkspaceUtil.getGroup(g2.getGroups(), "C");
            assertNotNull(g3);
        }
    }

    @Nested
    class DeploymentEnvironment {
        @Test
        void when_load_given_deploymentEnvironment_thenSucceed() {

            var config = builder().deploymentEnvironment("A").build();
            var ws = load(config);
            var a = ws.getModel().findDeploymentEnvironmentById("A");
            assertNotNull(a);
        }

    }

    @Nested
    class DeploymentNode {
        @Test
        void when_load_given_deploymentNodeOnDeploymentEnvironment_thenSucceed() {

            var config = builder().deploymentEnvironment("A").deploymentNode("A", "B").build();
            var ws = load(config);
            var a = ws.getModel().findDeploymentEnvironmentById("A");
            assertNotNull(a);
            var b = a.findDeploymentNodeById("B");
            assertNotNull(b);
        }

        @Test
        void when_load_given_deploymentNodeOnRoot_thenFail() {

            var config = builder().deploymentNode("A").build();
            var e = Assertions.assertThrows(ElementNotAllowedException.class, () -> load(config));
            assertEquals("Can't add DeploymentNode (A) to Model", e.getMessage());
        }
    }

    @Nested
    class InfrastructureNode {
        @Test
        void when_load_given_infrastructureNode_thenSucceed() {

            var config = builder() //
                    .deploymentEnvironment("A") //
                    .deploymentNode("A", "B") //
                    .containerInstance("B", "C", "c") //
                    .softwareSystem("SS") //
                    .container("SS", "c") //
                    .infrastructureNode("A", "infra1") //
                    .infrastructureNode("B", "infra2") //
                    .build();

            var ws = load(config);
            var a = ws.getModel().findDeploymentEnvironmentById("A");
            assertNotNull(a);
            var b = a.findDeploymentNodeById("B");
            assertNotNull(b);
            var infra1 = a.findInfrastructureNodeById("infra1");
            assertNotNull(infra1);
            var infra2 = b.findInfrastructureNodeById("infra2");
            assertNotNull(infra2);
        }

        @Test
        void when_load_given_infrastructureNodeOnWrongParent_thenFail() {

            var e = Assertions.assertThrows(ElementNotAllowedException.class, () -> load( //
                    builder() //
                            .softwareSystem("A") //
                            .infrastructureNode("A", "B") //
                            .build() //
            ));
            assertEquals("Can't add InfrastructureNode (B) to SoftwareSystem (A)", e.getMessage());

            e = Assertions.assertThrows(ElementNotAllowedException.class, () -> load( //
                    builder().softwareSystem("A").container("A", "B").infrastructureNode("B", "C").build() //
            ));
            assertEquals("Can't add InfrastructureNode (C) to Container (B)", e.getMessage());

            e = Assertions.assertThrows(ElementNotAllowedException.class, () -> load( //
                    builder().infrastructureNode("A").build() //
            ));
            assertEquals("Can't add InfrastructureNode (A) to Model", e.getMessage());
        }

    }

    @Nested
    class SoftwareSystemInstance {


        @Test
        void when_load_given_softwareSystemInstanceOnRoot_thenFail() {

            var config = builder().softwareSystemInstance("A", "c").build();
            var e = Assertions.assertThrows(ElementNotAllowedException.class, () -> load(config));
            assertEquals("Can't add SoftwareSystemInstance (A) to Model", e.getMessage());
        }

        @Test
        void when_load_given_softwareSystemInstanceOnDeploymentEnvironment_thenFail() {

            var config = builder() //
                    .deploymentEnvironment("A") //
                    .softwareSystemInstance("A", "B", "c") //
                    .build();

            var e = Assertions.assertThrows(ElementNotAllowedException.class, () -> load(config));
            assertEquals("Can't add SoftwareSystemInstance (B) to DeploymentEnvironment (A)", e.getMessage());
        }

        @Test
        void when_load_given_softwareSystemOnSoftwareSystemInstance_thenSucceed() {

            var config = builder() //
                    .deploymentEnvironment("A") //
                    .deploymentNode("A", "B") //
                    .softwareSystemInstance("B", "C", "SS") //
                    .softwareSystem("SS") //
                    .container("SS", "c") //
                    .build();

            var ws = load(config);
            var a = ws.getModel().findDeploymentEnvironmentById("A");
            assertNotNull(a);
            var b = a.findDeploymentNodeById("B");
            assertNotNull(b);
            var c = b.findSoftwareSystemInstanceById("C");
            assertNotNull(c);
            assertEquals("SS", c.getSoftwareSystemId());
        }

        @Test
        void when_load_given_softwareSystemInstanceWithNoSoftwareSystem_thenFail() {

            var config = builder() //
                    .deploymentEnvironment("A") //
                    .deploymentNode("A", "B") //
                    .softwareSystemInstance("B", "C", null) //
                    .build();

            var e = Assertions.assertThrows(IllegalArgumentException.class, () -> load(config));
            assertEquals("softwareSystemId is missing on SoftwareSystemInstance (C)", e.getMessage());
        }

        @Test
        void when_load_given_softwareSystemInstanceWithMissingSoftwareSystem_thenFail() {

            var config = builder() //
                    .deploymentEnvironment("A") //
                    .deploymentNode("A", "B") //
                    .softwareSystemInstance("B", "C", "c") //
                    .build();

            var e = Assertions.assertThrows(NoSuchElementException.class, () -> load(config));
            assertEquals("SoftwareSystem c doesn't exist. Dependency is unsatisfied for SoftwareSystemInstance (C)", e.getMessage());
        }

        @Test
        void when_load_given_softwareSystemInstanceWithSomethingNotASoftwareSystem_thenFail() {

            var config = builder() //
                    .deploymentEnvironment("A") //
                    .deploymentNode("A", "B") //
                    .softwareSystem("ss") //
                    .group("ss", "g").softwareSystemInstance("B", "C", "g") //
                    .build();

            var e = Assertions.assertThrows(IllegalArgumentException.class, () -> load(config));
            assertEquals("Dependency is unsatisfied for SoftwareSystemInstance (C). Expected a SoftwareSystem; Actual: Group (g)", e.getMessage());
        }

    }

    @Nested
    class ContainerInstance {
        @Test
        void when_load_given_containerInstanceOnRoot_thenFail() {

            var config = builder().containerInstance("A", "c").build();
            var e = Assertions.assertThrows(ElementNotAllowedException.class, () -> load(config));
            assertEquals("Can't add ContainerInstance (A) to Model", e.getMessage());
        }

        @Test
        void when_load_given_containerInstanceOnDeploymentEnvironment_thenFail() {

            var config = builder() //
                    .deploymentEnvironment("A") //
                    .containerInstance("A", "B", "c") //
                    .build();

            var e = Assertions.assertThrows(ElementNotAllowedException.class, () -> load(config));
            assertEquals("Can't add ContainerInstance (B) to DeploymentEnvironment (A)", e.getMessage());
        }

        @Test
        void when_load_given_containerOnContainerInstance_thenSucceed() {

            var config = builder() //
                    .deploymentEnvironment("A") //
                    .deploymentNode("A", "B") //
                    .containerInstance("B", "C", "c") //
                    .softwareSystem("SS") //
                    .container("SS", "c") //
                    .build();

            var ws = load(config);
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

            var config = builder() //
                    .deploymentEnvironment("A") //
                    .deploymentNode("A", "B") //
                    .containerInstance("B", "C", "") //
                    .build();

            var e = Assertions.assertThrows(IllegalArgumentException.class, () -> load(config));
            assertEquals("containerId is missing on ContainerInstance (C)", e.getMessage());
        }

        @Test
        void when_load_given_containerInstanceWithMissingContainer_thenFail() {

            var config = builder() //
                    .deploymentEnvironment("A") //
                    .deploymentNode("A", "B") //
                    .containerInstance("B", "C", "c") //
                    .build();

            var e = Assertions.assertThrows(NoSuchElementException.class, () -> load(config));
            assertEquals("Container c doesn't exist. Dependency is unsatisfied for ContainerInstance (C)", e.getMessage());
        }

        @Test
        void when_load_given_containerInstanceWithSomethingNotAContainer_thenFail() {

            var config = builder() //
                    .deploymentEnvironment("A") //
                    .deploymentNode("A", "B") //
                    .softwareSystem("ss") //
                    .containerInstance("B", "C", "ss") //
                    .build();

            var e = Assertions.assertThrows(IllegalArgumentException.class, () -> load(config));
            assertEquals("Dependency is unsatisfied for ContainerInstance (C). Expected a Container; Actual: SoftwareSystem (ss)", e.getMessage());
        }
    }

    @Nested
    class DirectRelation {

        @Nested
        class DirectRelationTests {
            WorkspaceModelBuilder builder;

            static Stream<Arguments> allowedRelations() {
                return Stream.of(arg("person1", "person1", true, "Relation from Person to self is allowed"), //
                        arg("person1", "person2", true, "Relation from Person to Person is allowed"), //
                        arg("person1", "softwareSystem1", true, "Relation from Person to SoftwareSystem is allowed"), //
                        arg("person1", "container11", true, "Relation from Person to Container is allowed"), //
                        arg("person1", "component111", true, "Relation from Person to Component is allowed"), //
                        arg("person1", "group1", false, "Relation from Person to Group is NOT allowed"), //
                        arg("person1", "env1", false, "Relation from Person to DeploymentEnvironment is NOT allowed"), //
                        arg("person1", "node11", false, "Relation from Person to DeploymentNode is NOT allowed"), //
                        arg("person1", "infra11", false, "Relation from Person to InfrastructureNode is NOT allowed"), //
                        arg("person1", "softwareSystemInstance1", false, "Relation from Person to SoftwareSystemInstance is NOT allowed"), //
                        arg("person1", "containerInstance11", false, "Relation from Person to ContainerInstance is NOT allowed"), //
                        //

                        arg("softwareSystem1", "softwareSystem1", true, "Relation from SoftwareSystem to self is allowed"), //
                        arg("softwareSystem1", "person2", true, "Relation from SoftwareSystem to Person is allowed"), //
                        arg("softwareSystem1", "softwareSystem2", true, "Relation from SoftwareSystem to SoftwareSystem is allowed"), //
                        arg("softwareSystem1", "container11", true, "Relation from SoftwareSystem to Container is allowed"), //
                        arg("softwareSystem1", "component111", true, "Relation from SoftwareSystem to Component is allowed"), //
                        arg("softwareSystem1", "group1", false, "Relation from SoftwareSystem to Group is NOT allowed"), //
                        arg("softwareSystem1", "env1", false, "Relation from SoftwareSystem to DeploymentEnvironment is NOT allowed"), //
                        arg("softwareSystem1", "node11", false, "Relation from SoftwareSystem to DeploymentNode is NOT allowed"), //
                        arg("softwareSystem1", "infra11", false, "Relation from SoftwareSystem to InfrastructureNode is NOT allowed"), //
                        arg("softwareSystem1", "softwareSystemInstance1", false, "Relation from SoftwareSystem to SoftwareSystemInstance is NOT allowed"), //
                        arg("softwareSystem1", "containerInstance11", false, "Relation from SoftwareSystem to ContainerInstance is NOT allowed"), //
                        //

                        arg("container21", "container21", true, "Relation from Container to self is allowed"), //
                        arg("container21", "person2", true, "Relation from Container to Person is allowed"), //
                        arg("container21", "softwareSystem1", true, "Relation from Container to SoftwareSystem is allowed"), //
                        arg("container21", "container11", true, "Relation from Container to Container is allowed"), //
                        arg("container21", "component111", true, "Relation from Container to Component is allowed"), //
                        arg("container21", "group1", false, "Relation from Container to Group is NOT allowed"), //
                        arg("container21", "env1", false, "Relation from Container to DeploymentEnvironment is NOT allowed"), //
                        arg("container21", "node11", false, "Relation from Container to DeploymentNode is NOT allowed"), //
                        arg("container21", "infra11", false, "Relation from Container to InfrastructureNode is NOT allowed"), //
                        arg("container21", "softwareSystemInstance1", false, "Relation from Container to SoftwareSystemInstance is NOT allowed"), //
                        arg("container21", "containerInstance11", false, "Relation from Container to ContainerInstance is NOT allowed"), //
                        //

                        arg("component111", "component111", true, "Relation from Component to self is allowed"), //
                        arg("component111", "person2", true, "Relation from Component to Person is allowed"), //
                        arg("component111", "softwareSystem1", true, "Relation from Component to SoftwareSystem is allowed"), //
                        arg("component111", "container11", true, "Relation from Component to Container is allowed"), //
                        arg("component111", "component112", true, "Relation from Component to Component is allowed"), //
                        arg("component111", "group1", false, "Relation from Component to Group is NOT allowed"), //
                        arg("component111", "env1", false, "Relation from Component to DeploymentEnvironment is NOT allowed"), //
                        arg("component111", "node11", false, "Relation from Component to DeploymentNode is NOT allowed"), //
                        arg("component111", "infra11", false, "Relation from Component to InfrastructureNode is NOT allowed"), //
                        arg("component111", "softwareSystemInstance1", false, "Relation from Component to SoftwareSystemInstance is NOT allowed"), //
                        arg("component111", "containerInstance11", false, "Relation from Component to ContainerInstance is NOT allowed"), //
                        //

                        arg("env1", "env1", false, "Relation from DeploymentEnvironment to self is NOT allowed"), //
                        arg("env1", "person2", false, "Relation from DeploymentEnvironment to Person is NOT allowed"), //
                        arg("env1", "softwareSystem1", false, "Relation from DeploymentEnvironment to SoftwareSystem is NOT allowed"), //
                        arg("env1", "container11", false, "Relation from DeploymentEnvironment to Container is NOT allowed"), //
                        arg("env1", "component111", false, "Relation from DeploymentEnvironment to Component is NOT allowed"), //
                        arg("env1", "group1", false, "Relation from DeploymentEnvironment to Group is NOT allowed"), //
                        arg("env1", "env2", false, "Relation from DeploymentEnvironment to DeploymentEnvironment is NOT allowed"), //
                        arg("env1", "node11", false, "Relation from DeploymentEnvironment to DeploymentNode is NOT allowed"), //
                        arg("env1", "infra11", false, "Relation from DeploymentEnvironment to InfrastructureNode is NOT allowed"), //
                        arg("env1", "softwareSystemInstance1", false, "Relation from DeploymentEnvironment to SoftwareSystemInstance is NOT allowed"), //
                        arg("env1", "containerInstance11", false, "Relation from DeploymentEnvironment to ContainerInstance is NOT allowed"), //
                        //

                        arg("node12", "node12", true, "Relation from DeploymentNode to self is allowed"), //
                        arg("node12", "person2", false, "Relation from DeploymentNode to Person is NOT allowed"), //
                        arg("node12", "softwareSystem1", false, "Relation from DeploymentNode to SoftwareSystem is NOT allowed"), //
                        arg("node12", "container11", false, "Relation from DeploymentNode to Container is NOT allowed"), //
                        arg("node12", "component111", false, "Relation from DeploymentNode to Component is NOT allowed"), //
                        arg("node12", "group1", false, "Relation from DeploymentNode to Group is NOT allowed"), //
                        arg("node12", "env1", false, "Relation from DeploymentNode to DeploymentEnvironment is NOT allowed"), //
                        arg("node12", "node11", true, "Relation from DeploymentNode to DeploymentNode is allowed"), //
                        arg("node12", "node21", false, "Relation from DeploymentNode to DeploymentNode on different environment is NOT allowed"), //
                        arg("node12", "infra11", false, "Relation from DeploymentNode to InfrastructureNode is NOT allowed"), //
                        arg("node12", "softwareSystemInstance1", false, "Relation from DeploymentNode to SoftwareSystemInstance is NOT allowed"), //
                        arg("node12", "containerInstance11", false, "Relation from DeploymentNode to ContainerInstance is NOT allowed"), //
                        //

                        arg("infra12", "infra12", true, "Relation from InfrastructureNode to self is allowed"), //
                        arg("infra12", "person2", false, "Relation from InfrastructureNode to Person is NOT allowed"), //
                        arg("infra12", "softwareSystem1", false, "Relation from InfrastructureNode to SoftwareSystem is NOT allowed"), //
                        arg("infra12", "container11", false, "Relation from InfrastructureNode to Container is NOT allowed"), //
                        arg("infra12", "component111", false, "Relation from InfrastructureNode to Component is allowed"), //
                        arg("infra12", "group1", false, "Relation from InfrastructureNode to Group is NOT allowed"), //
                        arg("infra12", "env1", false, "Relation from InfrastructureNode to DeploymentEnvironment is NOT allowed"), //
                        arg("infra12", "node11", true, "Relation from InfrastructureNode to DeploymentNode is allowed"), //
                        arg("infra12", "infra11", true, "Relation from InfrastructureNode to InfrastructureNode is allowed"), //
                        arg("infra12", "infra21", false, "Relation from InfrastructureNode to DeploymentNode on different environment is NOT allowed"), //
                        arg("infra12", "softwareSystemInstance1", true, "Relation from InfrastructureNode to SoftwareSystemInstance is allowed"), //
                        arg("infra12", "containerInstance11", true, "Relation from InfrastructureNode to ContainerInstance is allowed"), //
                        //

                        arg("softwareSystemInstance2", "softwareSystemInstance2", false, "Relation from SoftwareSystemInstance to self is NOT allowed"), //
                        arg("softwareSystemInstance2", "person2", false, "Relation from SoftwareSystemInstance to Person is NOT allowed"), //
                        arg("softwareSystemInstance2", "softwareSystem1", false, "Relation from SoftwareSystemInstance to SoftwareSystem is NOT allowed"), //
                        arg("softwareSystemInstance2", "container11", false, "Relation from SoftwareSystemInstance to Container is NOT allowed"), //
                        arg("softwareSystemInstance2", "component111", false, "Relation from SoftwareSystemInstance to Component is NOT allowed"), //
                        arg("softwareSystemInstance2", "group1", false, "Relation from SoftwareSystemInstance to Group is NOT allowed"), //
                        arg("softwareSystemInstance2", "env1", false, "Relation from SoftwareSystemInstance to DeploymentEnvironment is NOT allowed"), //
                        arg("softwareSystemInstance2", "node11", false, "Relation from SoftwareSystemInstance to DeploymentNode is NOT allowed"), //
                        arg("softwareSystemInstance2", "infra11", true, "Relation from SoftwareSystemInstance to InfrastructureNode is allowed"), //
                        arg("softwareSystemInstance2", "infra22", false, "Relation from SoftwareSystemInstance to InfrastructureNode on different environment is NOT allowed"), //
                        arg("softwareSystemInstance2", "softwareSystemInstance1", false, "Relation from SoftwareSystemInstance to SoftwareSystemInstance is NOT allowed"), //
                        arg("softwareSystemInstance2", "containerInstance11", false, "Relation from SoftwareSystemInstance to ContainerInstance is NOT allowed"), //
                        //
                        arg("containerInstance12", "containerInstance12", false, "Relation from ContainerInstance to self is NOT allowed"), //
                        arg("containerInstance12", "person2", false, "Relation from ContainerInstance to Person is NOT allowed"), //
                        arg("containerInstance12", "softwareSystem1", false, "Relation from ContainerInstance to SoftwareSystem is NOT allowed"), //
                        arg("containerInstance12", "container11", false, "Relation from ContainerInstance to Container is NOT allowed"), //
                        arg("containerInstance12", "component111", false, "Relation from ContainerInstance to Component is allowed"), //
                        arg("containerInstance12", "group1", false, "Relation from ContainerInstance to Group is NOT allowed"), //
                        arg("containerInstance12", "env1", false, "Relation from ContainerInstance to DeploymentEnvironment is NOT allowed"), //
                        arg("containerInstance12", "node11", false, "Relation from ContainerInstance to DeploymentNode is NOT allowed"), //
                        arg("containerInstance12", "infra11", true, "Relation from ContainerInstance to InfrastructureNode is allowed"), //
                        arg("containerInstance12", "infra22", false, "Relation from ContainerInstance to InfrastructureNode on different environment is NOT allowed"), //
                        arg("containerInstance12", "softwareSystemInstance1", false, "Relation from ContainerInstance to SoftwareSystemInstance is NOT allowed"), //
                        arg("containerInstance12", "containerInstance11", false, "Relation from ContainerInstance to ContainerInstance is NOT allowed") //
                        //
                );
            }

            static Arguments arg(String sourceId, String destinationId, boolean shouldSucceed, String displayName) {
                return Arguments.of(sourceId, destinationId, shouldSucceed, displayName);
            }

            @BeforeEach
            void init() {
                builder = builder() //
                        .person("person1") //
                        .person("person2") //
                        //
                        .softwareSystem("softwareSystem1") //
                        .softwareSystem("softwareSystem2") //
                        //
                        .group("group1") //
                        .group("group2") //
                        //
                        .container("softwareSystem1", "container11") //
                        .container("softwareSystem1", "container12") //
                        .container("softwareSystem2", "container21") //
                        .container("softwareSystem2", "container22") //
                        //
                        .component(ElementRaw.builder().parentId("container11").id("component111")) //
                        .component(ElementRaw.builder().parentId("container11").id("component112")) //
                        .component(ElementRaw.builder().parentId("container21").id("component211")) //
                        .component(ElementRaw.builder().parentId("container21").id("component212")) //
                        //
                        .deploymentEnvironment("env1") //
                        .deploymentEnvironment("env2") //
                        //
                        .deploymentNode("env1", "node11") //
                        .deploymentNode("env1", "node12") //
                        .deploymentNode("env2", "node21") //
                        .deploymentNode("env2", "node22") //
                        //
                        .infrastructureNode("env1", "infra11") //
                        .infrastructureNode("env1", "infra12") //
                        .infrastructureNode("env2", "infra21") //
                        .infrastructureNode("env2", "infra22") //
                        //
                        .softwareSystemInstance("node11", "softwareSystemInstance1", "softwareSystem1") //
                        .softwareSystemInstance("node11", "softwareSystemInstance2", "softwareSystem2") //
                        .softwareSystemInstance("node21", "softwareSystemInstance3", "softwareSystem1") //
                        //
                        .containerInstance("node11", "containerInstance11", "container11") //
                        .containerInstance("node11", "containerInstance12", "container12") //
                        .containerInstance("node11", "containerInstance21", "container21") //
                        .containerInstance("node11", "containerInstance22", "container22") //
                //
                ;
            }

            @ParameterizedTest(name = "{index} - {3}: from {0} to {1}")
            @MethodSource("allowedRelations")
            void allowedRelations(String sourceId, String destinationId, boolean shouldSucceed, String displayName) {
                builder.relation(sourceId, destinationId);
                if (shouldSucceed) {
                    var ws = load(builder.build());
                    var relations = ws.getModel().getRelationManager().getDirect().getAll();
                    assertEquals(1, relations.size());
                    assertEquals(sourceId, relations.get(0).getFrom().getId());
                    assertEquals(destinationId, relations.get(0).getTo().getId());
                } else {
                    Assertions.assertThrows(RelationNotAllowedException.class, () -> load(builder.build()));
                }
            }
        }
    }

    @DisplayName("When load diagram")
    @Nested
    class DiagramTests {
        @Test
        void given_noDiagram_thenEmptyList() {
            var config = builder().build();
            var ws = load(config);
            assertEquals(0, ws.getDiagrams().getAll().size());
        }

        @Test
        void given_diagramStyleIsDefined_when_landscapeDiagram_thenStyleIsLoaded() {
            var style = StyleTestUtil.randomStyle();
            var config = builder().landscapeDiagram(LandscapeDiagramRaw.builder().name("diagram").style(style)).build();
            var ws = load(config);
            var diagram = ws.getDiagrams().getByName("diagram");
            assertEquals(style, diagram.getStyle());
        }

        @Test
        void given_diagramStyleIsDefined_when_systemContextDiagram_thenStyleIsLoaded() {
            var style = StyleTestUtil.randomStyle();
            var config = builder() //
                    .softwareSystem("A") //
                    .systemContextDiagram(SystemContextDiagramRaw.builder().name("diagram").target("A").style(style)) //
                    .build();
            var ws = load(config);
            var diagram = ws.getDiagrams().getByName("diagram");
            assertEquals(style, diagram.getStyle());
        }

        @Test
        void given_diagramStyleIsDefined_when_deploymentDiagram_thenStyleIsLoaded() {
            var style = StyleTestUtil.randomStyle();
            var config = builder() //
                    .deploymentEnvironment("env") //
                    .deploymentDiagram(DeploymentDiagramRaw.builder().environment("env").name("diagram").style(style)) //
                    .build();
            var ws = load(config);
            var diagram = ws.getDiagrams().getByName("diagram");
            assertEquals(style, diagram.getStyle());
        }
    }

    @Nested
    class Flow {

        @Test
        void given_noFlow_then_contextIsLoaded() {
            var config = builder().build();
            load(config);
        }

        @Test
        void given_flow_then_flowIsLoaded() {
            var config = builder() //
                    .softwareSystem("A") //
                    .softwareSystem("B") //
                    .flow(FlowRaw.builder().id("flow1").name("flow 1") //
                            .steps(Arrays.asList( //
                                    FlowStepRaw.builder().from("A").to("B").description("link 1").build(), //
                                    FlowStepRaw.builder().from("B").to("A").description("link 2").build() //
                            )) //
                    ).build();

            var ws = load(config);
            var flows = ws.getModel().getFlows();
            assertEquals(1, flows.size());
            var flow = flows.get(0);
            assertEquals("flow1", flow.getId());
            assertEquals("flow 1", flow.getName());
            assertEquals(2, flow.getSteps().size());
            var step1 = flow.getSteps().get(0);
            assertEquals("A", step1.getFrom().getId());
            assertEquals("B", step1.getTo().getId());
            assertEquals("link 1", step1.getDescription());
            var step2 = flow.getSteps().get(1);
            assertEquals("B", step2.getFrom().getId());
            assertEquals("A", step2.getTo().getId());
            assertEquals("link 2", step2.getDescription());
        }

        @Test
        void given_flowWithoutId_then_fail() {
            var config = builder() //
                    .flow(FlowRaw.builder().name("flow 1")).build();

            var e = Assertions.assertThrows(IllegalArgumentException.class, () -> load(config));
            assertEquals("Field required on flow [id]", e.getMessage());
        }

        @Test
        void given_flowStepWithoutFrom_then_fail() {
            var config = builder() //
                    .softwareSystem("A") //
                    .softwareSystem("B") //
                    .flow(FlowRaw.builder().id("flow1").name("flow 1") //
                            .steps(Collections.singletonList( //
                                    FlowStepRaw.builder().to("B").description("link 1").build())) //
                    ).build();

            var e = Assertions.assertThrows(RuntimeException.class, () -> load(config));
            assertEquals("Error on flow flow1, step 0: Field required [from]", e.getMessage());
        }

        @Test
        void given_flowStepWithoutTo_then_fail() {
            var config = builder() //
                    .softwareSystem("A") //
                    .softwareSystem("B") //
                    .flow(FlowRaw.builder().id("flow1").name("flow 1") //
                            .steps(Collections.singletonList( //
                                    FlowStepRaw.builder().from("A").description("link 1").build())) //
                    ).build();

            var e = Assertions.assertThrows(RuntimeException.class, () -> load(config));
            assertEquals("Error on flow flow1, step 0: Field required [to]", e.getMessage());
        }

        @Test
        void given_flowStepWithInvalidRelation_then_fail() {
            var config = builder() //
                    .softwareSystem("A") //
                    .deploymentEnvironment("env") //
                    .flow(FlowRaw.builder().id("flow1").name("flow 1") //
                            .steps(Collections.singletonList( //
                                    FlowStepRaw.builder().from("A").to("env").description("link 1").build())) //
                    ).build();

            var e = Assertions.assertThrows(RelationNotAllowedException.class, () -> load(config));
            assertEquals("Relation from SoftwareSystem (A) to DeploymentEnvironment (env) is not allowed: Error on flow flow1, step 0", e.getMessage());
        }

        @Test
        void given_flowStep_then_indirectRelationsAreUpdated() {
            var config = builder() //
                    .softwareSystem("A") //
                    .container("A", "C1") //
                    .softwareSystem("B") //
                    .container("B", "C2") //
                    .flow(FlowRaw.builder().id("flow1").name("flow 1") //
                            .steps(Collections.singletonList( //
                                    FlowStepRaw.builder().from("C1").to("C2").description("link 1").build() //
                            )) //
                    ).build();

            var ws = load(config);
            var a_b = ws.getModel().getRelationManager().getImplicit().getRelationsBetween(ws.getModel().getElementById("A"), ws.getModel().getElementById("B"));
            assertEquals(1, a_b.size());
            assertEquals("link 1", a_b.get(0).getSource().getDescription());
        }

        @Test
        void given_flowStep_then_directRelationsAreUpdated() {
            var config = builder() //
                    .softwareSystem("A") //
                    .container("A", "C1") //
                    .softwareSystem("B") //
                    .container("B", "C2") //
                    .flow(FlowRaw.builder().id("flow1").name("flow 1") //
                            .steps(Collections.singletonList( //
                                    FlowStepRaw.builder().from("C1").to("C2").description("link 1").build() //
                            )) //
                    ).build();

            var ws = load(config);
            var relations = ws.getModel().getRelationManager().getDirect().getRelationsBetween(ws.getModel().getElementById("C1"), ws.getModel().getElementById("C2"));
            assertEquals(1, relations.size());
            assertEquals("link 1", relations.get(0).getDescription());
        }
    }
}
