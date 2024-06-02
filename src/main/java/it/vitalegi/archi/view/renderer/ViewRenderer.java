package it.vitalegi.archi.view.renderer;

import it.vitalegi.archi.view.dto.View;

import java.nio.file.Path;

public interface ViewRenderer {
    void render(View view, Path basePath, ViewFormat[] formats);
}
