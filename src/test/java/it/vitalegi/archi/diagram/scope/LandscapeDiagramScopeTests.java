package it.vitalegi.archi.diagram.scope;

import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.Diagram;
import it.vitalegi.archi.model.diagram.LandscapeDiagram;
import it.vitalegi.archi.model.element.Element;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static it.vitalegi.archi.util.WorkspaceTestUtil.b;
import static it.vitalegi.archi.util.WorkspaceTestUtil.load;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class LandscapeDiagramScopeTests {

    @Nested
    class IsAllowed {
        @Test
        void given_softwareSystem_then_shouldReturnTrue() {
            var ws = load(b() //
                    .softwareSystem("A") //
                    .landscapeDiagram("diagram", "title") //
            );
            var diagram = (LandscapeDiagram) (ws.getDiagrams().getByName("diagram"));
            var diagramScope = diagramScope(diagram);
            assertTrue(diagramScope.isAllowed(getElementById(ws, "A")));
        }

        @Test
        void given_person_then_shouldReturnTrue() {
            var ws = load(b() //
                    .person("A") //
                    .landscapeDiagram("diagram", "title") //
            );
            var diagram = (LandscapeDiagram) (ws.getDiagrams().getByName("diagram"));
            var diagramScope = diagramScope(diagram);
            assertTrue(diagramScope.isAllowed(getElementById(ws, "A")));
        }

        @Test
        void given_group_then_shouldReturnTrue() {
            var ws = load(b() //
                    .group("A") //
                    .landscapeDiagram("diagram", "title") //
            );
            var diagram = (LandscapeDiagram) (ws.getDiagrams().getByName("diagram"));
            var diagramScope = diagramScope(diagram);
            assertTrue(diagramScope.isAllowed(getElementById(ws, "A")));
        }

        @Test
        void given_container_then_shouldReturnFalse() {
            var ws = load(b() //
                    .softwareSystem("A") //
                    .container("A", "B") //
                    .landscapeDiagram("diagram", "title") //
            );
            var diagram = (LandscapeDiagram) (ws.getDiagrams().getByName("diagram"));
            var diagramScope = diagramScope(diagram);
            assertFalse(diagramScope.isAllowed(getElementById(ws, "B")));
        }
    }

    @Nested
    class ComputeScope {
        @Test
        void given_defaultConfiguration_then_AllSoftwareSystemsAreIncluded() {
            var ws = load(b() //
                    .softwareSystem("A") //
                    .softwareSystem("B") //

                    .landscapeDiagram("diagram") //
            );
            var diagram = ws.getDiagrams().getByName("diagram");
            var diagramScope = diagramScope(diagram);
            var scope = diagramScope.computeScope();
            assertTrue(scope.isInScope(getElementById(ws, "A")));
            assertTrue(scope.isInScope(getElementById(ws, "B")));
        }

        @Test
        void given_defaultConfiguration_then_AllPeopleAreIncluded() {
            var ws = load(b() //
                    .person("A") //
                    .person("B") //

                    .landscapeDiagram("diagram") //
            );
            var diagram = ws.getDiagrams().getByName("diagram");
            var diagramScope = diagramScope(diagram);
            var scope = diagramScope.computeScope();
            assertTrue(scope.isInScope(getElementById(ws, "A")));
            assertTrue(scope.isInScope(getElementById(ws, "B")));
        }

        @Test
        void given_defaultConfiguration_then_AllGroupsThatContainASoftwareSystemAreIncluded() {
            var ws = load(b() //
                    .group("g")
                    .softwareSystem("g", "A") //
                    .softwareSystem("B") //

                    .landscapeDiagram("diagram") //
            );
            var diagram = ws.getDiagrams().getByName("diagram");
            var diagramScope = diagramScope(diagram);
            var scope = diagramScope.computeScope();
            assertTrue(scope.isInScope(getElementById(ws, "g")));
        }

        @Test
        void given_defaultConfiguration_then_AllGroupsThatDontContainASoftwareSystemAreExcluded() {
            var ws = load(b() //
                    .softwareSystem("A") //
                    .group("g")

                    .landscapeDiagram("diagram") //
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
                    .softwareSystem("B") //
                    .person("C") //
                    .relation("A", "B") //
                    .relation("A", "C") //

                    .landscapeDiagram("diagram") //
            );
            var diagram = ws.getDiagrams().getByName("diagram");
            var diagramScope = diagramScope(diagram);
            var scope = diagramScope.computeScope();
            var a = getElementById(ws, "A");
            var b = getElementById(ws, "B");
            var c = getElementById(ws, "C");

            var r_ab = ws.getModel().getRelations().getRelationsBetween(a, b).get(0);
            var r_ac = ws.getModel().getRelations().getRelationsBetween(a, c).get(0);
            assertTrue(scope.isInScope(r_ab));
            assertTrue(scope.isInScope(r_ac));
        }


        @Test
        void given_defaultConfiguration_then_AllInheritedRelationsAreExcluded() {
            var ws = load(b() //
                    .softwareSystem("A") //
                    .container("A", "A1") //
                    .softwareSystem("B") //
                    .relation("A1", "B") //

                    .landscapeDiagram("diagram") //
            );
            var diagram = ws.getDiagrams().getByName("diagram");
            var diagramScope = diagramScope(diagram);
            var scope = diagramScope.computeScope();
            var a1 = getElementById(ws, "A1");
            var b = getElementById(ws, "B");

            var r = ws.getModel().getRelations().getRelationsBetween(a1, b).get(0);
            assertFalse(scope.isInScope(r));
        }
    }

    static Element getElementById(Workspace ws, String id) {
        return ws.getModel().getElementById(id);
    }

    static LandscapeDiagramScopeBuilder diagramScope(Diagram diagram) {
        return new LandscapeDiagramScopeBuilder((LandscapeDiagram) diagram);
    }

}
