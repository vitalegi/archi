package it.vitalegi.archi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SoftwareSystem extends Element {
    List<Container> containers;

    public SoftwareSystem() {
        containers = new ArrayList<>();
    }
}
