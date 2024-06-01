package it.vitalegi.archi.view.dto;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ToString
public class Views {
    List<View> views;

    public Views() {
        views = new ArrayList<>();
    }

    public List<View> getAll() {
        return views;
    }

    public void add(View view) {
        views.add(view);
    }

    public View getByName(String name) {
        return views.stream().filter(v -> Objects.equals(v.getName(), name)).findFirst().orElse(null);
    }
}
