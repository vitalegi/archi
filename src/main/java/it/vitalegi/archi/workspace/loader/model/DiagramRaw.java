package it.vitalegi.archi.workspace.loader.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeploymentDiagramRaw.class, name = "DEPLOYMENT"),
        @JsonSubTypes.Type(value = LandscapeDiagramRaw.class, name = "LANDSCAPE"),
        @JsonSubTypes.Type(value = SystemContextDiagramRaw.class, name = "SYSTEM_CONTEXT")
})
@Data
@JsonIgnoreProperties({"type"})
public class DiagramRaw {
    String name;
    String title;
}
