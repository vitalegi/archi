package it.vitalegi.archi.util;

import it.vitalegi.archi.style.model.SkinParam;
import it.vitalegi.archi.style.model.Style;

import java.util.Arrays;
import java.util.UUID;

public class StyleTestUtil {


    public static Style randomStyle() {
        return Style.builder() //
                .skinParams(Arrays.asList(randomSkinParam(), randomSkinParam(), randomSkinParam())) //
                .build();
    }

    public static SkinParam randomSkinParam() {
        return SkinParam.builder().key(randomString()).value(randomString()).build();
    }

    static String randomString() {
        return UUID.randomUUID().toString();
    }
}
