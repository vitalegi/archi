package it.vitalegi.archi.util;

import it.vitalegi.archi.model.style.ElementTag;
import it.vitalegi.archi.model.style.LineStyle;
import it.vitalegi.archi.model.style.RelationTag;
import it.vitalegi.archi.model.style.SkinParam;
import it.vitalegi.archi.model.style.Style;
import it.vitalegi.archi.model.style.Tags;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StyleTestUtil {


    public static Style randomStyle() {
        return Style.builder() //
                .skinParams(IntStream.of(5).mapToObj(i -> randomSkinParam()).collect(Collectors.toList())) //
                .tags(randomTags()) //
                .build();
    }

    public static SkinParam randomSkinParam() {
        return SkinParam.builder().key(randomString()).value(randomString()).build();
    }

    public static Tags randomTags() {
        return Tags.builder() //
                .elements(IntStream.of(5).mapToObj(i -> randomElementTag()).collect(Collectors.toList())) //
                .relations(IntStream.of(5).mapToObj(i -> randomRelationTag()).collect(Collectors.toList())) //
                .build();
    }

    public static ElementTag randomElementTag() {
        return ElementTag.builder() //
                .alias(randomString()) //
                .technologies(randomString()) //
                .borderThickness(randomInt(5)) //
                .borderStyle(randomLineStyle()) //
                .build();
    }

    public static RelationTag randomRelationTag() {
        return RelationTag.builder() //
                .alias(randomString()) //
                .technologies(randomString()) //
                .lineThickness(randomInt(5)) //
                .lineStyle(randomLineStyle()) //
                .build();
    }

    static String randomString() {
        return UUID.randomUUID().toString();
    }

    static LineStyle randomLineStyle() {
        return LineStyle.values()[randomInt(LineStyle.values().length)];
    }

    static int randomInt(int size) {
        return (int) (Math.random() * size);
    }
}
