package it.vitalegi.archi.exception;

public class ElementNotFoundException extends Error {

    String target;

    public ElementNotFoundException(String target, String message) {
        super("Can't find " + target + ": " + message);
        this.target = target;
    }

    public ElementNotFoundException(String target) {
        super("Can't find " + target);
        this.target = target;
    }
}
