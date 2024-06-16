package it.vitalegi.archi.exporter.c4.plantuml.builder;

import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.DeploymentDiagram;
import it.vitalegi.archi.model.diagram.DiagramOptions;
import it.vitalegi.archi.model.diagram.LandscapeDiagram;
import it.vitalegi.archi.model.diagram.SystemContextDiagram;
import it.vitalegi.archi.util.C4DiagramModelUtil;
import it.vitalegi.archi.visitor.DiagramVisitor;
import it.vitalegi.archi.workspaceloader.model.DeploymentDiagramRaw;
import it.vitalegi.archi.workspaceloader.model.ElementRaw;
import it.vitalegi.archi.workspaceloader.model.LandscapeDiagramRaw;
import it.vitalegi.archi.workspaceloader.model.SystemContextDiagramRaw;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static it.vitalegi.archi.util.WorkspaceTestUtil.b;
import static it.vitalegi.archi.util.WorkspaceTestUtil.load;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class AllDiagramsModelBuilderTests {

    @DisplayName("GIVEN inherited relations and direct relation between same elements THEN only direct relation is in the model")
    @Nested
    class InheritedRelationsWithDirectRelation implements DiagramVisitor<Void> {

        Workspace ws;

        @BeforeEach
        void init() {
            var options = DiagramOptions.builder().inheritRelations(true).build();
            ws = load(b() //
                    .softwareSystem("A") //
                    .container("A", "C1") //
                    .component(ElementRaw.builder().parentId("C1").id("comp1")) //

                    .softwareSystem("B") //
                    .container("B", "C2") //
                    .component(ElementRaw.builder().parentId("C2").id("comp2")) //

                    .relation("comp1", "comp2") //
                    .relation("A", "B") //
                    .relation("C1", "C2") //

                    .deploymentEnvironment("env") //
                    .deploymentNode("env", "node") //
                    .softwareSystemInstance("node", "si1", "A") //
                    .softwareSystemInstance("node", "si2", "B") //

                    .landscapeDiagram(LandscapeDiagramRaw.builder().name("landscape").options(options)) //
                    .systemContextDiagram(SystemContextDiagramRaw.builder().name("systemContext").target("A").options(options)) //
                    .deploymentDiagram(DeploymentDiagramRaw.all("deployment", "env").options(options)) //
            );
        }

        @Test
        void landscapeDiagram() {
            ws.getDiagrams().getByName("landscape").visit(this);
        }

        @Test
        void systemContextDiagram() {
            ws.getDiagrams().getByName("systemContext").visit(this);
        }

        @Test
        void deploymentDiagram() {
            ws.getDiagrams().getByName("deployment").visit(this);
        }

        @Override
        public Void visitLandscapeDiagram(LandscapeDiagram diagram) {
            var model = AllDiagramsModelBuilderTests.landscapeDiagram(ws, diagram);
            assertNotNull(model.findByAliasesPath("SoftwareSystem_A"));
            assertNotNull(model.findByAliasesPath("SoftwareSystem_B"));
            assertEquals(1, model.findRelations("SoftwareSystem_A", "SoftwareSystem_B").size());
            assertEquals(2, model.countElements());
            assertEquals(1, model.countRelations());
            return null;
        }

        @Override
        public Void visitSystemContextDiagram(SystemContextDiagram diagram) {
            var model = AllDiagramsModelBuilderTests.systemContextDiagram(ws, diagram);
            assertNotNull(model.findByAliasesPath("SoftwareSystem_A"));
            assertNotNull(model.findByAliasesPath("SoftwareSystem_A", "Container_C1"));
            assertNotNull(model.findByAliasesPath("SoftwareSystem_B"));
            assertEquals(1, model.findRelations("Container_C1", "SoftwareSystem_B").size());
            assertEquals(3, model.countElements());
            assertEquals(1, model.countRelations());
            return null;
        }

        @Override
        public Void visitDeploymentDiagram(DeploymentDiagram diagram) {
            var model = AllDiagramsModelBuilderTests.deploymentDiagram(ws, diagram);
            assertNotNull(model.findByAliasesPath("DeploymentNode_node"));
            assertNotNull(model.findByAliasesPath("DeploymentNode_node", "SoftwareSystemInstance_si1"));
            assertNotNull(model.findByAliasesPath("DeploymentNode_node", "SoftwareSystemInstance_si2"));
            assertNotNull(model.findByAliasesPath("DeploymentNode_node", "SoftwareSystemInstance_si1", "SoftwareSystem_A"));
            assertNotNull(model.findByAliasesPath("DeploymentNode_node", "SoftwareSystemInstance_si2", "SoftwareSystem_B"));
            assertEquals(1, model.findRelations("SoftwareSystem_A", "SoftwareSystem_B").size());
            assertEquals(5, model.countElements());
            assertEquals(1, model.countRelations());
            return null;
        }
    }

    static C4DiagramModelUtil deploymentDiagram(Workspace ws, DeploymentDiagram diagram) {
        var builder = new DeploymentDiagramModelBuilder(ws, diagram);
        var actual = builder.build();
        return new C4DiagramModelUtil(actual);
    }

    static C4DiagramModelUtil landscapeDiagram(Workspace ws, LandscapeDiagram diagram) {
        var builder = new LandscapeDiagramModelBuilder(ws, diagram);
        var actual = builder.build();
        return new C4DiagramModelUtil(actual);
    }

    static C4DiagramModelUtil systemContextDiagram(Workspace ws, SystemContextDiagram diagram) {
        var builder = new SystemContextDiagramModelBuilder(ws, diagram);
        var actual = builder.build();
        return new C4DiagramModelUtil(actual);
    }
}
