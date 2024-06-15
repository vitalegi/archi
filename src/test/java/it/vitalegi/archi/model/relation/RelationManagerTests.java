package it.vitalegi.archi.model.relation;


import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.workspaceloader.model.ElementRaw;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static it.vitalegi.archi.util.AssertionUtil.assertArrayEqualsUnsorted;
import static it.vitalegi.archi.util.WorkspaceTestUtil.b;
import static it.vitalegi.archi.util.WorkspaceTestUtil.load;
import static it.vitalegi.archi.util.WorkspaceTestUtil.stringifyRelations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class RelationManagerTests {
    @Test
    void given_addRelation_then_relationShouldBeRetrievable() {
        var ws = load(b() //
                .softwareSystem("A") //
                .softwareSystem("B") //
        );
        var relations = ws.getModel().getRelationManager();
        relations.addRelation(relation(ws, "A", "B"));
        assertEquals(1, relations.getDirect().getAll().size());
    }

    @Test
    void given_addRelation_when_containers_then_shouldCreateImplicitRelations() {
        var ws = load(b() //
                .softwareSystem("A") //
                .softwareSystem("B") //

                .container("A", "C1") //
                .container("B", "C2") //
        );
        var relations = ws.getModel().getRelationManager();
        relations.addRelation(relation(ws, "C1", "C2"));
        assertEquals(1, relations.getDirect().getAll().size());
        assertArrayEqualsUnsorted(List.of("SoftwareSystem (A) => SoftwareSystem (B)", "SoftwareSystem (A) => Container (C2)"), strigifyImplicits(ws, "A"));
        assertArrayEqualsUnsorted(List.of("SoftwareSystem (A) => SoftwareSystem (B)", "Container (C1) => SoftwareSystem (B)"), strigifyImplicits(ws, "B"));
        assertArrayEqualsUnsorted(List.of("Container (C1) => Container (C2)", "Container (C1) => SoftwareSystem (B)"), strigifyImplicits(ws, "C1"));
        assertArrayEqualsUnsorted(List.of("Container (C1) => Container (C2)", "SoftwareSystem (A) => Container (C2)"), strigifyImplicits(ws, "C2"));
    }

    @Test
    void given_addRelation_when_components_then_shouldCreateImplicitRelations() {
        var ws = load(b() //
                .softwareSystem("A") //
                .softwareSystem("B") //

                .container("A", "C1") //
                .container("B", "C2") //

                .component(ElementRaw.builder().parentId("C1").id("comp1"))
                .component(ElementRaw.builder().parentId("C2").id("comp2")) //
        );
        var relations = ws.getModel().getRelationManager();
        relations.addRelation(relation(ws, "comp1", "comp2"));
        assertEquals(1, relations.getDirect().getAll().size());
        assertArrayEqualsUnsorted(List.of("SoftwareSystem (A) => Component (comp2)", "SoftwareSystem (A) => Container (C2)", "SoftwareSystem (A) => SoftwareSystem (B)"), strigifyImplicits(ws, "A"));
        assertArrayEqualsUnsorted(List.of("Container (C1) => Component (comp2)", "Container (C1) => Container (C2)", "Container (C1) => SoftwareSystem (B)"), strigifyImplicits(ws, "C1"));
        assertArrayEqualsUnsorted(List.of("Component (comp1) => Component (comp2)", "Component (comp1) => Container (C2)", "Component (comp1) => SoftwareSystem (B)"), strigifyImplicits(ws, "comp1"));

        assertArrayEqualsUnsorted(List.of("Component (comp1) => SoftwareSystem (B)", "Container (C1) => SoftwareSystem (B)", "SoftwareSystem (A) => SoftwareSystem (B)"), strigifyImplicits(ws, "B"));
        assertArrayEqualsUnsorted(List.of("Component (comp1) => Container (C2)", "Container (C1) => Container (C2)", "SoftwareSystem (A) => Container (C2)"), strigifyImplicits(ws, "C2"));
        assertArrayEqualsUnsorted(List.of("Component (comp1) => Component (comp2)", "Container (C1) => Component (comp2)", "SoftwareSystem (A) => Component (comp2)"), strigifyImplicits(ws, "comp2"));
    }

    static List<String> strigifyImplicits(Workspace ws, String id) {
        var relations = ws.getModel().getRelationManager().getImplicit();
        var e = getElementById(ws, id);
        return stringifyRelations(relations.getRelations(e));
    }

    static DirectRelation relation(Workspace ws, String from, String to) {
        return DirectRelation.builder().from(getElementById(ws, from)).to(getElementById(ws, to)).build();
    }

    static Element getElementById(Workspace ws, String id) {
        return ws.getModel().getElementById(id);
    }
}
