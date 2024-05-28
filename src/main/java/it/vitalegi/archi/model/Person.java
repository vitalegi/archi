package it.vitalegi.archi.model;

import lombok.Data;
import lombok.ToString;

@ToString(callSuper = true)
public class Person extends Element {

    public Element findChildById(String id) {
        return null;
    }

    public void addChild(Element child) {
        throw new IllegalArgumentException("Can't add " + child + " to a person " + this);
    }
}
