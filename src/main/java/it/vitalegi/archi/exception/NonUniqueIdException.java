package it.vitalegi.archi.exception;

public class NonUniqueIdException extends Error {
    String id;

    public NonUniqueIdException(String id) {
        super("ID " + id + " is defined more than once");
        this.id = id;
    }
}
