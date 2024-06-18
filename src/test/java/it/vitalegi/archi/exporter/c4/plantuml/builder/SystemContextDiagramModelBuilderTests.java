package it.vitalegi.archi.exporter.c4.plantuml.builder;

import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.Diagram;
import it.vitalegi.archi.model.diagram.options.DiagramOptions;
import it.vitalegi.archi.model.diagram.SystemContextDiagram;
import it.vitalegi.archi.util.C4DiagramModelUtil;
import it.vitalegi.archi.workspaceloader.model.SystemContextDiagramRaw;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static it.vitalegi.archi.util.WorkspaceTestUtil.b;
import static it.vitalegi.archi.util.WorkspaceTestUtil.load;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class SystemContextDiagramModelBuilderTests {

    static SystemContextDiagramModelBuilder builder(Workspace ws, Diagram diagram) {
        return new SystemContextDiagramModelBuilder(ws, (SystemContextDiagram) diagram);
    }

    @Test
    void given_defaultConfiguration_NoContainers_then_TargetSoftwareSystemShouldBeInTheModel() {
        var ws = load(b() //
                .softwareSystem("A") //
                .systemContextDiagram("diagram", "A", "title") //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);


        assertNotNull(model.findByAliasesPath("SoftwareSystem_A"));
        assertEquals(1, model.countElements());
        assertEquals(0, model.countRelations());
    }

    @Test
    void given_personNotConnectedToTargetSoftwareSystem_then_PersonShouldNotBeInTheModel() {
        var ws = load(b() //
                .person("A") //
                .softwareSystem("S") //
                .systemContextDiagram("diagram", "S", "title") //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);

        assertNull(model.findByAliasesPath("Person_A"), "Person not connected to anything in scope");
        assertEquals(1, model.countElements());
        assertEquals(0, model.countRelations());
    }

    @Test
    void given_groupWithoutSoftwareSystemsOrContainers_then_shouldNotBeInTheModel() {
        var ws = load(b() //
                .group("A") //
                .softwareSystem("S") //
                .systemContextDiagram("diagram", "S", "title") //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);

        assertNull(model.findByAliasesPath("Group_A"));
        assertNotNull(model.findByAliasesPath("SoftwareSystem_S"));
        assertEquals(1, model.countElements());
        assertEquals(0, model.countRelations());

    }

    @Test
    void given_containerInTargetSoftwareSystem_then_ShouldBeInTheModel() {
        var ws = load(b() //
                .softwareSystem("A") //
                .container("A", "B") //
                .systemContextDiagram("diagram", "A", "title") //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);

        assertNotNull(model.findByAliasesPath("SoftwareSystem_A"));
        assertNotNull(model.findByAliasesPath("SoftwareSystem_A", "Container_B"));
        assertEquals(2, model.countElements());
        assertEquals(0, model.countRelations());
    }

    @Test
    void given_defaultConfiguration_then_AllContainersOfTargetSoftwareSystemShouldBeInTheModel() {
        var ws = load(b() //
                .softwareSystem("A") //
                .container("A", "C1") //
                .container("A", "C2") //
                .softwareSystem("B") //

                .systemContextDiagram("diagram", "A") //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);

        assertNotNull(model.findByAliasesPath("SoftwareSystem_A"));
        assertNotNull(model.findByAliasesPath("SoftwareSystem_A", "Container_C1"));
        assertNotNull(model.findByAliasesPath("SoftwareSystem_A", "Container_C2"));
        assertEquals(3, model.countElements());
        assertEquals(0, model.countRelations());
    }

    @Test
    void then_peopleNotConnectedToTargetShouldNotBeInTheModel() {
        var ws = load(b() //
                .person("A") //
                .softwareSystem("C") //

                .systemContextDiagram("diagram", "C") //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);

        assertNotNull(model.findByAliasesPath("SoftwareSystem_C"));
        assertNull(model.findByAliasesPath("Person_A"));
        assertEquals(1, model.countElements());
        assertEquals(0, model.countRelations());
    }

    @Test
    void then_AllGroupsThatContainAContainerInScopeShouldBeInTheModel() {
        var ws = load(b() //
                .group("g") //
                .softwareSystem("g", "A") //
                .container("A", "C1") //

                .systemContextDiagram("diagram", "A") //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);

        assertNotNull(model.findByAliasesPath("Group_g"));
        assertNotNull(model.findByAliasesPath("Group_g", "SoftwareSystem_A"));
        assertNotNull(model.findByAliasesPath("Group_g", "SoftwareSystem_A", "Container_C1"));
        assertEquals(3, model.countElements());
        assertEquals(0, model.countRelations());
    }

    @Test
    void then_AllGroupsThatDontContainAContainerInTargetShouldNotBeInTheModel() {
        var ws = load(b() //
                .softwareSystem("A") //
                .group("g")

                .systemContextDiagram("diagram", "A") //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);

        assertNotNull(model.findByAliasesPath("SoftwareSystem_A"));
        assertNull(model.findByAliasesPath("SoftwareSystem_A", "Group_g"));
        assertEquals(1, model.countElements());
        assertEquals(0, model.countRelations());
    }

    @Test
    void given_defaultConfiguration_then_AllDirectRelationsShouldBeInTheModel() {
        var ws = load(b() //
                .softwareSystem("A") //
                .container("A", "C1") //
                .softwareSystem("B") //
                .person("P") //

                .relation("C1", "B") //
                .relation("P", "C1") //

                .systemContextDiagram("diagram", "A") //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);

        assertNotNull(model.findByAliasesPath("SoftwareSystem_A"));
        assertNotNull(model.findByAliasesPath("SoftwareSystem_A", "Container_C1"));
        assertNotNull(model.findByAliasesPath("SoftwareSystem_B"));
        assertNotNull(model.findByAliasesPath("Person_P"));
        assertEquals(1, model.findRelations("Container_C1", "SoftwareSystem_B").size());
        assertEquals(1, model.findRelations("Person_P", "Container_C1").size());
        assertEquals(4, model.countElements());
        assertEquals(2, model.countRelations());
    }

    @Test
    void then_AllRelationsToAndFromTargetSoftwareSystemShouldNotBeInTheModel() {
        var ws = load(b() //
                .softwareSystem("A") //
                .container("A", "C1") //
                .softwareSystem("B") //
                .person("P") //

                .relation("A", "P") //
                .relation("P", "A") //
                .relation("A", "B") //
                .relation("B", "A") //

                .relation("P", "C1") //

                .systemContextDiagram("diagram", "A") //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);

        assertNotNull(model.findByAliasesPath("SoftwareSystem_A"));
        assertNotNull(model.findByAliasesPath("SoftwareSystem_A", "Container_C1"));
        assertNotNull(model.findByAliasesPath("SoftwareSystem_B"));
        assertNotNull(model.findByAliasesPath("Person_P"));
        assertEquals(0, model.findRelations("SoftwareSystem_A", "Person_P").size());
        assertEquals(0, model.findRelations("Person_P", "SoftwareSystem_A").size());
        assertEquals(0, model.findRelations("SoftwareSystem_A", "SoftwareSystem_B").size());
        assertEquals(0, model.findRelations("SoftwareSystem_B", "SoftwareSystem_A").size());
        assertEquals(1, model.findRelations("Person_P", "Container_C1").size());
        assertEquals(4, model.countElements());
        assertEquals(1, model.countRelations());
    }

    @Test
    void given_inheritedRelationsEnabled_then_AllInheritedRelationsShouldBeInTheModel() {
        var ws = load(b() //
                .softwareSystem("A") //
                .container("A", "C1") //
                .softwareSystem("B") //
                .container("B", "C2") //

                .relation("C1", "C2") //

                .systemContextDiagram(SystemContextDiagramRaw.builder().name("diagram").target("A") //
                        .options(DiagramOptions.builder().inheritRelations(true).build()) //
                ) //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);

        assertNotNull(model.findByAliasesPath("SoftwareSystem_A"));
        assertNotNull(model.findByAliasesPath("SoftwareSystem_A", "Container_C1"));
        assertNotNull(model.findByAliasesPath("SoftwareSystem_B"));
        assertNull(model.findByAliasesPath("SoftwareSystem_B", "Container_C2"));

        assertEquals(0, model.findRelations("Container_C1", "Container_C2").size());
        assertEquals(1, model.findRelations("Container_C1", "SoftwareSystem_B").size());
        assertEquals(0, model.findRelations("SoftwareSystem_A", "Container_C2").size());
        assertEquals(0, model.findRelations("SoftwareSystem_A", "SoftwareSystem_B").size());
        assertEquals(3, model.countElements());
        assertEquals(1, model.countRelations());
    }
}
