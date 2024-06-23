package it.vitalegi.archi.exporter.c4.plantuml.builder;

import it.vitalegi.archi.model.Workspace;
import it.vitalegi.archi.model.diagram.DeploymentDiagram;
import it.vitalegi.archi.model.diagram.FlowDiagram;
import it.vitalegi.archi.model.diagram.LandscapeDiagram;
import it.vitalegi.archi.model.diagram.SystemContextDiagram;
import it.vitalegi.archi.model.diagram.options.DiagramOptions;
import it.vitalegi.archi.model.diagramelement.C4DiagramElementProperty;
import it.vitalegi.archi.model.diagramelement.C4DiagramRelation;
import it.vitalegi.archi.model.element.PropertyEntries;
import it.vitalegi.archi.model.element.PropertyEntry;
import it.vitalegi.archi.util.C4DiagramModelUtil;
import it.vitalegi.archi.util.DiagramVisitorTest;
import it.vitalegi.archi.util.WorkspaceModelBuilder;
import it.vitalegi.archi.workspaceloader.model.DeploymentDiagramRaw;
import it.vitalegi.archi.workspaceloader.model.ElementRaw;
import it.vitalegi.archi.workspaceloader.model.FlowDiagramRaw;
import it.vitalegi.archi.workspaceloader.model.FlowRaw;
import it.vitalegi.archi.workspaceloader.model.FlowStepRaw;
import it.vitalegi.archi.workspaceloader.model.LandscapeDiagramRaw;
import it.vitalegi.archi.workspaceloader.model.RelationRaw;
import it.vitalegi.archi.workspaceloader.model.SystemContextDiagramRaw;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.vitalegi.archi.util.WorkspaceTestUtil.b;
import static it.vitalegi.archi.util.WorkspaceTestUtil.load;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class AllDiagramsModelBuilderTests {

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

    static C4DiagramModelUtil flowDiagram(Workspace ws, FlowDiagram diagram) {
        var builder = new FlowDiagramModelBuilder(ws, diagram);
        var actual = builder.build();
        return new C4DiagramModelUtil(actual);
    }

    @DisplayName("GIVEN inherited relations and direct relation between same elements THEN only direct relation is in the model")
    @Nested
    class InheritedRelationsWithDirectRelation implements DiagramVisitorTest {

        static DiagramOptions options() {
            return DiagramOptions.builder().inheritRelations(true).build();
        }

        WorkspaceModelBuilder defaultBuilder() {
            return b() //
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

                    .landscapeDiagram(LandscapeDiagramRaw.builder().name("landscape").options(options())) //
                    .systemContextDiagram(SystemContextDiagramRaw.builder().name("systemContext").target("A").options(options())) //
                    .deploymentDiagram(DeploymentDiagramRaw.all("deployment", "env").options(options()));
        }

        @Test
        @Override
        public void testLandscapeDiagram() {
            var ws = load(defaultBuilder());
            var diagram = (LandscapeDiagram) ws.getDiagrams().getByName("landscape");
            var model = AllDiagramsModelBuilderTests.landscapeDiagram(ws, diagram);
            assertNotNull(model.findByAliasesPath("SoftwareSystem_A"));
            assertNotNull(model.findByAliasesPath("SoftwareSystem_B"));
            assertEquals(1, model.findRelations("SoftwareSystem_A", "SoftwareSystem_B").size());
            assertEquals(2, model.countElements());
            assertEquals(1, model.countRelations());
        }

        @Test
        @Override
        public void testSystemContextDiagram() {
            var ws = load(defaultBuilder());
            var diagram = (SystemContextDiagram) ws.getDiagrams().getByName("systemContext");
            var model = AllDiagramsModelBuilderTests.systemContextDiagram(ws, diagram);
            assertNotNull(model.findByAliasesPath("SoftwareSystem_A"));
            assertNotNull(model.findByAliasesPath("SoftwareSystem_A", "Container_C1"));
            assertNotNull(model.findByAliasesPath("SoftwareSystem_B"));
            assertEquals(1, model.findRelations("Container_C1", "SoftwareSystem_B").size());
            assertEquals(3, model.countElements());
            assertEquals(1, model.countRelations());
        }

        @Test
        @Override
        public void testDeploymentDiagram() {
            var ws = load(defaultBuilder());
            var diagram = (DeploymentDiagram) ws.getDiagrams().getByName("deployment");

            var model = AllDiagramsModelBuilderTests.deploymentDiagram(ws, diagram);
            assertNotNull(model.findByAliasesPath("DeploymentNode_node"));
            assertNotNull(model.findByAliasesPath("DeploymentNode_node", "SoftwareSystemInstance_si1"));
            assertNotNull(model.findByAliasesPath("DeploymentNode_node", "SoftwareSystemInstance_si2"));
            assertNotNull(model.findByAliasesPath("DeploymentNode_node", "SoftwareSystemInstance_si1", "SoftwareSystem_A"));
            assertNotNull(model.findByAliasesPath("DeploymentNode_node", "SoftwareSystemInstance_si2", "SoftwareSystem_B"));
            assertEquals(1, model.findRelations("SoftwareSystem_A", "SoftwareSystem_B").size());
            assertEquals(5, model.countElements());
            assertEquals(1, model.countRelations());
        }

        @Test
        @Override
        public void testFlowDiagram() {
            var builder = defaultBuilder();
            builder.flow(FlowRaw.builder().id("flow1").name("Flow 1").steps(Arrays.asList( //
                            FlowStepRaw.builder().from("C1").to("C2").label("step 1").build(), //
                            FlowStepRaw.builder().from("C2").to("C1").label("step 2").build(), //
                            FlowStepRaw.builder().from("A").to("B").label("step 3").build() //
                    ))) //
                    .flowDiagram(FlowDiagramRaw.builder().name("flow").title("Flow Diagram").flow("flow1").options(options()));

            var ws = load(builder);
            var diagram = (FlowDiagram) ws.getDiagrams().getByName("flow");
            var model = AllDiagramsModelBuilderTests.flowDiagram(ws, diagram);

            assertNotNull(model.findByAliasesPath("SoftwareSystem_A"));
            assertNotNull(model.findByAliasesPath("SoftwareSystem_A", "Container_C1"));
            assertNotNull(model.findByAliasesPath("SoftwareSystem_B"));
            assertNotNull(model.findByAliasesPath("SoftwareSystem_B", "Container_C2"));
            assertEquals(4, model.countElements());

            assertEquals(1, model.findRelations("Container_C1", "Container_C2").size());
            assertEquals(1, model.findRelations("Container_C2", "Container_C1").size());
            assertEquals(1, model.findRelations("SoftwareSystem_A", "SoftwareSystem_B").size());
            assertEquals(3, model.countRelations());
        }
    }

    @DisplayName("GIVEN hideRelationsText=true THEN labels and descriptions on relations are hidden")
    @Nested
    class HideRelationsText implements DiagramVisitorTest {
        static DiagramOptions options() {
            return DiagramOptions.builder().hideRelationsText(true).build();
        }

        WorkspaceModelBuilder defaultBuilder() {
            return b() //
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

                    .landscapeDiagram(LandscapeDiagramRaw.builder().name("landscape").options(options())) //
                    .systemContextDiagram(SystemContextDiagramRaw.builder().name("systemContext").target("A").options(options())) //
                    .deploymentDiagram(DeploymentDiagramRaw.all("deployment", "env").options(options()));
        }


        @Test
        @Override
        public void testLandscapeDiagram() {
            var ws = load(defaultBuilder());
            var diagram = (LandscapeDiagram) ws.getDiagrams().getByName("landscape");
            var model = AllDiagramsModelBuilderTests.landscapeDiagram(ws, diagram);
            check(model);
        }

        @Test
        @Override
        public void testSystemContextDiagram() {
            var ws = load(defaultBuilder());
            var diagram = (SystemContextDiagram) ws.getDiagrams().getByName("systemContext");
            var model = AllDiagramsModelBuilderTests.systemContextDiagram(ws, diagram);
            check(model);
        }

        @Test
        @Override
        public void testDeploymentDiagram() {
            var ws = load(defaultBuilder());
            var diagram = (DeploymentDiagram) ws.getDiagrams().getByName("deployment");
            var model = AllDiagramsModelBuilderTests.deploymentDiagram(ws, diagram);
            check(model);
        }

        @Test
        @Override
        public void testFlowDiagram() {
            var builder = defaultBuilder();
            builder.flow(FlowRaw.builder().id("flow1").name("Flow 1").steps(Arrays.asList( //
                            FlowStepRaw.builder().from("C1").to("C2").label("step 1").build(), //
                            FlowStepRaw.builder().from("C2").to("C1").label("step 2").build(), //
                            FlowStepRaw.builder().from("A").to("B").label("step 3").build() //
                    ))) //
                    .flowDiagram(FlowDiagramRaw.builder().name("flow").title("Flow Diagram").flow("flow1").options(options()));

            var ws = load(builder);
            var diagram = (FlowDiagram) ws.getDiagrams().getByName("flow");
            var model = AllDiagramsModelBuilderTests.flowDiagram(ws, diagram);

            check(model);
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

    @DisplayName("GIVEN elements and relations with properties THEN properties are in the model")
    @Nested
    class ElementsAndRelationsWithProperties implements DiagramVisitorTest {

        static DiagramOptions options() {
            return DiagramOptions.builder().hideRelationsText(false).build();
        }

        static PropertyEntries properties() {
            var properties = new PropertyEntries();
            properties.getProperties().add(new PropertyEntry("key1", "value1"));
            properties.getProperties().add(new PropertyEntry("key2", "value2"));
            return properties;
        }

        static List<C4DiagramElementProperty> expectedProperties() {
            var expected = new ArrayList<C4DiagramElementProperty>();
            expected.add(new C4DiagramElementProperty("key1", "value1"));
            expected.add(new C4DiagramElementProperty("key2", "value2"));
            return expected;
        }

        WorkspaceModelBuilder defaultBuilder() {
            return b() //
                    .element(ElementRaw.softwareSystem().id("A").properties(properties())) //
                    .element(ElementRaw.container().parentId("A").id("C1").properties(properties())) //

                    .element(ElementRaw.softwareSystem().id("B").properties(properties())) //
                    .element(ElementRaw.container().parentId("B").id("C2").properties(properties())) //

                    .relation(RelationRaw.builder().from("A").to("B").properties(properties())) //
                    .relation(RelationRaw.builder().from("C1").to("C2").properties(properties())) //
                    .relation(RelationRaw.builder().from("C1").to("B").properties(properties())) //

                    .deploymentEnvironment("env") //
                    .deploymentNode("env", "node") //
                    .softwareSystemInstance("node", "si1", "A") //
                    .softwareSystemInstance("node", "si2", "B") //

                    .landscapeDiagram(LandscapeDiagramRaw.builder().name("landscape").options(options())) //
                    .systemContextDiagram(SystemContextDiagramRaw.builder().name("systemContext").target("A").options(options())) //
                    .deploymentDiagram(DeploymentDiagramRaw.all("deployment", "env").options(options())) //
                    ;
        }


        @Test
        @Override
        public void testLandscapeDiagram() {
            var ws = load(defaultBuilder());
            var diagram = (LandscapeDiagram) ws.getDiagrams().getByName("landscape");
            var model = AllDiagramsModelBuilderTests.landscapeDiagram(ws, diagram);
            check(model);
        }

        @Test
        @Override
        public void testSystemContextDiagram() {
            var ws = load(defaultBuilder());
            var diagram = (SystemContextDiagram) ws.getDiagrams().getByName("systemContext");
            var model = AllDiagramsModelBuilderTests.systemContextDiagram(ws, diagram);
            check(model);
        }

        @Test
        @Override
        public void testDeploymentDiagram() {
            var ws = load(defaultBuilder());
            var diagram = (DeploymentDiagram) ws.getDiagrams().getByName("deployment");
            var model = AllDiagramsModelBuilderTests.deploymentDiagram(ws, diagram);
            check(model);
        }

        @Test
        @Override
        public void testFlowDiagram() {
            var builder = defaultBuilder();
            builder.flow(FlowRaw.builder().id("flow1").name("Flow 1").steps(Arrays.asList( //
                            FlowStepRaw.builder().from("C1").to("C2").label("step 1").properties(properties()).build(), //
                            FlowStepRaw.builder().from("C2").to("C1").label("step 2").properties(properties()).build(), //
                            FlowStepRaw.builder().from("A").to("B").label("step 3").properties(properties()).build() //
                    ))) //
                    .flowDiagram(FlowDiagramRaw.builder().name("flow").title("Flow Diagram").flow("flow1").options(options()));

            var ws = load(builder);
            var diagram = (FlowDiagram) ws.getDiagrams().getByName("flow");
            var model = AllDiagramsModelBuilderTests.flowDiagram(ws, diagram);

            var r1 = model.findRelations("Container_C1", "Container_C2");
            assertEquals(1, r1.size());
            check(r1.get(0));

            var r2 = model.findRelations("Container_C2", "Container_C1");
            assertEquals(1, r2.size());
            check(r2.get(0));

            var r3 = model.findRelations("SoftwareSystem_A", "SoftwareSystem_B");
            assertEquals(1, r3.size());
            check(r3.get(0));
        }

        protected void check(C4DiagramModelUtil model) {


            var b = model.findByAlias("SoftwareSystem_B");
            assertEquals(expectedProperties(), b.getProperties());

            var relations = model.findAllRelations();
            assertNotEquals(0, relations.size());
            for (var relation : relations) {
                check(relation);
            }
        }

        protected void check(C4DiagramRelation relation) {
            assertEquals(expectedProperties(), relation.getProperties(), "Relation " + relation + " doesn't have correct properties");
        }
    }
}
