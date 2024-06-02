package it.vitalegi.archi.view;

import it.vitalegi.archi.exception.ElementNotFoundException;
import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.util.WorkspaceLoaderBuilder;
import it.vitalegi.archi.util.WorkspaceUtil;
import it.vitalegi.archi.view.dto.DeploymentView;
import it.vitalegi.archi.workspace.Workspace;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static it.vitalegi.archi.util.AssertionUtil.assertArrayEqualsUnsorted;
import static it.vitalegi.archi.util.ModelUtil.defaultBuilder;
import static it.vitalegi.archi.util.ModelUtil.defaultLoader;
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

        WorkspaceLoaderBuilder model;

        @BeforeEach
        void init() {
            model = b() //
                    .softwareSystem("A") //
                    .container("A", "A_C1") //
                    .container("A", "A_C2") //
                    .container("A", "A_C3") //

                    .group("group1") //
                    .softwareSystem("group1", "B") //

                    .softwareSystem("C") //
                    .container("C", "C_C1") //

                    .softwareSystem("D") //

                    .deploymentEnvironment("env1") //
                    .deploymentNode("env1", "env1_node_1") //
                    .deploymentNode("env1", "env1_node_2") //
                    .deploymentNode("env1", "env1_node_3") //
                    .infrastructureNode("env1_node_3", "env1_infra1")
                    .softwareSystemInstance("env1_node_1", "env1_A", "A") //
                    .containerInstance("env1_node_1", "env1_A_C1", "A_C1") //
                    .containerInstance("env1_node_1", "env1_A_C2", "A_C2") //
                    .softwareSystemInstance("env1_node_1", "env1_B", "B") //

                    .relation("A_C1", "B") //
                    .relation("A_C2", "C_C1") //


                    .deploymentEnvironment("env2") //
                    .deploymentNode("env2", "env2_node_1") //
                    .deploymentNode("env2", "env2_node_2") //
                    .softwareSystemInstance("env2_node_1", "env2_C", "C") //

                    .deploymentView("*", "env1", "view1_all") //
                    .deploymentView("A", "env1", "view1_A") //
                    .deploymentView("*", "env2", "view2") //
            ;
        }

        @Test
        void given_scopeAll_thenAllContainerInstancesAreIncluded() {
            var ws = load(model);
            var view = ws.getViews().getByName("view1_all");
            var elements = viewProcessor.getElementsInScope(viewProcessor.cast(view));
            var ids = getIds(WorkspaceUtil.getContainerInstances(elements));
            assertArrayEqualsUnsorted(Arrays.asList("env1_A_C1", "env1_A_C2"), ids);
        }

        @Test
        void given_scopeAll_thenAllSoftwareSystemInstancesAreIncluded() {
            var ws = load(model);
            var view = ws.getViews().getByName("view1_all");
            var elements = viewProcessor.getElementsInScope(viewProcessor.cast(view));
            var ids = getIds(WorkspaceUtil.getSoftwareSystemInstances(elements));
            assertArrayEqualsUnsorted(Arrays.asList("env1_A", "env1_B"), ids);
        }

        @DisplayName("Given scope SoftwareSystem then should include 1) the element of the software system itself 2) other containerInstances/softwareSystemInstances with max distance 1 from any of the elements in scope at point 1, 3) infrastructureNode with max distance 1 from any of the elements in scope at point 1, 4) DeploymentNode parent of any of the elements at point 1, 2, 3")
        @Test
        void given_scopeSoftwareSystem_thenOnlyContainerInstancesAndSoftwareSystemInstancesWithMaxDistance1AreIncluded() {
            var ws = load(model);
            var view = ws.getViews().getByName("view1_all");
            var elements = viewProcessor.getElementsInScope(viewProcessor.cast(view));
            assertArrayEqualsUnsorted(Arrays.asList("env1_A_C1", "env1_A_C2"), getIds(WorkspaceUtil.getContainerInstances(elements)));
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
