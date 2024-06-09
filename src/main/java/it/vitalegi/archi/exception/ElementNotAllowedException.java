package it.vitalegi.archi.exception;

import it.vitalegi.archi.model.element.Element;

public class ElementNotAllowedException extends Error {
    Element parent;
    Element child;

    public ElementNotAllowedException(Element parent, Element child) {
        super(format(parent, child));
        this.parent = parent;
        this.child = child;
    }

    public ElementNotAllowedException(String message, Element parent, Element child) {
        super(format(parent, child) + ". " + message);
        this.parent = parent;
        this.child = child;
    }

    public ElementNotAllowedException(String message, Throwable cause, Element parent, Element child) {
        super(format(parent, child) + ". " + message, cause);
        this.parent = parent;
        this.child = child;
    }

    protected static String format(Element parent, Element child) {
        return "Can't add " + child.toShortString() + " to " + parent.toShortString();
    }
}
