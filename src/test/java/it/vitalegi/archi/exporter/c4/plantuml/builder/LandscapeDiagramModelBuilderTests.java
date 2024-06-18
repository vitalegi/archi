package it.vitalegi.archi.exporter.c4.plantuml.builder;

import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.Diagram;
import it.vitalegi.archi.model.diagram.DiagramOptions;
import it.vitalegi.archi.model.diagram.LandscapeDiagram;
import it.vitalegi.archi.util.C4DiagramModelUtil;
import it.vitalegi.archi.workspaceloader.model.LandscapeDiagramRaw;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static it.vitalegi.archi.util.WorkspaceTestUtil.b;
import static it.vitalegi.archi.util.WorkspaceTestUtil.load;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class LandscapeDiagramModelBuilderTests {

    static LandscapeDiagramModelBuilder builder(Workspace ws, Diagram diagram) {
        return new LandscapeDiagramModelBuilder(ws, (LandscapeDiagram) diagram);
    }

    @Test
    void given_softwareSystem_then_shouldBeInTheModel() {
        var ws = load(b() //
                .softwareSystem("A") //
                .landscapeDiagram("diagram", "title") //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);
        assertNotNull(model.findByAliasesPath("SoftwareSystem_A"));
        assertEquals(1, model.countElements());
        assertEquals(0, model.countRelations());
    }

    @Test
    void given_person_then_shouldBeInTheModel() {
        var ws = load(b() //
                .person("A") //
                .landscapeDiagram("diagram", "title") //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);
        assertNotNull(model.findByAliasesPath("Person_A"));
        assertEquals(1, model.countElements());
        assertEquals(0, model.countRelations());
    }

    @Test
    void given_groupWithoutSoftwareSystems_then_shouldNotBeInTheModel() {
        var ws = load(b() //
                .group("A") //
                .landscapeDiagram("diagram", "title") //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);
        assertEquals(0, model.countElements());
        assertEquals(0, model.countRelations());
    }

    @Test
    void given_container_then_shouldNotBeInTheModel() {
        var ws = load(b() //
                .softwareSystem("A") //
                .container("A", "B") //
                .landscapeDiagram("diagram", "title") //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);
        assertNotNull(model.findByAliasesPath("SoftwareSystem_A"));
        assertEquals(1, model.countElements());
        assertEquals(0, model.countRelations());
    }

    @Test
    void given_defaultConfiguration_then_AllSoftwareSystemsAreInTheModel() {
        var ws = load(b() //
                .softwareSystem("A") //
                .softwareSystem("B") //

                .landscapeDiagram("diagram") //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);
        assertNotNull(model.findByAliasesPath("SoftwareSystem_A"));
        assertNotNull(model.findByAliasesPath("SoftwareSystem_B"));
        assertEquals(2, model.countElements());
        assertEquals(0, model.countRelations());
    }

    @Test
    void given_defaultConfiguration_then_AllPeopleAreInTheModel() {
        var ws = load(b() //
                .person("A") //
                .person("B") //

                .landscapeDiagram("diagram") //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);
        assertNotNull(model.findByAliasesPath("Person_A"));
        assertNotNull(model.findByAliasesPath("Person_B"));
        assertEquals(2, model.countElements());
        assertEquals(0, model.countRelations());
    }

    @Test
    void given_defaultConfiguration_then_AllGroupsThatContainSoftwareSystemsAreInTheModel() {
        var ws = load(b() //
                .group("g") //
                .softwareSystem("g", "A") //
                .softwareSystem("B") //

                .landscapeDiagram("diagram") //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);
        assertNotNull(model.findByAliasesPath("Group_g", "SoftwareSystem_A"));
        assertNotNull(model.findByAliasesPath("SoftwareSystem_B"));
        assertNotNull(model.findByAliasesPath("Group_g"));
        assertEquals(3, model.countElements());
        assertEquals(0, model.countRelations());
    }

    @Test
    void given_defaultConfiguration_then_AllDirectRelationsAreInTheModel() {
        var ws = load(b() //
                .softwareSystem("A") //
                .softwareSystem("B") //
                .person("C") //
                .relation("A", "B") //
                .relation("A", "C") //

                .landscapeDiagram("diagram") //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);

        assertNotNull(model.findByAliasesPath("SoftwareSystem_A"));
        assertNotNull(model.findByAliasesPath("SoftwareSystem_B"));
        assertNotNull(model.findByAliasesPath("Person_C"));
        assertEquals(1, model.findRelations("SoftwareSystem_A", "SoftwareSystem_B").size());
        assertEquals(1, model.findRelations("SoftwareSystem_A", "Person_C").size());
        assertEquals(3, model.countElements());
        assertEquals(2, model.countRelations());
    }

    @Test
    void given_defaultConfiguration_then_AllInheritedRelationsAreNotInTheModel() {
        var ws = load(b() //
                .softwareSystem("A") //
                .container("A", "A1") //
                .softwareSystem("B") //
                .relation("A1", "B") //

                .landscapeDiagram("diagram") //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);

        assertNotNull(model.findByAliasesPath("SoftwareSystem_A"));
        assertNotNull(model.findByAliasesPath("SoftwareSystem_B"));
        assertEquals(2, model.countElements());
        assertEquals(0, model.countRelations());
    }

    @Test
    void given_implicitRelationsEnabled_then_implicitRelationsAreInTheModel() {
        var ws = load(b() //
                .softwareSystem("A") //
                .container("A", "A1") //
                .softwareSystem("B") //

                .relation("A1", "B") //

                .landscapeDiagram(LandscapeDiagramRaw.builder() //
                        .name("diagram") //
                        .options(DiagramOptions.builder().inheritRelations(true).build()) //
                ) //
        );
        var diagram = ws.getDiagrams().getByName("diagram");
        var actual = builder(ws, diagram).build();
        var model = new C4DiagramModelUtil(actual);

        assertNotNull(model.findByAliasesPath("SoftwareSystem_A"));
        assertNotNull(model.findByAliasesPath("SoftwareSystem_B"));
        assertNotNull(model.findRelations("SoftwareSystem_A", "SoftwareSystem_B"));
        assertEquals(2, model.countElements());
        assertEquals(1, model.countRelations());
    }

}
