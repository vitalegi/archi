package it.vitalegi.archi.workspaceloader.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.vitalegi.archi.diagram.model.Diagram;
import it.vitalegi.archi.style.model.Style;
import it.vitalegi.archi.workspaceloader.visitor.DiagramRawVisitor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeploymentDiagramRaw.class, name = "DEPLOYMENT"),
        @JsonSubTypes.Type(value = LandscapeDiagramRaw.class, name = "LANDSCAPE"),
        @JsonSubTypes.Type(value = SystemContextDiagramRaw.class, name = "SYSTEM_CONTEXT")
})
@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties({"type"})
@NoArgsConstructor
@AllArgsConstructor
public abstract class DiagramRaw {
    String name;
    String title;
    Style style;

    public abstract <E> E visit(DiagramRawVisitor<E> visitor);
}
