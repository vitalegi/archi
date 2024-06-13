package it.vitalegi.archi.util;

public interface Mergeable<E> {

    /**
     * Takes another object and create a new instance of self with others' values merged
     *
     * @param other
     * @return
     */
    E merge(E other);
}
