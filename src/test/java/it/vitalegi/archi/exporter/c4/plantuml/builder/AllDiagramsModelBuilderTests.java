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
import it.vitalegi.archi.workspaceloader.model.RelationRaw;
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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    @DisplayName("GIVEN inherited relations and direct relation between same elements THEN only direct relation is in the model")
    @Nested
    class HideRelationsText implements DiagramVisitor<Void> {

        Workspace ws;

        @BeforeEach
        void init() {
            var options = DiagramOptions.builder().hideRelationsText(true).build();
            ws = load(b() //
                    .softwareSystem("A") //
                    .container("A", "C1") //
                    .component(ElementRaw.builder().parentId("C1").id("comp1")) //

                    .softwareSystem("B") //
                    .container("B", "C2") //
                    .component(ElementRaw.builder().parentId("C2").id("comp2")) //

                    .relation(RelationRaw.builder().from("comp1").to("comp2").label("Label 1").description("Description 1")) //
                    .relation(RelationRaw.builder().from("A").to("B").label("Label AB").description("Description AB")) //
                    .relation(RelationRaw.builder().from("C1").to("C2").label("Label C1 - C2").description("Description C1 - C2")) //
                    .relation(RelationRaw.builder().from("C1").to("B").label("Label C1 - B").description("Description C1 - B")) //

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
            check(model);
            return null;
        }

        @Override
        public Void visitSystemContextDiagram(SystemContextDiagram diagram) {
            var model = AllDiagramsModelBuilderTests.systemContextDiagram(ws, diagram);
            check(model);
            return null;
        }

        @Override
        public Void visitDeploymentDiagram(DeploymentDiagram diagram) {
            var model = AllDiagramsModelBuilderTests.deploymentDiagram(ws, diagram);
            check(model);
            return null;
        }

        protected void check(C4DiagramModelUtil model) {
            var relations = model.findAllRelations();
            assertNotEquals(0, relations.size());
            for (var relation : relations) {
                assertNull(relation.getDescription(), "Relation " + relation + " must have null description");
                assertNull(relation.getLabel(), "Relation " + relation + " must have null label");
            }
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
