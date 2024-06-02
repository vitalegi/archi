package it.vitalegi.archi.plantuml;

import lombok.Getter;

@Getter
public enum LayoutDirection {
    LEFT_TO_RIGHT("left to right direction"), TOP_TO_BOTTOM("top to bottom direction");

    private final String direction;

    LayoutDirection(String direction) {
        this.direction = direction;
    }

}
