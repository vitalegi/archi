package it.vitalegi.archi.util;

public class ObjectUtil {

    public static <E> E getFirstNotNull(E... elements) {
        for (var element : elements) {
            if (element != null) {
                return element;
            }
        }
        return null;
    }

    public static <E extends MergeableCloneable<E>> E safeMerge(E e1, E e2) {
        if (e1 == null && e2 == null) {
            return null;
        }
        if (e1 != null) {
            return e1.merge(e2);
        }
        return e2.duplicate();
    }
}
