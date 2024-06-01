package it.vitalegi.archi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@Setter
@ToString(callSuper = true)
public class Relation extends Entity {
    Element from;
    Element to;
    String description;
    List<String> tags;
    Map<String, String> metadata;

    public Relation(Model model) {
        super(model);
        tags = new ArrayList<>();
        metadata = new HashMap<>();
    }
}
