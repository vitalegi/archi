package it.vitalegi.archi.diagram.scope;

import it.vitalegi.archi.diagram.DiagramScope;
import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.Diagram;
import it.vitalegi.archi.model.diagram.SystemContextDiagram;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.relation.Relation;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static it.vitalegi.archi.util.WorkspaceTestUtil.b;
import static it.vitalegi.archi.util.WorkspaceTestUtil.load;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class SystemContextScopeTests {

    @Nested
    class IsAllowed {
        @Test
        void given_softwareSystem_then_shouldReturnTrue() {
            var ws = load(b() //
                    .softwareSystem("A") //
                    .systemContextDiagram("diagram", "A", "title") //
            );
            var diagram = (SystemContextDiagram) (ws.getDiagrams().getByName("diagram"));
            var diagramScope = diagramScope(diagram);
            assertTrue(diagramScope.isAllowed(getElementById(ws, "A")));
        }

        @Test
        void given_person_then_shouldReturnTrue() {
            var ws = load(b() //
                    .person("A") //
                    .softwareSystem("S") //
                    .systemContextDiagram("diagram", "S", "title") //
            );
            var diagram = (SystemContextDiagram) (ws.getDiagrams().getByName("diagram"));
            var diagramScope = diagramScope(diagram);
            assertTrue(diagramScope.isAllowed(getElementById(ws, "A")));
        }

        @Test
        void given_group_then_shouldReturnTrue() {
            var ws = load(b() //
                    .group("A") //
                    .softwareSystem("S") //
                    .systemContextDiagram("diagram", "S", "title") //
            );
            var diagram = (SystemContextDiagram) (ws.getDiagrams().getByName("diagram"));
            var diagramScope = diagramScope(diagram);
            assertTrue(diagramScope.isAllowed(getElementById(ws, "A")));
        }

        @Test
        void given_container_then_shouldReturnTrue() {
            var ws = load(b() //
                    .softwareSystem("A") //
                    .container("A", "B") //
                    .systemContextDiagram("diagram", "A", "title") //
            );
            var diagram = (SystemContextDiagram) (ws.getDiagrams().getByName("diagram"));
            var diagramScope = diagramScope(diagram);
            assertTrue(diagramScope.isAllowed(getElementById(ws, "B")));
        }
    }

    @Nested
    class ComputeScope {
        @Test
        void given_defaultConfiguration_then_AllContainersOfTargetSoftwareSystemAreIncluded() {
            var ws = load(b() //
                    .softwareSystem("A") //
                    .container("A", "C1") //
                    .container("A", "C2") //
                    .softwareSystem("B") //

                    .systemContextDiagram("diagram", "A") //
            );
            var diagram = ws.getDiagrams().getByName("diagram");
            var diagramScope = diagramScope(diagram);
            var scope = diagramScope.computeScope();
            assertTrue(scope.isInScope(getElementById(ws, "C1")));
            assertTrue(scope.isInScope(getElementById(ws, "C2")));
        }

        @Test
        void given_defaultConfiguration_NoContainers_then_TargetSoftwareSystemIsIncluded() {
            var ws = load(b() //
                    .softwareSystem("A") //
                    .systemContextDiagram("diagram", "A") //
            );
            var diagram = ws.getDiagrams().getByName("diagram");
            var diagramScope = diagramScope(diagram);
            var scope = diagramScope.computeScope();
            assertTrue(scope.isInScope(getElementById(ws, "A")));
        }

        @Test
        void given_defaultConfiguration_then_PeopleNotConnectedToTargetAreExcluded() {
            var ws = load(b() //
                    .person("A") //
                    .softwareSystem("C") //

                    .systemContextDiagram("diagram", "C") //
            );
            var diagram = ws.getDiagrams().getByName("diagram");
            var diagramScope = diagramScope(diagram);
            var scope = diagramScope.computeScope();
            assertFalse(scope.isInScope(getElementById(ws, "A")));
        }

        @Test
        void given_defaultConfiguration_then_AllGroupsThatContainAContainerInScopeAreIncluded() {
            var ws = load(b() //
                    .group("g") //
                    .softwareSystem("g", "A") //
                    .container("A", "C1") //

                    .systemContextDiagram("diagram", "A") //
            );
            var diagram = ws.getDiagrams().getByName("diagram");
            var diagramScope = diagramScope(diagram);
            var scope = diagramScope.computeScope();
            assertTrue(scope.isInScope(getElementById(ws, "g")));
        }

        @Test
        void given_defaultConfiguration_then_AllGroupsThatDontContainAContainerInTargetAreExcluded() {
            var ws = load(b() //
                    .softwareSystem("A") //
                    .group("g")

                    .systemContextDiagram("diagram", "A") //
            );
            var diagram = ws.getDiagrams().getByName("diagram");
            var diagramScope = diagramScope(diagram);
            var scope = diagramScope.computeScope();
            assertFalse(scope.isInScope(getElementById(ws, "g")));
        }

        @Test
        void given_defaultConfiguration_then_AllDirectRelationsAreIncluded() {
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
            var diagramScope = diagramScope(diagram);
            var scope = diagramScope.computeScope();
            var c1 = getElementById(ws, "C1");
            var b = getElementById(ws, "B");
            var p = getElementById(ws, "P");

            var r1 = ws.getModel().getRelations().getRelationsBetween(c1, b).get(0);
            var r2 = ws.getModel().getRelations().getRelationsBetween(p, c1).get(0);
            assertTrue(scope.isInScope(r1));
            assertTrue(scope.isInScope(r2));
        }


        @Test
        void given_defaultConfiguration_then_AllRelationsToAndFromTargetSoftwareSystemAreExcluded() {
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
            var diagramScope = diagramScope(diagram);
            var scope = diagramScope.computeScope();
            assertTrue(hasRelationsBetween(ws, scope, "P", "C1"));
            assertFalse(hasRelationsBetween(ws, scope, "A", "B"));
            assertFalse(hasRelationsBetween(ws, scope, "A", "P"));
        }

        @Disabled
        @Test
        void given_inheritedRelationsEnabled_then_AllInheritedRelationsAreIncluded() {
            var ws = load(b() //
                    .softwareSystem("A") //
                    .container("A", "C1") //
                    .softwareSystem("B") //
                    .container("B", "C2") //

                    .relation("C1", "C2") //

                    .systemContextDiagram("diagram", "A") //
            );
            var diagram = ws.getDiagrams().getByName("diagram");
            var diagramScope = diagramScope(diagram);
            var scope = diagramScope.computeScope();
            var c1 = getElementById(ws, "C1");
            var c2 = getElementById(ws, "C2");

            var r1 = ws.getModel().getRelations().getRelationsBetween(c1, c2).get(0);
            assertTrue(scope.isInScope(r1));
        }
    }


    static Element getElementById(Workspace ws, String id) {
        return ws.getModel().getElementById(id);
    }

    static List<Relation> getRelationsBetween(Workspace ws, String a, String b) {
        var e1 = getElementById(ws, a);
        var e2 = getElementById(ws, b);
        return ws.getModel().getRelations().getRelationsBetween(e1, e2);
    }

    static boolean hasRelationsBetween(Workspace ws, DiagramScope scope, String a, String b) {
        var relations = getRelationsBetween(ws, a, b);
        if (relations == null) {
            return false;
        }
        for (var r : relations) {
            if (scope.isInScope(r)) {
                return true;
            }
        }
        return false;
    }

    static SystemContextScopeBuilder diagramScope(Diagram diagram) {
        return new SystemContextScopeBuilder((SystemContextDiagram) diagram);
    }
}
