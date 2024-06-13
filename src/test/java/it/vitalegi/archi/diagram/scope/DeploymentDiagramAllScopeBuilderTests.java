package it.vitalegi.archi.diagram.scope;

import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.DeploymentDiagram;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.relation.Relation;
import it.vitalegi.archi.util.WorkspaceUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

import static it.vitalegi.archi.util.AssertionUtil.assertArrayEqualsUnsorted;
import static it.vitalegi.archi.util.WorkspaceTestUtil.b;
import static it.vitalegi.archi.util.WorkspaceTestUtil.load;
import static it.vitalegi.archi.workspaceloader.model.DeploymentDiagramRaw.all;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DeploymentDiagramAllScopeBuilderTests {

    @Test
    void given_emptyEnvironment_then_shouldReturn() {
        var ws = load(b() //
                .deploymentEnvironment("env") //
                .deploymentDiagram(all("diagram", "env")) //
        );
        var scope = scopeBuilder(ws, "diagram").computeScope();
        assertEquals(0, scope.getElements().size());
        assertEquals(0, scope.getRelations().size());
    }

    @Test
    void given_containerInstanceIsIncluded_then_containerIsIncluded() {
        var ws = load(b() //
                .softwareSystem("A") //
                .container("A", "container") //

                .deploymentEnvironment("env1") //
                .deploymentNode("env1", "node_1") //
                .containerInstance("node_1", "node_container", "container") //

                .deploymentDiagram(all("diagram", "env1")) //
        );
        var scope = scopeBuilder(ws, "diagram").computeScope();
        var elements = scope.getElements();
        assertArrayEqualsUnsorted(List.of("node_container"), getIds(WorkspaceUtil.getContainerInstances(elements)));
        assertArrayEqualsUnsorted(List.of("container"), getIds(WorkspaceUtil.getContainers(elements)));
    }

    @Test
    void given_softwareSystemInstanceIsIncluded_then_softwareSystemIsIncluded() {
        var ws = load(b() //
                .softwareSystem("A") //

                .deploymentEnvironment("env1") //
                .deploymentNode("env1", "node_1") //
                .softwareSystemInstance("node_1", "node_A", "A") //

                .deploymentDiagram(all("diagram", "env1")) //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var scope = scopeBuilder(ws, "diagram").computeScope();
        var elements = scope.getElements();
        assertArrayEqualsUnsorted(List.of("node_A"), getIds(WorkspaceUtil.getSoftwareSystemInstances(elements)));
        assertArrayEqualsUnsorted(List.of("A"), getIds(WorkspaceUtil.getSoftwareSystems(elements)));
    }

    @Test
    void given_multipleContainerInstances_then_allContainerInstancesAreIncluded() {
        var ws = load(b() //
                .softwareSystem("A") //

                .container("A", "A_C1") //
                .container("A", "A_C2") //

                .deploymentEnvironment("env1") //
                .deploymentNode("env1", "node_1") //
                .containerInstance("node_1", "env1_A_C1", "A_C1") //
                .containerInstance("node_1", "env1_A_C2", "A_C2") //

                .deploymentDiagram(all("diagram", "env1")) //
        );
        var scope = scopeBuilder(ws, "diagram").computeScope();
        var elements = scope.getElements();
        assertArrayEqualsUnsorted(asList("env1_A_C1", "env1_A_C2"), getIds(WorkspaceUtil.getContainerInstances(elements)));
        assertArrayEqualsUnsorted(asList("A_C1", "A_C2"), getIds(WorkspaceUtil.getContainers(elements)));
    }

    @Test
    void given_multipleSoftwareSystemInstances_then_allSoftwareSystemInstancesAreIncluded() {
        var ws = load(b() //
                .softwareSystem("A") //
                .softwareSystem("B") //

                .container("A", "A_C1") //
                .container("A", "A_C2") //

                .deploymentEnvironment("env1") //
                .deploymentNode("env1", "node_1") //
                .softwareSystemInstance("node_1", "env1_A", "A") //
                .softwareSystemInstance("node_1", "env1_B", "B") //

                .deploymentDiagram(all("diagram", "env1")) //
        );
        var scope = scopeBuilder(ws, "diagram").computeScope();
        var elements = scope.getElements();
        assertArrayEqualsUnsorted(asList("env1_A", "env1_B"), getIds(WorkspaceUtil.getSoftwareSystemInstances(elements)));
        assertArrayEqualsUnsorted(asList("A", "B"), getIds(WorkspaceUtil.getSoftwareSystems(elements)));
    }

    @Test
    void given_multipleEnvironments_then_onlyInstancesOfTargetEnvironmentAreIncluded() {
        var ws = load(b() //
                .softwareSystem("A") //

                .container("A", "A_C1") //
                .container("A", "A_C2") //

                .deploymentEnvironment("env1") //
                .deploymentNode("env1", "node_1") //
                .containerInstance("node_1", "env1_A_C1", "A_C1") //
                .containerInstance("node_1", "env1_A_C2", "A_C2") //

                .deploymentEnvironment("env2") //
                .deploymentNode("env2", "node_2") //
                .containerInstance("node_2", "env2_A_C1", "A_C1") //
                .containerInstance("node_2", "env2_A_C2", "A_C2") //

                .deploymentDiagram(all("diagram", "env1")) //
        );
        var scope = scopeBuilder(ws, "diagram").computeScope();
        var elements = scope.getElements();
        assertArrayEqualsUnsorted(asList("env1_A_C1", "env1_A_C2"), getIds(WorkspaceUtil.getContainerInstances(elements)));
        assertArrayEqualsUnsorted(asList("A_C1", "A_C2"), getIds(WorkspaceUtil.getContainers(elements)));
    }


    @Test
    void given_containersAreInScope_thenRelationIsIncluded() {
        var ws = load(b() //
                .softwareSystem("A") //

                .container("A", "A_C1") //
                .container("A", "A_C2") //

                .deploymentEnvironment("env1") //
                .deploymentNode("env1", "node_1") //

                .containerInstance("node_1", "node_1_A_C1", "A_C1") //
                .containerInstance("node_1", "node_1_A_C2", "A_C2") //

                .relation("A_C1", "A_C2") //

                .deploymentDiagram(all("diagram", "env1")) //
        );
        var scope = scopeBuilder(ws, "diagram").computeScope();
        var relations = scope.getRelations();
        assertArrayEqualsUnsorted(asList("Container (A_C1) => Container (A_C2)"), stringifyRelations(relations));
    }

    @Test
    void given_softwareSystemsAreInScope_thenRelationIsIncluded() {
        var ws = load(b() //
                .softwareSystem("A") //
                .softwareSystem("B") //

                .deploymentEnvironment("env1") //
                .deploymentNode("env1", "node_1") //

                .softwareSystemInstance("node_1", "node_1_A", "A") //
                .softwareSystemInstance("node_1", "node_1_B", "B") //

                .relation("A", "B") //

                .deploymentDiagram(all("diagram", "env1")) //
        );
        var scope = scopeBuilder(ws, "diagram").computeScope();
        var relations = scope.getRelations();
        assertArrayEqualsUnsorted(List.of("SoftwareSystem (A) => SoftwareSystem (B)"), stringifyRelations(relations));
    }

    @Test
    void given_elementsAreInScope_thenDirectRelationsAreIncluded() {
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

                .deploymentDiagram(all("diagram", "env1")) //
        );
        var scope = scopeBuilder(ws, "diagram").computeScope();
        var elements = scope.getElements();
        var relations = scope.getRelations();

        assertArrayEqualsUnsorted(asList("node_1_A", "node_2_B"), getIds(WorkspaceUtil.getSoftwareSystemInstances(elements)));
        assertArrayEqualsUnsorted(asList("node_1_A_C1", "node_1_A_C2"), getIds(WorkspaceUtil.getContainerInstances(elements)));
        assertArrayEqualsUnsorted(asList("node_1", "node_2"), getIds(WorkspaceUtil.getDeploymentNodes(elements)));
        assertArrayEqualsUnsorted(List.of("node_1_infra1"), getIds(WorkspaceUtil.getInfrastructureNodes(elements)));

        assertArrayEqualsUnsorted(asList("Container (A_C1) => Container (A_C2)", "SoftwareSystem (B) => SoftwareSystem (A)", "DeploymentNode (node_1) => DeploymentNode (node_2)", "InfrastructureNode (node_1_infra1) => ContainerInstance (node_1_A_C1)"), stringifyRelations(relations));
    }

    static Element getElementById(Workspace ws, String id) {
        return ws.getModel().getElementById(id);
    }

    static DeploymentDiagramScopeBuilder scopeBuilder(Workspace ws, String diagramName) {
        var diagram = ws.getDiagrams().getByName(diagramName);
        if (diagram instanceof DeploymentDiagram) {
            return new DeploymentDiagramAllScopeBuilder((DeploymentDiagram) diagram);
        }
        throw new IllegalArgumentException("Not a DeploymentDiagram: " + diagram);
    }

    static List<String> getIds(List<? extends Element> elements) {
        return elements.stream().map(Element::getId).collect(Collectors.toList());
    }

    static List<String> stringifyRelations(List<? extends Relation> relations) {
        return relations.stream().map(r -> r.getFrom().toShortString() + " => " + r.getTo().toShortString()).collect(Collectors.toList());
    }
}
