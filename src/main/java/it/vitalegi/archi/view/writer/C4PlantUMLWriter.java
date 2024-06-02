package it.vitalegi.archi.view.writer;

import it.vitalegi.archi.plantuml.Direction;

import java.util.Formatter;

public class C4PlantUMLWriter extends PlantUMLWriter {

    public void addElementTag(String alias, String bgColor, String borderColor, String fontColor, String sprite, String shadowing, String borderStyle) {
        println(format("AddElementTag(\"%s\", $bgColor=\"%s\", $borderColor=\"%s\", $fontColor=\"%s\", $sprite=\"%s\", $shadowing=\"%s\", $borderStyle=\"%s\")", alias, bgColor, borderColor, fontColor, sprite, shadowing, borderStyle));
    }

    public void addRelTag(String alias, String textColor, String lineColor, String lineStyle) {
        println(format("AddRelTag(\"%s\", $textColor=\"%s\", $lineColor=\"%s\", $lineStyle=\"%s\")", alias, textColor, lineColor, lineStyle));
    }

    public void deploymentNodeStart(String alias, String label, String type, String description, String sprite, String tags, String link) {
        println(format("Deployment_Node(%s, %s, $type=\"%s\", $descr=\"%s\", $sprite=\"%s\", $tags=\"%s\", $link=\"%s\") {", alias, label, type, description, sprite, tags, link));
        increaseTab();
    }

    public void deploymentNodeEnd() {
        decreaseTab();
        println("}");
    }

    public void container(String alias, String label, String technology, String description, String sprite, String tags, String link, String shape) {
        println(format("Container($alias=\"%s\", $label=\"%s\", $techn=\"%s\", $descr=\"%s\", $sprite=\"%s\", $tags=\"%s\", $link=\"%s\", $baseShape=\"%s\")", alias, label, technology, description, sprite, tags, link, shape));
    }

    public void withoutPropertyHeader() {
        println("WithoutPropertyHeader()");
    }

    public void addProperty(String col1) {
        println(format("AddProperty(\"%s\")", col1));
    }

    public void addProperty(String col1, String col2) {
        println(format("AddProperty(\"%s\",\"%s\")", col1, col2));
    }

    public void addProperty(String col1, String col2, String col3) {
        println(format("AddProperty(\"%s\",\"%s\",\"%s\")", col1, col2, col3));
    }

    public void addProperty(String col1, String col2, String col3, String col4) {
        println(format("AddProperty(\"%s\",\"%s\",\"%s\",\"%s\")", col1, col2, col3, col4));
    }

    public void addRelation(String aliasFrom, String aliasTo, String description, String technology, String tags, String link) {
        addRelation("Rel", aliasFrom, aliasTo, description, technology, tags, link);
    }

    public void addRelation(Direction direction, String aliasFrom, String aliasTo, String description, String technology, String tags, String link) {
        addRelation(direction.getRelationKeyword(), aliasFrom, aliasTo, description, technology, tags, link);
    }

    protected void addRelation(String command, String aliasFrom, String aliasTo, String description, String technology, String tags, String link) {
        println(format("%s(\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\")", command, aliasFrom, aliasTo, description, technology, tags, link));
    }

    public void hideStereotypes() {
        println("hide stereotypes");
    }

    protected String format(String format, Object... args) {
        var copy = new Object[args.length];
        System.arraycopy(args, 0, copy, 0, args.length);

        for (int i = 0; i < args.length; i++) {
            if (copy[i] == null) {
                copy[i] = "";
            }
        }
        return new Formatter().format(format, copy).toString();
    }
}