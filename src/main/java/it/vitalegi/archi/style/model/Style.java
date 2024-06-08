package it.vitalegi.archi.style.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Style {
    List<SkinParam> skinParams;

    public static Style merge(Style style1, Style style2) {
        var out = new Style();
        if (style1 == null) {
            style1 = new Style();
        }
        if (style2 == null) {
            style2 = new Style();
        }
        out.setSkinParams(SkinParam.merge(style1.getSkinParams(), style2.getSkinParams()));
        return out;
    }

    public void validate() {

    }
}
