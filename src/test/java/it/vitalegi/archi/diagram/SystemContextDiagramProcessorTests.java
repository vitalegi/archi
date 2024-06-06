package it.vitalegi.archi.diagram;

import it.vitalegi.archi.diagram.dto.SystemContextDiagram;
import it.vitalegi.archi.exception.ElementNotFoundException;
import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.Relation;
import it.vitalegi.archi.util.WorkspaceLoaderBuilder;
import it.vitalegi.archi.workspace.Workspace;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

import static it.vitalegi.archi.util.ModelUtil.defaultBuilder;
import static it.vitalegi.archi.util.ModelUtil.defaultLoader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class SystemContextDiagramProcessorTests {

    SystemContextDiagramProcessor diagramProcessor;

    @BeforeEach
    void init() {
        diagramProcessor = new SystemContextDiagramProcessor();

    }

    @Nested
    class Loader {
        @Test
        void given_correctConfiguration_thenSucceed() {
            var ws = load(b().softwareSystem("B").systemContextDiagram("A", "B", "title"));
            var diagram = ws.getDiagrams().getByName("A");
            assertNotNull(diagram);
            assertEquals(diagram.getClass(), SystemContextDiagram.class);
            var d = (SystemContextDiagram) diagram;
            assertEquals("A", d.getName());
            assertEquals("title", d.getTitle());
            assertEquals("B", d.getTarget());
        }


        @Test
        void given_missingTargetField_thenFail() {
            var e = Assertions.assertThrows(IllegalArgumentException.class, () -> load(b() //
                    .systemContextDiagram("diagram1", "", "123") //
            ));
            assertEquals("Diagram diagram1, missing target.", e.getMessage());
        }

        @Test
        void given_missingTarget_thenFail() {
            var e = Assertions.assertThrows(ElementNotFoundException.class, () -> load(b() //
                    .systemContextDiagram("diagram1", "ss", "123") //
            ));
            assertEquals("Can't find ss: invalid target on Diagram diagram1", e.getMessage());
        }

        @Test
        void given_targetIsNotSoftwareSystem_thenFail() {
            var e = Assertions.assertThrows(IllegalArgumentException.class, () -> load(b() //
                    .systemContextDiagram("diagram1", "c", "123") //
                    .softwareSystem("ss") //
                    .container("ss", "c")));
            assertEquals("Diagram diagram1, invalid target. Expected: SoftwareSystem, Actual: Container (c)", e.getMessage());
        }
    }

    @Nested
    class Accept {
        @Test
        void given_systemContextDiagram_then_shouldReturnTrue() {
            var ws = load(b().systemContextDiagram("diagram1", "A").softwareSystem("A"));
            assertTrue(diagramProcessor.accept(ws.getDiagrams().getByName("diagram1")));
        }

        @Test
        void given_differentDiagram_then_shouldReturnFalse() {
            var ws = load(b().landscapeDiagram("diagram1").systemContextDiagram("diagram2", "A").softwareSystem("A"));
            assertFalse(diagramProcessor.accept(ws.getDiagrams().getByName("diagram1")));
        }
    }

    @Nested
    class IsAllowed {
        @Test
        void given_softwareSystem_then_shouldReturnTrue() {
            var ws = load(b() //
                    .softwareSystem("A") //
                    .systemContextDiagram("diagram", "A", "title") //
            );
            var diagram = diagramProcessor.cast(ws.getDiagrams().getByName("diagram"));
            assertTrue(diagramProcessor.isAllowed(diagram, getElementById(ws, "A")));
        }

        @Test
        void given_person_then_shouldReturnTrue() {
            var ws = load(b() //
                    .person("A") //
                    .softwareSystem("S") //
                    .systemContextDiagram("diagram", "S", "title") //
            );
            var diagram = diagramProcessor.cast(ws.getDiagrams().getByName("diagram"));
            assertTrue(diagramProcessor.isAllowed(diagram, getElementById(ws, "A")));
        }

        @Test
        void given_group_then_shouldReturnTrue() {
            var ws = load(b() //
                    .group("A") //
                    .softwareSystem("S") //
                    .systemContextDiagram("diagram", "S", "title") //
            );
            var diagram = diagramProcessor.cast(ws.getDiagrams().getByName("diagram"));
            assertTrue(diagramProcessor.isAllowed(diagram, getElementById(ws, "A")));
        }

        @Test
        void given_container_then_shouldReturnTrue() {
            var ws = load(b() //
                    .softwareSystem("A") //
                    .container("A", "B") //
                    .systemContextDiagram("diagram", "A", "title") //
            );
            var diagram = diagramProcessor.cast(ws.getDiagrams().getByName("diagram"));
            assertTrue(diagramProcessor.isAllowed(diagram, getElementById(ws, "B")));
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
            var scope = diagramProcessor.computeScope(diagramProcessor.cast(diagram));
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
            var scope = diagramProcessor.computeScope(diagramProcessor.cast(diagram));
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
            var scope = diagramProcessor.computeScope(diagramProcessor.cast(diagram));
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
            var scope = diagramProcessor.computeScope(diagramProcessor.cast(diagram));
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
            var scope = diagramProcessor.computeScope(diagramProcessor.cast(diagram));
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
            var scope = diagramProcessor.computeScope(diagramProcessor.cast(diagram));
            var c1 = getElementById(ws, "C1");
            var b = getElementById(ws, "B");
            var p = getElementById(ws, "P");

            var r1 = ws.getModel().getRelations().getRelationsBetween(c1, b).get(0);
            var r2 = ws.getModel().getRelations().getRelationsBetween(p, c1).get(0);
            assertTrue(scope.isInScope(r1));
            assertTrue(scope.isInScope(r2));
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
            var scope = diagramProcessor.computeScope(diagramProcessor.cast(diagram));
            var c1 = getElementById(ws, "C1");
            var c2 = getElementById(ws, "C2");

            var r1 = ws.getModel().getRelations().getRelationsBetween(c1, c2).get(0);
            assertTrue(scope.isInScope(r1));
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

    static List<String> stringifyRelations(List<? extends Relation> relations) {
        return relations.stream().map(r -> r.getFrom().toShortString() + " => " + r.getTo().toShortString()).collect(Collectors.toList());
    }

    static Element getElementById(Workspace ws, String id) {
        return ws.getModel().getElementById(id);
    }
}
