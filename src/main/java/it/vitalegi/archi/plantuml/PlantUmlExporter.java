package it.vitalegi.archi.plantuml;

import it.vitalegi.archi.util.FileUtil;
import it.vitalegi.archi.view.renderer.ViewFormat;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

@Slf4j
public class PlantUmlExporter {
    public void export(Path outDir, String name, ViewFormat[] formats, String pumlDiagram) {
        for (var format : formats) {
            export(outDir, name, format, pumlDiagram);
        }
    }

    public void export(Path outDir, String name, ViewFormat format, String pumlDiagram) {
        switch (format) {
            case PUML -> saveAsPuml(outDir, name, pumlDiagram);
            case PNG -> saveAsPng(outDir, name, pumlDiagram);
            case SVG -> saveAsSvg(outDir, name, pumlDiagram);
        }
    }

    protected void saveAsPuml(Path outDir, String name, String pumlDiagram) {
        var filename = name + ".puml";
        var out = outDir.resolve(filename).toFile();
        log.debug("Save diagram as " + out);
        FileUtil.createDirs(outDir);
        try (var pw = new PrintWriter(out)) {
            pw.println(pumlDiagram);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected File saveAsSvg(Path outDir, String name, String pumlDiagram) {
        return saveAs(outDir, name + ".svg", pumlDiagram, FileFormat.SVG);
    }

    protected File saveAsPng(Path outDir, String name, String pumlDiagram) {
        return saveAs(outDir, name + ".png", pumlDiagram, FileFormat.PNG);
    }

    protected File saveAs(Path outDir, String fileName, String pumlDiagram, FileFormat format) {
        SourceStringReader reader = new SourceStringReader(pumlDiagram);
        FileUtil.createDirs(outDir);
        File out = new File(outDir.toFile(), fileName);
        log.debug("Save diagram as " + out);
        try (FileOutputStream fos = new FileOutputStream(out)) {
            reader.outputImage(fos, new FileFormatOption(format, false));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return out;
    }
}