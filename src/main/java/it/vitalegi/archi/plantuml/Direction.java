package it.vitalegi.archi.plantuml;

import lombok.Getter;

@Getter
public enum Direction {
    UP("Rel_Up"),
    RIGHT("Rel_Right"), DOWN("Rel_Down"), LEFT("Rel_Left");

    private final String relationKeyword;

    Direction(String relationKeyword) {
        this.relationKeyword = relationKeyword;
    }
}
