package it.vitalegi.archi.exporter.plantuml;

import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.relation.Relation;
import it.vitalegi.archi.model.diagram.DeploymentDiagram;
import it.vitalegi.archi.exception.ElementNotFoundException;
import it.vitalegi.archi.util.WorkspaceUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

import static it.vitalegi.archi.util.AssertionUtil.assertArrayEqualsUnsorted;
import static it.vitalegi.archi.util.WorkspaceTestUtil.b;
import static it.vitalegi.archi.util.WorkspaceTestUtil.load;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class DeploymentDiagramPlantumlExporterTests {

    DeploymentDiagramPlantumlExporter processor;

    @BeforeEach
    void init() {
        processor = new DeploymentDiagramPlantumlExporter();
    }

    @Nested
    class Loader {
        @Test
        void given_correctConfiguration_thenSucceed() {
            var ws = load(b().deploymentEnvironment("prod").deploymentDiagram("*", "prod", "123"));
            var diagram = ws.getDiagrams().getByName("123");
            assertNotNull(diagram);
            assertEquals(diagram.getClass(), DeploymentDiagram.class);
            var deploymentDiagram = (DeploymentDiagram) diagram;
            assertEquals("prod", deploymentDiagram.getEnvironment());
            assertEquals("*", deploymentDiagram.getScope());
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
                        .deploymentDiagram("*", "prod", "123") //
                );
                var diagram = (DeploymentDiagram) ws.getDiagrams().getByName("123");
                processor.validate(diagram);
            }
        }

        @Nested
        class ScopeSoftwareSystem {
            @Test
            void given_SoftwareSystemIsExists_thenSucceed() {
                var ws = load(b() //
                        .deploymentEnvironment("prod") //
                        .deploymentDiagram("A", "prod", "123") //
                        .softwareSystem("A") //
                );
                var diagram = (DeploymentDiagram) ws.getDiagrams().getByName("123");
                processor.validate(diagram);
            }

            @Test
            void given_SoftwareSystemIsMissing_thenFail() {
                var e = Assertions.assertThrows(ElementNotFoundException.class, () -> load(b() //
                        .deploymentEnvironment("prod") //
                        .deploymentDiagram("B", "prod", "123") //
                        .softwareSystem("A") //
                ));
                assertEquals("Can't find B: Scope B on diagram 123 is invalid. Check if all objects exist.", e.getMessage());
            }
        }


        @Nested
        class DeploymentEnvironment {
            @Test
            void given_missingDeploymentEnvironment_thenFail() {
                var e = Assertions.assertThrows(ElementNotFoundException.class, () -> load(b() //
                        .deploymentEnvironment("qa") //
                        .deploymentDiagram("*", "prod", "123") //
                ));
                assertEquals("Can't find prod: required on diagram 123", e.getMessage());
            }
        }
    }

    @Nested
    class ElementsPerimeter {
        @Test
        void when_containerInstanceIsIncluded_then_containerIsIncluded() {
            var ws = load(b() //
                    .softwareSystem("A") //
                    .container("A", "container") //

                    .deploymentEnvironment("env1") //
                    .deploymentNode("env1", "node_1") //
                    .containerInstance("node_1", "node_container", "container") //

                    .deploymentDiagram("*", "env1", "diagram") //
            );
            var diagram = ws.getDiagrams().getByName("diagram");
            var elements = processor.getElementsPerimeter((DeploymentDiagram) (diagram)).collect(Collectors.toList());
            assertArrayEqualsUnsorted(List.of("node_container"), getIds(WorkspaceUtil.getContainerInstances(elements)));
            assertArrayEqualsUnsorted(List.of("container"), getIds(WorkspaceUtil.getContainers(elements)));
        }

        @Test
        void when_softwareSystemInstanceIsIncluded_then_softwareSystemIsIncluded() {
            var ws = load(b() //
                    .softwareSystem("A") //

                    .deploymentEnvironment("env1") //
                    .deploymentNode("env1", "node_1") //
                    .softwareSystemInstance("node_1", "node_A", "A") //

                    .deploymentDiagram("*", "env1", "diagram") //
            );
            var diagram = ws.getDiagrams().getByName("diagram");
            var elements = processor.getElementsPerimeter((DeploymentDiagram) (diagram)).collect(Collectors.toList());
            assertArrayEqualsUnsorted(List.of("node_A"), getIds(WorkspaceUtil.getSoftwareSystemInstances(elements)));
            assertArrayEqualsUnsorted(List.of("A"), getIds(WorkspaceUtil.getSoftwareSystems(elements)));
        }

        @Test
        void when_elementIsImportedMultipleTimes_then_onlyOneElementIsReturned() {
            var ws = load(b() //
                    .softwareSystem("A") //

                    .deploymentEnvironment("env1") //
                    .deploymentNode("env1", "node_1") //
                    .softwareSystemInstance("node_1", "node_A1", "A") //
                    .softwareSystemInstance("node_1", "node_A2", "A") //

                    .deploymentDiagram("*", "env1", "diagram") //
            );
            var diagram = ws.getDiagrams().getByName("diagram");
            var elements = processor.getElementsPerimeter((DeploymentDiagram) (diagram)).collect(Collectors.toList());
            assertArrayEqualsUnsorted(List.of("node_A1", "node_A2"), getIds(WorkspaceUtil.getSoftwareSystemInstances(elements)));
            assertArrayEqualsUnsorted(List.of("A"), getIds(WorkspaceUtil.getSoftwareSystems(elements)));
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

                    .deploymentDiagram("*", "env1", "diagram") //
            );
            var diagram = ws.getDiagrams().getByName("diagram");
            var elements = processor.getElementsInScope((DeploymentDiagram) (diagram));
            assertArrayEqualsUnsorted(asList("env1_A_C1", "env1_A_C2"), getIds(WorkspaceUtil.getContainerInstances(elements)));
            assertArrayEqualsUnsorted(asList("A_C1", "A_C2"), getIds(WorkspaceUtil.getContainers(elements)));
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

                    .deploymentDiagram("*", "env1", "diagram") //
            );
            var diagram = ws.getDiagrams().getByName("diagram");
            var elements = processor.getElementsInScope((DeploymentDiagram) (diagram));
            assertArrayEqualsUnsorted(asList("env1_A", "env1_B"), getIds(WorkspaceUtil.getSoftwareSystemInstances(elements)));
            assertArrayEqualsUnsorted(asList("A", "B"), getIds(WorkspaceUtil.getSoftwareSystems(elements)));
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

                    .deploymentDiagram("A", "env1", "diagram") //
            );
            var diagram = ws.getDiagrams().getByName("diagram");
            var elements = processor.getElementsInScope((DeploymentDiagram) (diagram));
            assertArrayEqualsUnsorted(asList("env1_A_C1", "env1_A_C2", "env1_B_C1", "env1_B_C2"), getIds(WorkspaceUtil.getContainerInstances(elements)));
            assertArrayEqualsUnsorted(asList("A_C1", "A_C2", "B_C1", "B_C2"), getIds(WorkspaceUtil.getContainers(elements)));
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

                    .deploymentDiagram("A", "env1", "diagram") //
            );
            var diagram = ws.getDiagrams().getByName("diagram");
            var elements = processor.getElementsInScope((DeploymentDiagram) (diagram));
            assertArrayEqualsUnsorted(asList("env1_A", "env1_B"), getIds(WorkspaceUtil.getSoftwareSystemInstances(elements)));
            assertArrayEqualsUnsorted(asList("A", "B"), getIds(WorkspaceUtil.getSoftwareSystems(elements)));
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

                    .deploymentDiagram("A", "env1", "diagram") //
            );
            var diagram = ws.getDiagrams().getByName("diagram");
            var elements = processor.getElementsInScope((DeploymentDiagram) (diagram));
            assertArrayEqualsUnsorted(List.of("infra1"), getIds(WorkspaceUtil.getInfrastructureNodes(elements)));
        }
    }


    @Nested
    class RelationsInScope {

        @Test
        void when_containersAreInScope_thenRelationIsIncluded() {
            var ws = load(b() //
                    .softwareSystem("A") //
                    .container("A", "A_C1") //
                    .container("A", "A_C2") //

                    .deploymentEnvironment("env1") //
                    .deploymentNode("env1", "node_1") //

                    .containerInstance("node_1", "node_1_A_C1", "A_C1") //
                    .containerInstance("node_1", "node_1_A_C2", "A_C2") //

                    .relation("A_C1", "A_C2") //
                    .deploymentDiagram("*", "env1", "diagram") //
            );
            var diagram = ws.getDiagrams().getByName("diagram");
            var elements = processor.getElementsInScope((DeploymentDiagram) (diagram));

            var relations = processor.getRelationsInScope((DeploymentDiagram) (diagram), elements);
            assertArrayEqualsUnsorted(asList("Container (A_C1) => Container (A_C2)"), stringifyRelations(relations));
        }

        @Test
        void when_softwareSystemsAreInScope_thenRelationIsIncluded() {
            var ws = load(b() //
                    .softwareSystem("A") //
                    .softwareSystem("B") //

                    .deploymentEnvironment("env1") //
                    .deploymentNode("env1", "node_1") //

                    .softwareSystemInstance("node_1", "node_1_A", "A") //
                    .softwareSystemInstance("node_1", "node_1_B", "B") //

                    .relation("A", "B") //
                    .deploymentDiagram("*", "env1", "diagram") //
            );
            var diagram = ws.getDiagrams().getByName("diagram");
            var elements = processor.getElementsInScope((DeploymentDiagram) (diagram));

            var relations = processor.getRelationsInScope((DeploymentDiagram) (diagram), elements);
            assertArrayEqualsUnsorted(List.of("SoftwareSystem (A) => SoftwareSystem (B)"), stringifyRelations(relations));
        }


        @Test
        void when_elementsAreInScope_thenDirectRelationsAreIncluded() {
            var ws = load(b() //
                    .softwareSystem("A") //
                    .container("A", "A_C1") //
                    .container("A", "A_C2") //

                    .softwareSystem("B") //

                    .deploymentEnvironment("env1") //
                    .deploymentNode("env1", "node_1") //
                    .deploymentNode("env1", "node_2") //

                    .containerInstance("node_1", "node_1_A_C1", "A_C1") //
                    .containerInstance("node_1", "node_1_A_C2", "A_C2") //

                    .softwareSystemInstance("node_1", "node_1_A", "A") //
                    .softwareSystemInstance("node_2", "node_2_B", "B") //
                    .infrastructureNode("node_1", "node_1_infra1") //

                    .relation("A_C1", "A_C2") //
                    .relation("B", "A") //
                    .relation("node_1", "node_2") //
                    .relation("node_1_infra1", "node_1_A_C1") //

                    .deploymentDiagram("*", "env1", "diagram") //
            );
            var diagram = ws.getDiagrams().getByName("diagram");
            var elements = processor.getElementsInScope((DeploymentDiagram) (diagram));
            assertArrayEqualsUnsorted(asList("node_1_A", "node_2_B"), getIds(WorkspaceUtil.getSoftwareSystemInstances(elements)));
            assertArrayEqualsUnsorted(asList("node_1_A_C1", "node_1_A_C2"), getIds(WorkspaceUtil.getContainerInstances(elements)));
            assertArrayEqualsUnsorted(asList("node_1", "node_2"), getIds(WorkspaceUtil.getDeploymentNodes(elements)));
            assertArrayEqualsUnsorted(List.of("node_1_infra1"), getIds(WorkspaceUtil.getInfrastructureNodes(elements)));

            var relations = processor.getRelationsInScope((DeploymentDiagram) (diagram), elements);
            assertArrayEqualsUnsorted(asList("Container (A_C1) => Container (A_C2)", "SoftwareSystem (B) => SoftwareSystem (A)", "DeploymentNode (node_1) => DeploymentNode (node_2)", "InfrastructureNode (node_1_infra1) => ContainerInstance (node_1_A_C1)"), stringifyRelations(relations));
        }

        @Test
        void when_containerIsOutOfScope_thenRelationIsExcluded() {
            var ws = load(b() //
                    .softwareSystem("A") //
                    .container("A", "A_C") //
                    .softwareSystem("B") //
                    .container("B", "B_C") //
                    .softwareSystem("C") //
                    .container("C", "C_C") //

                    .deploymentEnvironment("env1") //
                    .deploymentNode("env1", "node_1") //

                    .containerInstance("node_1", "node_1_A_C", "A_C") //
                    .containerInstance("node_1", "node_1_B_C", "B_C") //
                    .containerInstance("node_1", "node_1_C_C", "C_C") //

                    .relation("A_C", "B_C") //
                    .relation("B_C", "C_C") //

                    .deploymentDiagram("A", "env1", "diagram") //
            );
            var diagram = ws.getDiagrams().getByName("diagram");
            var elements = processor.getElementsInScope((DeploymentDiagram) (diagram));
            assertArrayEqualsUnsorted(asList("node_1_A_C", "node_1_B_C"), getIds(WorkspaceUtil.getContainerInstances(elements)));

            var relations = processor.getRelationsInScope((DeploymentDiagram) (diagram), elements);
            assertArrayEqualsUnsorted(List.of("Container (A_C) => Container (B_C)"), stringifyRelations(relations));
        }


        @Test
        void when_softwareSystemIsOutOfScope_thenRelationIsExcluded() {
            var ws = load(b() //
                    .softwareSystem("A") //
                    .softwareSystem("B") //
                    .softwareSystem("C") //

                    .deploymentEnvironment("env1") //
                    .deploymentNode("env1", "node_1") //

                    .softwareSystemInstance("node_1", "node_1_A", "A") //
                    .softwareSystemInstance("node_1", "node_1_B", "B") //
                    .softwareSystemInstance("node_1", "node_1_C", "C") //

                    .relation("A", "B") //
                    .relation("B", "C") //

                    .deploymentDiagram("A", "env1", "diagram") //
            );
            var diagram = ws.getDiagrams().getByName("diagram");
            var elements = processor.getElementsInScope((DeploymentDiagram) (diagram));
            assertArrayEqualsUnsorted(asList("node_1_A", "node_1_B"), getIds(WorkspaceUtil.getSoftwareSystemInstances(elements)));
            assertArrayEqualsUnsorted(asList("A", "B"), getIds(WorkspaceUtil.getSoftwareSystems(elements)));

            var relations = processor.getRelationsInScope((DeploymentDiagram) (diagram), elements);
            assertArrayEqualsUnsorted(List.of("SoftwareSystem (A) => SoftwareSystem (B)"), stringifyRelations(relations));
        }


        // relazioni tra elementi di cui uno NON in scope NON sono mantenute
        // relazioni implicite tra elementi in scope sono mantenute


    }

    static List<String> getIds(List<? extends Element> elements) {
        return elements.stream().map(Element::getId).collect(Collectors.toList());
    }

    static List<String> stringifyRelations(List<? extends Relation> relations) {
        return relations.stream().map(r -> r.getFrom().toShortString() + " => " + r.getTo().toShortString()).collect(Collectors.toList());
    }
}
