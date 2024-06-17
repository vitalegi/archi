package it.vitalegi.archi.diagram;

import lombok.Getter;

@Getter
public enum C4Shape {
    RECTANGLE("rectangle"),
    CIRCLE("circle"),
    DATABASE("database"),
    QUEUE("queue"),
    ACTOR("actor"),
    CLOUD("cloud"),
    COLLECTIONS("collections"),
    FILE("file"),
    FOLDER("folder"),
    HEXAGON("hexagon"),
    PACKAGE("package"),
    STORAGE("storage");

    private final String plantumlShape;

    C4Shape(String plantumlShape) {
        this.plantumlShape = plantumlShape;
    }

}
