package it.vitalegi.archi.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Setter
@EqualsAndHashCode
@ToString
public abstract class Entity {
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Setter(AccessLevel.PROTECTED)
    @Getter
    protected Model model;
    String id;
    String uniqueId;

    public Entity(Model model) {
        this.model = model;
    }

    public static boolean equals(String id1, String id2) {
        return Objects.equals(id1, id2);
    }

    public static String collectIds(List<? extends Entity> elements) {
        return elements.stream().map(Entity::getId).collect(Collectors.joining(", "));
    }

    public String toShortString() {
        return getClass().getSimpleName() + " (" + getId() + ")";
    }

    public void validate() {
    }

    public abstract ElementType getElementType();
}
