package it.vitalegi.archi.view;

import it.vitalegi.archi.exception.ElementNotFoundException;
import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.util.WorkspaceLoaderBuilder;
import it.vitalegi.archi.util.WorkspaceUtil;
import it.vitalegi.archi.view.dto.DeploymentView;
import it.vitalegi.archi.workspace.Workspace;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

import static it.vitalegi.archi.util.AssertionUtil.assertArrayEqualsUnsorted;
import static it.vitalegi.archi.util.ModelUtil.defaultBuilder;
import static it.vitalegi.archi.util.ModelUtil.defaultLoader;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class DeploymentViewProcessorTests {

    DeploymentViewProcessor viewProcessor;

    @BeforeEach
    void init() {
        viewProcessor = new DeploymentViewProcessor();

    }

    @Nested
    class Loader {
        @Test
        void given_correctConfiguration_thenSucceed() {
            var ws = load(b().deploymentEnvironment("prod").deploymentView("*", "prod", "123"));
            var view = ws.getViews().getByName("123");
            assertNotNull(view);
            assertEquals(view.getClass(), DeploymentView.class);
            var deploymentView = (DeploymentView) view;
            assertEquals("prod", deploymentView.getEnvironment());
            assertEquals("*", deploymentView.getScope());
        }
    }

    @Nested
    class Accept {
        @Test
        void given_deploymentView_then_shouldReturnTrue() {
            var ws = load(b().deploymentEnvironment("prod").deploymentView(null, "prod", "view1"));
            assertTrue(viewProcessor.accept(ws.getViews().getByName("view1")));
        }
    }

    @Nested
    class Validate {

        @Nested
        class ScopeAll {
            @Test
            void when_scopeIsAll_thenSucceed() {
                var ws = load(b() //
                        .deploymentEnvironment("prod") //
                        .deploymentView("*", "prod", "123") //
                );
                var view = ws.getViews().getByName("123");
                viewProcessor.validate(view);
            }
        }

        @Nested
        class ScopeSoftwareSystem {
            @Test
            void given_SoftwareSystemIsExists_thenSucceed() {
                var ws = load(b() //
                        .deploymentEnvironment("prod") //
                        .deploymentView("A", "prod", "123") //
                        .softwareSystem("A") //
                );
                var view = ws.getViews().getByName("123");
                viewProcessor.validate(view);
            }

            @Test
            void given_SoftwareSystemIsMissing_thenFail() {
                var e = Assertions.assertThrows(ElementNotFoundException.class, () -> load(b() //
                        .deploymentEnvironment("prod") //
                        .deploymentView("B", "prod", "123") //
                        .softwareSystem("A") //
                ));
                assertEquals("Can't find B: Scope B on view 123 is invalid. Check if all objects exist.", e.getMessage());
            }
        }


        @Nested
        class DeploymentEnvironment {
            @Test
            void given_missingDeploymentEnvironment_thenFail() {
                var e = Assertions.assertThrows(ElementNotFoundException.class, () -> load(b() //
                        .deploymentEnvironment("qa") //
                        .deploymentView("*", "prod", "123") //
                ));
                assertEquals("Can't find prod: required on view 123", e.getMessage());
            }
        }
    }

    @Nested
    class ElementsInScope {

        @Test
        void given_scopeAll_thenAllContainerInstancesAreIncluded() {
            var ws = load(b() //
                    .softwareSystem("A") //
                    .container("A", "A_C1") //
                    .container("A", "A_C2") //

                    .deploymentEnvironment("env1") //
                    .deploymentNode("env1", "node_1") //
                    .containerInstance("node_1", "env1_A_C1", "A_C1") //
                    .containerInstance("node_1", "env1_A_C2", "A_C2") //

                    .deploymentView("*", "env1", "view") //
            );
            var view = ws.getViews().getByName("view");
            var elements = viewProcessor.getElementsInScope(viewProcessor.cast(view));
            var ids = getIds(WorkspaceUtil.getContainerInstances(elements));
            assertArrayEqualsUnsorted(asList("env1_A_C1", "env1_A_C2"), ids);
        }

        @Test
        void given_scopeAll_thenAllSoftwareSystemInstancesAreIncluded() {
            var ws = load(b() //
                    .softwareSystem("A") //
                    .softwareSystem("B") //

                    .deploymentEnvironment("env1") //
                    .deploymentNode("env1", "node_1") //
                    .softwareSystemInstance("node_1", "env1_A", "A") //
                    .softwareSystemInstance("node_1", "env1_B", "B") //

                    .deploymentView("*", "env1", "view") //
            );
            var view = ws.getViews().getByName("view");
            var elements = viewProcessor.getElementsInScope(viewProcessor.cast(view));
            var ids = getIds(WorkspaceUtil.getSoftwareSystemInstances(elements));
            assertArrayEqualsUnsorted(asList("env1_A", "env1_B"), ids);
        }


        @Test
        void given_scopeSoftwareSystem_thenAllConnectedContainerInstancesAreIncluded() {
            var ws = load(b() //
                    .softwareSystem("A") //
                    .container("A", "A_C1") //
                    .container("A", "A_C2") //
                    .softwareSystem("B") //
                    .container("B", "B_C1") //
                    .container("B", "B_C2") //
                    .container("B", "B_C3") //

                    .relation("A_C1", "B_C1") //
                    .relation("B_C2", "A_C2") //

                    .deploymentEnvironment("env1") //
                    .deploymentNode("env1", "node_1") //
                    .containerInstance("node_1", "env1_A_C1", "A_C1") //
                    .containerInstance("node_1", "env1_A_C2", "A_C2") //
                    .containerInstance("node_1", "env1_B_C1", "B_C1") //
                    .containerInstance("node_1", "env1_B_C2", "B_C2") //
                    .containerInstance("node_1", "env1_B_C3", "B_C3") //

                    .deploymentView("A", "env1", "view") //
            );
            var view = ws.getViews().getByName("view");
            var elements = viewProcessor.getElementsInScope(viewProcessor.cast(view));
            var ids = getIds(WorkspaceUtil.getContainerInstances(elements));
            assertArrayEqualsUnsorted(asList("env1_A_C1", "env1_A_C2", "env1_B_C1", "env1_B_C2"), ids);
        }


        @Test
        void given_scopeSoftwareSystem_thenAllConnectedSoftwareSystemInstancesAreIncluded() {
            var ws = load(b() //
                    .softwareSystem("A") //
                    .softwareSystem("B") //
                    .softwareSystem("C") //

                    .relation("A", "B") //

                    .deploymentEnvironment("env1") //
                    .deploymentNode("env1", "node_1") //
                    .softwareSystemInstance("node_1", "env1_A", "A") //
                    .softwareSystemInstance("node_1", "env1_B", "B") //
                    .softwareSystemInstance("node_1", "env1_C", "C") //

                    .deploymentView("A", "env1", "view") //
            );
            var view = ws.getViews().getByName("view");
            var elements = viewProcessor.getElementsInScope(viewProcessor.cast(view));
            var ids = getIds(WorkspaceUtil.getSoftwareSystemInstances(elements));
            assertArrayEqualsUnsorted(asList("env1_A", "env1_B"), ids);
        }

        @Test
        void given_scopeSoftwareSystem_thenAllConnectedInfrastructureNodesAreIncluded() {
            var ws = load(b() //
                    .softwareSystem("A") //
                    .container("A", "A1") //

                    .deploymentEnvironment("env1") //
                    .deploymentNode("env1", "node1") //
                    .containerInstance("node1", "node1_A", "A1") //
                    .infrastructureNode("node1", "infra1") //
                    .infrastructureNode("node1", "infra2") //

                    .relation("node1_A", "infra1") //

                    .deploymentView("A", "env1", "view") //
            );
            var view = ws.getViews().getByName("view");
            var elements = viewProcessor.getElementsInScope(viewProcessor.cast(view));
            var ids = getIds(WorkspaceUtil.getInfrastructureNodes(elements));
            assertArrayEqualsUnsorted(asList("infra1"), ids);
        }
    }

    static Workspace load(WorkspaceLoaderBuilder builder) {
        return defaultLoader().load(builder.build());
    }

    static WorkspaceLoaderBuilder b() {
        return defaultBuilder();
    }

    static List<String> getIds(List<? extends Element> elements) {
        return elements.stream().map(Element::getId).collect(Collectors.toList());
    }
}
