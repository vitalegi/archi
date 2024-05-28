package it.vitalegi.archi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class Container extends Element {

    public Element findChildById(String id) {
        return null;
    }

    public void addChild(Element child) {
        throw new IllegalArgumentException("Can't add " + child + " to a container " + this);
    }
}
