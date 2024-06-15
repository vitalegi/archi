package it.vitalegi.archi.diagram.scope;

import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.DeploymentDiagram;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.util.WorkspaceUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

import static it.vitalegi.archi.util.AssertionUtil.assertArrayEqualsUnsorted;
import static it.vitalegi.archi.util.WorkspaceTestUtil.b;
import static it.vitalegi.archi.util.WorkspaceTestUtil.load;
import static it.vitalegi.archi.util.WorkspaceTestUtil.stringifyRelations;
import static it.vitalegi.archi.workspaceloader.model.DeploymentDiagramRaw.scoped;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

@ExtendWith(MockitoExtension.class)
public class DeploymentDiagramSoftwareSystemScopeBuilderTests {


    @Test
    void given_scopeSoftwareSystem_thenSoftwareSystemIsInScope() {
        var ws = load(b() //

                .softwareSystem("A") //
                .container("A", "A_C1") //
                .container("A", "A_C2") //

                .relation("A_C1", "A_C2") //

                .deploymentEnvironment("env1") //
                .deploymentNode("env1", "node_1") //
                .containerInstance("node_1", "env1_A_C1", "A_C1") //
                .containerInstance("node_1", "env1_A_C2", "A_C2") //

                .deploymentDiagram(scoped("diagram", "env1", "A")) //
        );
        var scope = scopeBuilder(ws, "diagram").computeScope();
        var elements = scope.getElements();
        var relations = scope.getRelations();

        assertArrayEqualsUnsorted(asList("env1_A_C1", "env1_A_C2"), getIds(WorkspaceUtil.getContainerInstances(elements)));
        assertArrayEqualsUnsorted(asList("A_C1", "A_C2"), getIds(WorkspaceUtil.getContainers(elements)));
        assertArrayEqualsUnsorted(List.of("Container (A_C1) => Container (A_C2)"), stringifyRelations(relations));
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

                .deploymentDiagram(scoped("diagram", "env1", "A")) //
        );
        var scope = scopeBuilder(ws, "diagram").computeScope();
        var elements = scope.getElements();
        var relations = scope.getRelations();

        assertArrayEqualsUnsorted(asList("env1_A_C1", "env1_A_C2", "env1_B_C1", "env1_B_C2"), getIds(WorkspaceUtil.getContainerInstances(elements)));
        assertArrayEqualsUnsorted(asList("A_C1", "A_C2", "B_C1", "B_C2"), getIds(WorkspaceUtil.getContainers(elements)));
    }

    @Test
    void given_scopeSoftwareSystem_thenAllConnectedSoftwareSystemInstancesAreIncluded() {
        var ws = load(b() //

                .softwareSystem("A") //
                .container("A", "A1") //
                .softwareSystem("B") //
                .container("B", "B1") //
                .softwareSystem("C") //
                .container("C", "C1") //

                .relation("A1", "B1") //

                .deploymentEnvironment("env1") //
                .deploymentNode("env1", "node_1") //
                .containerInstance("node_1", "env1_A", "A1") //
                .containerInstance("node_1", "env1_B", "B1") //
                .containerInstance("node_1", "env1_C", "C1") //

                .deploymentDiagram(scoped("diagram", "env1", "A")) //
        );
        var scope = scopeBuilder(ws, "diagram").computeScope();
        var elements = scope.getElements();
        var relations = scope.getRelations();

        assertArrayEqualsUnsorted(asList("env1_A", "env1_B"), getIds(WorkspaceUtil.getContainerInstances(elements)));
        assertArrayEqualsUnsorted(asList("A1", "B1"), getIds(WorkspaceUtil.getContainers(elements)));
    }

    @Test
    void given_deploymentNodeWithoutChildrenInScope_thenDeploymentNodeIsExcluded() {
        var ws = load(b() //
                .softwareSystem("A") //

                .deploymentEnvironment("env1") //
                .deploymentNode("env1", "node_1") //

                .deploymentDiagram(scoped("diagram", "env1", "A")) //
        );
        var scope = scopeBuilder(ws, "diagram").computeScope();
        var elements = scope.getElements();
        var relations = scope.getRelations();

        assertArrayEqualsUnsorted(emptyList(), getIds(WorkspaceUtil.getDeploymentNodes(elements)));
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

                .deploymentDiagram(scoped("diagram", "env1", "A")) //
        );
        var scope = scopeBuilder(ws, "diagram").computeScope();
        var elements = scope.getElements();
        var relations = scope.getRelations();

        assertArrayEqualsUnsorted(List.of("infra1"), getIds(WorkspaceUtil.getInfrastructureNodes(elements)));
    }

    @Test
    void given_containerIsOutOfScope_thenRelationIsExcluded() {
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

                .deploymentDiagram(scoped("diagram", "env1", "A")) //
        );
        var scope = scopeBuilder(ws, "diagram").computeScope();
        var elements = scope.getElements();
        var relations = scope.getRelations();

        assertArrayEqualsUnsorted(asList("node_1_A_C", "node_1_B_C"), getIds(WorkspaceUtil.getContainerInstances(elements)));
        assertArrayEqualsUnsorted(List.of("Container (A_C) => Container (B_C)"), stringifyRelations(relations));
    }

    @Test
    void given_softwareSystemIsOutOfScope_thenRelationIsExcluded() {
        var ws = load(b() //

                .softwareSystem("A") //
                .container("A", "A1") //
                .softwareSystem("B") //
                .container("B", "B1") //
                .softwareSystem("C") //
                .container("C", "C1") //

                .deploymentEnvironment("env1") //
                .deploymentNode("env1", "node_1") //

                .containerInstance("node_1", "node_1_A", "A1") //
                .containerInstance("node_1", "node_1_B", "B1") //
                .containerInstance("node_1", "node_1_C", "C1") //

                .relation("A1", "B1") //
                .relation("B1", "C1") //

                .deploymentDiagram(scoped("diagram", "env1", "A")) //
        );
        var scope = scopeBuilder(ws, "diagram").computeScope();
        var elements = scope.getElements();
        var relations = scope.getRelations();

        assertArrayEqualsUnsorted(asList("A1", "B1"), getIds(WorkspaceUtil.getContainers(elements)));
        assertArrayEqualsUnsorted(asList("node_1_A", "node_1_B"), getIds(WorkspaceUtil.getContainerInstances(elements)));
        assertArrayEqualsUnsorted(List.of("Container (A1) => Container (B1)"), stringifyRelations(relations));

        // relazioni tra elementi di cui uno NON in scope NON sono mantenute
        // relazioni implicite tra elementi in scope sono mantenute
    }

    static Element getElementById(Workspace ws, String id) {
        return ws.getModel().getElementById(id);
    }

    static DeploymentDiagramScopeBuilder scopeBuilder(Workspace ws, String diagramName) {
        var diagram = ws.getDiagrams().getByName(diagramName);
        if (diagram instanceof DeploymentDiagram) {
            var d = (DeploymentDiagram) diagram;
            return new DeploymentDiagramSoftwareSystemScopeBuilder(d);
        }
        throw new IllegalArgumentException("Not a DeploymentDiagram: " + diagram);
    }

    static List<String> getIds(List<? extends Element> elements) {
        return elements.stream().map(Element::getId).collect(Collectors.toList());
    }
}
