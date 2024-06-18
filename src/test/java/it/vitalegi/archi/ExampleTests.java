package it.vitalegi.archi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;

@ExtendWith(MockitoExtension.class)
public class ExampleTests {

    @Test
    void simpleWebapp() {
        var root = Path.of("examples", "simple-webapp");
        App.execute(root, root.resolve("output"));
    }

    @Test
    void properties() {
        var root = Path.of("examples", "properties");
        App.execute(root, root.resolve("output"));
    }

    @Test
    void flows() {
        var root = Path.of("examples", "flows");
        App.execute(root, root.resolve("output"));
    }
    @Test
    void hiddenRelations() {
        var root = Path.of("examples", "hidden-relations");
        App.execute(root, root.resolve("output"));
    }
}
