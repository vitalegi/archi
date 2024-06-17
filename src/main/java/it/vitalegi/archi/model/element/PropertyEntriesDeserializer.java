package it.vitalegi.archi.model.element;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class PropertyEntriesDeserializer extends StdDeserializer<PropertyEntries> {

    public PropertyEntriesDeserializer() {
        this(null);
    }

    protected PropertyEntriesDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public PropertyEntries deserialize(JsonParser jp, DeserializationContext ctx) throws IOException, JacksonException {
        var node = jp.getCodec().readTree(jp);
        var props = new PropertyEntries();
        var names = node.fieldNames();
        while (names.hasNext()) {
            var name = names.next();
            var value = asString(node.get(name));
            props.getProperties().add(new PropertyEntry(name, value));
        }
        return props;
    }

    protected String asString(TreeNode node) throws IOException {
        if (node instanceof TextNode) {
            return ((TextNode) node).asText();
        }
        if (node instanceof NumericNode) {
            return ((NumericNode) node).asText();
        }
        if (node instanceof BooleanNode) {
            return ((BooleanNode) node).asText();
        }
        throw new IOException("Can't map " + node + " as text");
    }
}
