package it.vitalegi.archi.style.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class SkinParam {
    String key;
    String value;

    public static List<SkinParam> merge(List<SkinParam> e1, List<SkinParam> e2) {
        if (e1 == null) {
            e1 = Collections.emptyList();
        }
        if (e2 == null) {
            e2 = Collections.emptyList();
        }
        var out = new ArrayList<SkinParam>();
        e1.forEach(e -> addOrUpdate(out, e));
        e2.forEach(e -> addOrUpdate(out, e));
        return out;
    }

    private static void addOrUpdate(List<SkinParam> list, SkinParam element) {
        int index = indexOf(list, element.getKey());
        if (index == -1) {
            list.add(element.duplicate());
        } else {
            list.remove(index);
            list.add(element.duplicate());
        }
    }

    private static int indexOf(List<SkinParam> list, String key) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getKey().equals(key)) {
                return i;
            }
        }
        return -1;
    }

    public SkinParam duplicate() {
        return SkinParam.builder().key(key).value(value).build();
    }
}
