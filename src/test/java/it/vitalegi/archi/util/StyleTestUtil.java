package it.vitalegi.archi.util;

import it.vitalegi.archi.style.model.ElementTag;
import it.vitalegi.archi.style.model.RelationTag;
import it.vitalegi.archi.style.model.SkinParam;
import it.vitalegi.archi.style.model.Style;
import it.vitalegi.archi.style.model.Tags;

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
        return ElementTag.builder().alias(randomString()).backgroundColor(randomString()).technologies(randomString()).build();
    }

    public static RelationTag randomRelationTag() {
        return RelationTag.builder().alias(randomString()).build();
    }

    static String randomString() {
        return UUID.randomUUID().toString();
    }
}
