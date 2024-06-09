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
public class RelationTag implements MergeableCloneable<RelationTag> {
    String alias;
    String textColor;
    String lineColor;
    LineStyle lineStyle;
    String sprite;
    String technologies;
    String legendText;
    String legendSprite;
    Integer lineThickness;

    @Override
    public RelationTag duplicate() {
        var out = new RelationTag();
        out.alias = this.alias;
        out.textColor = this.textColor;
        out.lineColor = this.lineColor;
        out.lineStyle = this.lineStyle;
        out.sprite = this.sprite;
        out.technologies = this.technologies;
        out.legendText = this.legendText;
        out.legendSprite = this.legendSprite;
        out.lineThickness = this.lineThickness;
        return out;
    }

    @Override
    public RelationTag merge(RelationTag other) {
        var out = this.duplicate();
        out.alias = ObjectUtil.getFirstNotNull(this.alias, other.alias);
        out.textColor = ObjectUtil.getFirstNotNull(this.textColor, other.textColor);
        out.lineColor = ObjectUtil.getFirstNotNull(this.lineColor, other.lineColor);
        out.lineStyle = ObjectUtil.getFirstNotNull(this.lineStyle, other.lineStyle);
        out.sprite = ObjectUtil.getFirstNotNull(this.sprite, other.sprite);
        out.technologies = ObjectUtil.getFirstNotNull(this.technologies, other.technologies);
        out.legendText = ObjectUtil.getFirstNotNull(this.legendText, other.legendText);
        out.legendSprite = ObjectUtil.getFirstNotNull(this.legendSprite, other.legendSprite);
        out.lineThickness = ObjectUtil.getFirstNotNull(this.lineThickness, other.lineThickness);
        return out;
    }
}
