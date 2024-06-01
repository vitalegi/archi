package it.vitalegi.archi.model.view;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ToString
public class Views {
    List<BaseView> views;

    public Views() {
        views = new ArrayList<>();
    }

    public List<BaseView> getAll() {
        return views;
    }

    public void add(BaseView view) {
        views.add(view);
    }

    public BaseView getByName(String name) {
        return views.stream().filter(v -> Objects.equals(v.getName(), name)).findFirst().orElse(null);
    }
}
