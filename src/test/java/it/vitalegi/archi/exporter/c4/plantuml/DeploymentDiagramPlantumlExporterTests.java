package it.vitalegi.archi.exporter.c4.plantuml;

import it.vitalegi.archi.exporter.c4.plantuml.DeploymentDiagramPlantumlExporter;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.relation.Relation;
import it.vitalegi.archi.model.diagram.DeploymentDiagram;
import it.vitalegi.archi.exception.ElementNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

import static it.vitalegi.archi.util.WorkspaceTestUtil.b;
import static it.vitalegi.archi.util.WorkspaceTestUtil.load;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class DeploymentDiagramPlantumlExporterTests {

    DeploymentDiagramPlantumlExporter processor;

    @BeforeEach
    void init() {
        processor = new DeploymentDiagramPlantumlExporter();
    }

    @Nested
    class Loader {
        @Test
        void given_correctConfiguration_thenSucceed() {
            var ws = load(b().deploymentEnvironment("prod").deploymentDiagram("*", "prod", "123"));
            var diagram = ws.getDiagrams().getByName("123");
            assertNotNull(diagram);
            assertEquals(diagram.getClass(), DeploymentDiagram.class);
            var deploymentDiagram = (DeploymentDiagram) diagram;
            assertEquals("prod", deploymentDiagram.getEnvironment());
            assertEquals("*", deploymentDiagram.getScope());
        }
    }

    @Nested
    class Validate {

        @Nested
        class ScopeAll {
            @Test
            void when_scopeIsAll_thenSucceed() {
                var ws = load(b() //
                        .deploymentEnvironment("prod") //
                        .deploymentDiagram("*", "prod", "123") //
                );
                var diagram = (DeploymentDiagram) ws.getDiagrams().getByName("123");
                processor.validate(diagram);
            }
        }

        @Nested
        class ScopeSoftwareSystem {
            @Test
            void given_SoftwareSystemIsExists_thenSucceed() {
                var ws = load(b() //
                        .deploymentEnvironment("prod") //
                        .deploymentDiagram("A", "prod", "123") //
                        .softwareSystem("A") //
                );
                var diagram = (DeploymentDiagram) ws.getDiagrams().getByName("123");
                processor.validate(diagram);
            }

            @Test
            void given_SoftwareSystemIsMissing_thenFail() {
                var e = Assertions.assertThrows(ElementNotFoundException.class, () -> load(b() //
                        .deploymentEnvironment("prod") //
                        .deploymentDiagram("B", "prod", "123") //
                        .softwareSystem("A") //
                ));
                assertEquals("Can't find B: Scope B on diagram 123 is invalid. Check if all objects exist.", e.getMessage());
            }
        }


        @Nested
        class DeploymentEnvironment {
            @Test
            void given_missingDeploymentEnvironment_thenFail() {
                var e = Assertions.assertThrows(ElementNotFoundException.class, () -> load(b() //
                        .deploymentEnvironment("qa") //
                        .deploymentDiagram("*", "prod", "123") //
                ));
                assertEquals("Can't find prod: required on diagram 123", e.getMessage());
            }
        }
    }

    static List<String> getIds(List<? extends Element> elements) {
        return elements.stream().map(Element::getId).collect(Collectors.toList());
    }

    static List<String> stringifyRelations(List<? extends Relation> relations) {
        return relations.stream().map(r -> r.getFrom().toShortString() + " => " + r.getTo().toShortString()).collect(Collectors.toList());
    }
}
