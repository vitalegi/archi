package it.vitalegi.archi.exporter.c4.plantuml.builder;

import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.DeploymentDiagram;
import it.vitalegi.archi.model.diagram.Diagram;
import it.vitalegi.archi.model.diagram.options.DiagramOptions;
import it.vitalegi.archi.util.C4DiagramModelUtil;
import it.vitalegi.archi.workspaceloader.model.DeploymentDiagramRaw;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static it.vitalegi.archi.util.WorkspaceTestUtil.b;
import static it.vitalegi.archi.util.WorkspaceTestUtil.load;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class DeploymentDiagramModelBuilderTests {

    static DeploymentDiagramModelBuilder builder(Workspace ws, Diagram diagram) {
        return new DeploymentDiagramModelBuilder(ws, (DeploymentDiagram) diagram);
    }

    @Test
    void given_emptyEnvironment_then_modelShouldBeEmpty() {
        var ws = load(b() //
                .deploymentEnvironment("env") //
                .deploymentDiagram(DeploymentDiagramRaw.all("diagram", "env")) //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);

        assertEquals(0, model.countElements());
        assertEquals(0, model.countRelations());
    }

    @Test
    void given_containerInstance_then_containerShouldBeInTheModel() {
        var ws = load(b() //
                .softwareSystem("A") //
                .container("A", "C1") //

                .deploymentEnvironment("env") //
                .deploymentNode("env", "d1") //
                .containerInstance("d1", "ci1", "C1") //

                .deploymentDiagram(DeploymentDiagramRaw.all("diagram", "env")) //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);

        assertNotNull(model.findByAliasesPath("DeploymentNode_d1"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "ContainerInstance_ci1"), "ContainerInstance should be in the model");
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "ContainerInstance_ci1", "Container_C1"), "Container should be in the model");
        assertEquals(3, model.countElements());
        assertEquals(0, model.countRelations());
    }

    @Test
    void given_softwareSystemInstance_then_softwareSystemShouldBeInTheModel() {
        var ws = load(b() //
                .softwareSystem("A") //

                .deploymentEnvironment("env") //
                .deploymentNode("env", "d1") //
                .softwareSystemInstance("d1", "si1", "A") //

                .deploymentDiagram(DeploymentDiagramRaw.all("diagram", "env")) //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);

        assertNotNull(model.findByAliasesPath("DeploymentNode_d1"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "SoftwareSystemInstance_si1"), "SoftwareSystemInstance should be in the model");
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "SoftwareSystemInstance_si1", "SoftwareSystem_A"), "SoftwareSystem should be in the model");
        assertEquals(3, model.countElements());
        assertEquals(0, model.countRelations());
    }

    @Test
    void given_scoped_then_onlyConnectedContainersShouldBeInTheModel() {
        var ws = load(b() //
                .softwareSystem("A") //
                .container("A", "C1") //

                .softwareSystem("B") //
                .container("B", "C2") //
                .container("B", "C3") //

                .relation("C1", "C2") //

                .deploymentEnvironment("env") //
                .deploymentNode("env", "d1") //
                .containerInstance("d1", "ci1", "C1") //
                .containerInstance("d1", "ci2", "C2") //
                .containerInstance("d1", "ci3", "C3") //

                .deploymentDiagram(DeploymentDiagramRaw.scoped("diagram", "env", "A")) //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);

        assertNotNull(model.findByAliasesPath("DeploymentNode_d1"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "ContainerInstance_ci1"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "ContainerInstance_ci1", "Container_C1"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "ContainerInstance_ci2"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "ContainerInstance_ci2", "Container_C2"));
        assertEquals(1, model.findRelations("Container_C1", "Container_C2").size());
        assertEquals(5, model.countElements());
        assertEquals(1, model.countRelations());
    }

    @Test
    void given_scoped_then_onlyConnectedSoftwareSystemsShouldBeInTheModel() {
        var ws = load(b() //
                .softwareSystem("A") //
                .container("A", "C1") //

                .softwareSystem("B") //
                .softwareSystem("C") //

                .relation("A", "B") //

                .deploymentEnvironment("env") //
                .deploymentNode("env", "d1") //
                .softwareSystemInstance("d1", "si1", "A") //
                .softwareSystemInstance("d1", "si2", "B") //
                .softwareSystemInstance("d1", "si3", "C") //

                .deploymentDiagram(DeploymentDiagramRaw.scoped("diagram", "env", "A")) //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);

        assertNotNull(model.findByAliasesPath("DeploymentNode_d1"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "SoftwareSystemInstance_si1"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "SoftwareSystemInstance_si1", "SoftwareSystem_A"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "SoftwareSystemInstance_si2"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "SoftwareSystemInstance_si2", "SoftwareSystem_B"));
        assertEquals(1, model.findRelations("SoftwareSystem_A", "SoftwareSystem_B").size());
        assertEquals(5, model.countElements());
        assertEquals(1, model.countRelations());
    }

    @Test
    void given_multipleContainerInstances_then_AllShouldBeInTheModel() {
        var ws = load(b() //
                .softwareSystem("A") //
                .container("A", "C1") //

                .deploymentEnvironment("env") //
                .deploymentNode("env", "d1") //
                .containerInstance("d1", "ci1", "C1") //
                .containerInstance("d1", "ci2", "C1") //

                .deploymentDiagram(DeploymentDiagramRaw.all("diagram", "env")) //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);

        assertNotNull(model.findByAliasesPath("DeploymentNode_d1"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "ContainerInstance_ci1"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "ContainerInstance_ci1", "Container_C1"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "ContainerInstance_ci2"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "ContainerInstance_ci2", "Container_C1_1"), "Container should be in the model, with a dedicated alias");
        assertEquals(5, model.countElements());
        assertEquals(0, model.countRelations());
    }

    @Test
    void given_multipleSoftwareSystemInstance_then_allShouldBeInTheModel() {
        var ws = load(b() //
                .softwareSystem("A") //

                .deploymentEnvironment("env") //
                .deploymentNode("env", "d1") //
                .softwareSystemInstance("d1", "si1", "A") //
                .softwareSystemInstance("d1", "si2", "A") //

                .deploymentDiagram(DeploymentDiagramRaw.all("diagram", "env")) //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);

        assertNotNull(model.findByAliasesPath("DeploymentNode_d1"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "SoftwareSystemInstance_si1"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "SoftwareSystemInstance_si1", "SoftwareSystem_A"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "SoftwareSystemInstance_si2"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "SoftwareSystemInstance_si2", "SoftwareSystem_A_1"));
        assertEquals(5, model.countElements());
        assertEquals(0, model.countRelations());
    }

    @Test
    void given_multipleEnvironments_then_onlyElementsFromTargetEnvShouldBeInTheModel() {
        var ws = load(b() //
                .softwareSystem("A") //
                .container("A", "C1") //
                .container("A", "C2") //

                .deploymentEnvironment("env") //
                .deploymentNode("env", "d1") //
                .containerInstance("d1", "ci1", "C1") //

                .deploymentEnvironment("env2") //
                .deploymentNode("env2", "d2") //
                .containerInstance("d2", "ci2", "C2") //
                .softwareSystemInstance("d2", "si2", "A") //

                .deploymentDiagram(DeploymentDiagramRaw.all("diagram", "env")) //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);

        assertNotNull(model.findByAliasesPath("DeploymentNode_d1"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "ContainerInstance_ci1"), "ContainerInstance should be in the model");
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "ContainerInstance_ci1", "Container_C1"), "Container should be in the model");
        assertEquals(3, model.countElements());
        assertEquals(0, model.countRelations());
    }

    @Test
    void given_containersInScope_then_relationShouldBeInTheModel() {
        var ws = load(b() //
                .softwareSystem("A") //
                .container("A", "C1") //

                .softwareSystem("B") //
                .container("B", "C2") //

                .relation("C1", "C2") //

                .deploymentEnvironment("env") //
                .deploymentNode("env", "d1")
                .containerInstance("d1", "ci1", "C1") //
                .containerInstance("d1", "ci2", "C2") //

                .deploymentDiagram(DeploymentDiagramRaw.all("diagram", "env")) //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);

        assertNotNull(model.findByAliasesPath("DeploymentNode_d1"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "ContainerInstance_ci1"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "ContainerInstance_ci1", "Container_C1"));

        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "ContainerInstance_ci2"), "ContainerInstance should be in the model");
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "ContainerInstance_ci2", "Container_C2"), "Container should be in the model");

        assertEquals(1, model.findRelations("Container_C1", "Container_C2").size());
        assertEquals(5, model.countElements());
        assertEquals(1, model.countRelations());
    }

    @Test
    void given_softwareSystemsInScope_then_relationShouldBeInTheModel() {
        var ws = load(b() //
                .softwareSystem("A") //
                .softwareSystem("B") //

                .relation("A", "B") //

                .deploymentEnvironment("env") //
                .deploymentNode("env", "d1") //
                .softwareSystemInstance("d1", "si1", "A") //
                .softwareSystemInstance("d1", "si2", "B") //

                .deploymentDiagram(DeploymentDiagramRaw.all("diagram", "env")) //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);

        assertNotNull(model.findByAliasesPath("DeploymentNode_d1"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "SoftwareSystemInstance_si1"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "SoftwareSystemInstance_si1", "SoftwareSystem_A"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "SoftwareSystemInstance_si2"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "SoftwareSystemInstance_si2", "SoftwareSystem_B"));

        assertEquals(1, model.findRelations("SoftwareSystem_A", "SoftwareSystem_B").size());
        assertEquals(5, model.countElements());
        assertEquals(1, model.countRelations());
    }

    @Test
    void given_inheritedRelationsEnabled_then_relationShouldBeInTheModel() {
        var ws = load(b() //
                .softwareSystem("A") //
                .container("A", "C1") //

                .softwareSystem("B") //
                .container("B", "C2") //

                .relation("C1", "C2") //

                .deploymentEnvironment("env") //
                .deploymentNode("env", "d1") //
                .softwareSystemInstance("d1", "si1", "A") //
                .softwareSystemInstance("d1", "si2", "B") //

                .deploymentDiagram(DeploymentDiagramRaw.all("diagram", "env") //
                        .options(DiagramOptions.builder().inheritRelations(true).build()) //
                ) //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);

        assertNotNull(model.findByAliasesPath("DeploymentNode_d1"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "SoftwareSystemInstance_si1"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "SoftwareSystemInstance_si1", "SoftwareSystem_A"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "SoftwareSystemInstance_si2"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "SoftwareSystemInstance_si2", "SoftwareSystem_B"));

        assertEquals(1, model.findRelations("SoftwareSystem_A", "SoftwareSystem_B").size());
        assertEquals(5, model.countElements());
        assertEquals(1, model.countRelations());
    }

    @Test
    void given_inheritedRelationsDisabled_then_relationShouldNotBeInTheModel() {
        var ws = load(b() //
                .softwareSystem("A") //
                .container("A", "C1") //

                .softwareSystem("B") //
                .container("B", "C2") //

                .relation("C1", "C2") //

                .deploymentEnvironment("env") //
                .deploymentNode("env", "d1") //
                .softwareSystemInstance("d1", "si1", "A") //
                .softwareSystemInstance("d1", "si2", "B") //

                .deploymentDiagram(DeploymentDiagramRaw.all("diagram", "env") //
                        .options(DiagramOptions.builder().inheritRelations(false).build()) //
                ) //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);

        assertNotNull(model.findByAliasesPath("DeploymentNode_d1"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "SoftwareSystemInstance_si1"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "SoftwareSystemInstance_si1", "SoftwareSystem_A"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "SoftwareSystemInstance_si2"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "SoftwareSystemInstance_si2", "SoftwareSystem_B"));

        assertEquals(0, model.findRelations("SoftwareSystem_A", "SoftwareSystem_B").size());
        assertEquals(5, model.countElements());
        assertEquals(0, model.countRelations());
    }

    @Test
    void given_deploymentNodes_then_onlyDeploymentNodesWithChildrenInScopeShouldBeInTheModel() {
        var ws = load(b() //
                .softwareSystem("A") //

                .deploymentEnvironment("env") //
                .deploymentNode("env", "d1") //
                .deploymentNode("d1", "node1") //
                .deploymentNode("d1", "node2") //
                .softwareSystemInstance("node1", "si1", "A") //

                .deploymentDiagram(DeploymentDiagramRaw.all("diagram", "env")) //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);

        assertNotNull(model.findByAliasesPath("DeploymentNode_d1"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "DeploymentNode_node1"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "DeploymentNode_node1", "SoftwareSystemInstance_si1"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "DeploymentNode_node1", "SoftwareSystemInstance_si1", "SoftwareSystem_A"));
        assertEquals(4, model.countElements());
        assertEquals(0, model.countRelations());
    }

    @Test
    void given_infrastructureNodes_then_onlyInfrastructureNodesConnectedToElementsInScopeShouldBeInTheModel() {
        var ws = load(b() //
                .softwareSystem("A") //

                .deploymentEnvironment("env") //
                .deploymentNode("env", "d1") //
                .infrastructureNode("d1", "infra1") //
                .infrastructureNode("d1", "infra2") //
                .softwareSystemInstance("d1", "ss1", "A") //

                .relation("infra1", "ss1") //

                .deploymentDiagram(DeploymentDiagramRaw.all("diagram", "env")) //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);

        assertNotNull(model.findByAliasesPath("DeploymentNode_d1"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "InfrastructureNode_infra1"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "SoftwareSystemInstance_ss1"));
        assertNotNull(model.findByAliasesPath("DeploymentNode_d1", "SoftwareSystemInstance_ss1", "SoftwareSystem_A"));
        assertEquals(1, model.findRelations("InfrastructureNode_infra1", "SoftwareSystemInstance_ss1").size());
        assertEquals(4, model.countElements());
        assertEquals(1, model.countRelations());
    }
}
