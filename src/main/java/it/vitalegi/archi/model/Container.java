package it.vitalegi.archi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Container extends Element {
    SoftwareSystem parent;
}
