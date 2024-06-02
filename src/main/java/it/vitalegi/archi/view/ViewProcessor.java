package it.vitalegi.archi.view;

import it.vitalegi.archi.view.constant.ViewFormat;
import it.vitalegi.archi.view.dto.View;

import java.nio.file.Path;

public interface ViewProcessor {
    boolean accept(View view);
    void validate(View view);
    void render(View view, Path basePath, ViewFormat[] formats);
}
