package it.vitalegi.archi.model.style;

import it.vitalegi.archi.util.MergeableCloneable;
import it.vitalegi.archi.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class ElementTag implements MergeableCloneable<ElementTag> {
    String alias;
    String backgroundColor;
    String fontColor;
    String borderColor;
    Boolean shadowing;
    String shape;
    String sprite;
    String technologies;
    String legendText;
    String legendSprite;
    LineStyle borderStyle;
    Integer borderThickness;

    @Override
    public ElementTag duplicate() {
        var out = new ElementTag();
        out.alias = this.alias;
        out.backgroundColor = this.backgroundColor;
        out.fontColor = this.fontColor;
        out.borderColor = this.borderColor;
        out.shadowing = this.shadowing;
        out.shape = this.shape;
        out.sprite = this.sprite;
        out.technologies = this.technologies;
        out.legendText = this.legendText;
        out.legendSprite = this.legendSprite;
        out.borderStyle = this.borderStyle;
        out.borderThickness = this.borderThickness;
        return out;
    }

    @Override
    public ElementTag merge(ElementTag other) {
        var out = this.duplicate();
        out.alias = ObjectUtil.getFirstNotNull(this.alias, other.alias);
        out.backgroundColor = ObjectUtil.getFirstNotNull(this.backgroundColor, other.backgroundColor);
        out.fontColor = ObjectUtil.getFirstNotNull(this.fontColor, other.fontColor);
        out.borderColor = ObjectUtil.getFirstNotNull(this.borderColor, other.borderColor);
        out.shadowing = ObjectUtil.getFirstNotNull(this.shadowing, other.shadowing);
        out.shape = ObjectUtil.getFirstNotNull(this.shape, other.shape);
        out.sprite = ObjectUtil.getFirstNotNull(this.sprite, other.sprite);
        out.technologies = ObjectUtil.getFirstNotNull(this.technologies, other.technologies);
        out.legendText = ObjectUtil.getFirstNotNull(this.legendText, other.legendText);
        out.legendSprite = ObjectUtil.getFirstNotNull(this.legendSprite, other.legendSprite);
        out.borderStyle = ObjectUtil.getFirstNotNull(this.borderStyle, other.borderStyle);
        out.borderThickness = ObjectUtil.getFirstNotNull(this.borderThickness, other.borderThickness);
        return out;
    }
}
