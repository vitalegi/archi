package it.vitalegi.archi.diagram;

import it.vitalegi.archi.diagram.dto.LandscapeDiagram;
import it.vitalegi.archi.model.Element;
import it.vitalegi.archi.model.Relation;
import it.vitalegi.archi.util.WorkspaceLoaderBuilder;
import it.vitalegi.archi.workspace.Workspace;
import org.junit.jupiter.api.BeforeEach;
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
public class LandscapeDiagramProcessorTests {

    LandscapeDiagramProcessor diagramProcessor;

    @BeforeEach
    void init() {
        diagramProcessor = new LandscapeDiagramProcessor();
    }

    @Nested
    class Loader {
        @Test
        void given_correctConfiguration_thenSucceed() {
            var ws = load(b().landscapeDiagram("A", "B"));
            var diagram = ws.getDiagrams().getByName("A");
            assertNotNull(diagram);
            assertEquals(diagram.getClass(), LandscapeDiagram.class);
            var d = (LandscapeDiagram) diagram;
            assertEquals("A", d.getName());
            assertEquals("B", d.getTitle());
        }
    }

    @Nested
    class Accept {
        @Test
        void given_landscapeDiagram_then_shouldReturnTrue() {
            var ws = load(b().deploymentEnvironment("prod").landscapeDiagram("diagram1"));
            assertTrue(diagramProcessor.accept(ws.getDiagrams().getByName("diagram1")));
        }

        @Test
        void given_differentDiagram_then_shouldReturnFalse() {
            var ws = load(b().deploymentEnvironment("prod").deploymentDiagram(null, "prod", "diagram1").landscapeDiagram("diagram2", "AAA"));
            assertFalse(diagramProcessor.accept(ws.getDiagrams().getByName("diagram1")));
        }
    }

    @Nested
    class IsAllowed {
        @Test
        void given_softwareSystem_then_shouldReturnTrue() {
            var ws = load(b() //
                    .softwareSystem("A") //
                    .landscapeDiagram("diagram", "title") //
            );
            var diagram = diagramProcessor.cast(ws.getDiagrams().getByName("diagram"));
            assertTrue(diagramProcessor.isAllowed(diagram, getElementById(ws, "A")));
        }

        @Test
        void given_person_then_shouldReturnTrue() {
            var ws = load(b() //
                    .person("A") //
                    .landscapeDiagram("diagram", "title") //
            );
            var diagram = diagramProcessor.cast(ws.getDiagrams().getByName("diagram"));
            assertTrue(diagramProcessor.isAllowed(diagram, getElementById(ws, "A")));
        }

        @Test
        void given_group_then_shouldReturnTrue() {
            var ws = load(b() //
                    .group("A") //
                    .landscapeDiagram("diagram", "title") //
            );
            var diagram = diagramProcessor.cast(ws.getDiagrams().getByName("diagram"));
            assertTrue(diagramProcessor.isAllowed(diagram, getElementById(ws, "A")));
        }

        @Test
        void given_container_then_shouldReturnFalse() {
            var ws = load(b() //
                    .softwareSystem("A") //
                    .container("A", "B") //
                    .landscapeDiagram("diagram", "title") //
            );
            var diagram = diagramProcessor.cast(ws.getDiagrams().getByName("diagram"));
            assertFalse(diagramProcessor.isAllowed(diagram, getElementById(ws, "B")));
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
            var scope = diagramProcessor.computeScope(diagramProcessor.cast(diagram));
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
            var scope = diagramProcessor.computeScope(diagramProcessor.cast(diagram));
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
            var scope = diagramProcessor.computeScope(diagramProcessor.cast(diagram));
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
            var scope = diagramProcessor.computeScope(diagramProcessor.cast(diagram));
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
            var scope = diagramProcessor.computeScope(diagramProcessor.cast(diagram));
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
            var scope = diagramProcessor.computeScope(diagramProcessor.cast(diagram));
            var a1 = getElementById(ws, "A1");
            var b = getElementById(ws, "B");

            var r = ws.getModel().getRelations().getRelationsBetween(a1, b).get(0);
            assertFalse(scope.isInScope(r));
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
