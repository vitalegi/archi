package it.vitalegi.archi.model.diagramelement;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class C4DiagramElementProperty {
    String col1;
    String col2;
    String col3;
    String col4;

    public C4DiagramElementProperty(String col1, String col2) {
        this.col1 = col1;
        this.col2 = col2;
    }
}
