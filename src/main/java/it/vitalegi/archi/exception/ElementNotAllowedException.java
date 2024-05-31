package it.vitalegi.archi.exception;

import it.vitalegi.archi.model.Element;

public class ElementNotAllowedException extends Error {
    Element parent;
    Element child;

    public ElementNotAllowedException(Element parent, Element child) {
        super("Can't add " + child.toShortString() + " to " + parent.toShortString());
        this.parent = parent;
        this.child = child;
    }

    public ElementNotAllowedException(String message, Element parent, Element child) {
        super(message);
        this.parent = parent;
        this.child = child;
    }

    public ElementNotAllowedException(String message, Throwable cause, Element parent, Element child) {
        super(message, cause);
        this.parent = parent;
        this.child = child;
    }

}
