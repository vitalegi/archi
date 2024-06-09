package it.vitalegi.archi.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListUtil {
    public static <E extends MergeableCloneable<E>> List<E> merge(List<E> list1, List<E> list2, Comparator<E> comparator) {
        if (list1 == null) {
            list1 = Collections.emptyList();
        }
        if (list2 == null) {
            list2 = Collections.emptyList();
        }
        var out = new ArrayList<E>();
        list1.forEach(e -> addOrUpdate(out, e, comparator));
        list2.forEach(e -> addOrUpdate(out, e, comparator));
        return out;
    }

    public static <E extends Cloneable<E>> List<E> duplicate(List<E> list) {
        if (list == null) {
            return null;
        }
        return list.stream().map(E::duplicate).collect(Collectors.toList());
    }

    public static <E extends MergeableCloneable<E>> void addOrUpdate(List<E> list, E element, Comparator<E> comparator) {
        int index = indexOf(list, element, comparator);
        if (index == -1) {
            list.add(element.duplicate());
        } else {
            var existing = list.remove(index);
            list.add(existing.merge(element));
        }
    }

    private static <E> int indexOf(List<E> list, E target, Comparator<E> comparator) {
        for (int i = 0; i < list.size(); i++) {
            if (comparator.compare(target, list.get(i)) == 0) {
                return i;
            }
        }
        return -1;
    }
}
