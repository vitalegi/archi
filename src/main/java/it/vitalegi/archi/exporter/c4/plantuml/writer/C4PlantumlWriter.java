package it.vitalegi.archi.exporter.c4.plantuml.writer;

import it.vitalegi.archi.exporter.c4.plantuml.constants.Direction;
import it.vitalegi.archi.model.diagramelement.C4DiagramElement;
import it.vitalegi.archi.model.diagramelement.C4DiagramRelation;
import it.vitalegi.archi.model.element.Element;
import it.vitalegi.archi.model.relation.DirectRelation;
import it.vitalegi.archi.model.style.ElementTag;
import it.vitalegi.archi.model.style.LineStyle;
import it.vitalegi.archi.model.style.RelationTag;

import java.util.Formatter;
import java.util.List;

public class C4PlantumlWriter extends PlantumlWriter {

    public void addElementTag(ElementTag tag) {
        addElementTag(tag.getAlias(), tag.getBackgroundColor(), tag.getFontColor(), tag.getBorderColor(), tag.getShadowing(), tag.getShape(), tag.getSprite(), tag.getTechnologies(), tag.getLegendText(), tag.getLegendSprite(), tag.getBorderStyle(), tag.getBorderThickness());
    }

    private void addElementTag(String alias, String bgColor, String fontColor, String borderColor, Boolean shadowing, String shape, String sprite, String techn, String legendText, String legendSprite, LineStyle borderStyle, Integer borderThickness) {
        println(format("AddElementTag(\"%s\", $bgColor=\"%s\", $fontColor=\"%s\", $borderColor=\"%s\", $shadowing=\"%b\", $shape=\"%s\", $sprite=\"%s\", $techn=\"%s\", $legendText=\"%s\", $legendSprite=\"%s\", $borderStyle=\"%s\", $borderThickness=\"%d\")", //
                alias, bgColor, fontColor, borderColor, shadowing, shape, sprite, techn, legendText, legendSprite, borderStyle != null ? borderStyle.name() : null, borderThickness));
    }

    public void addRelTag(RelationTag tag) {
        addRelTag(tag.getAlias(), tag.getTextColor(), tag.getLineColor(), tag.getLineStyle(), tag.getSprite(), tag.getTechnologies(), tag.getLegendText(), tag.getLegendSprite(), tag.getLineThickness());
    }

    private void addRelTag(String alias, String textColor, String lineColor, LineStyle lineStyle, String sprite, String techn, String legendText, String legendSprite, Integer lineThickness) {
        println(format("AddRelTag(\"%s\", $textColor=\"%s\", $lineColor=\"%s\", $lineStyle=\"%s\", $sprite=\"%s\", $techn=\"%s\", $legendText=\"%s\", $legendSprite=\"%s\", $lineThickness=\"%s\")", //
                alias, textColor, lineColor, lineStyle != null ? lineStyle : null, sprite, techn, legendText, legendSprite, lineThickness));
    }

    public void deploymentNodeStart(Element element) {
        deploymentNodeStart(getAlias(element), element.getName(), null, element.getDescription(), "", formatTags(element), "");
    }
    public void deploymentNodeStart(C4DiagramElement element) {
        deploymentNodeStart(getAlias(element), element.getName(), null, element.getDescription(), "", formatTags(element), "");
    }

    protected void deploymentNodeStart(String alias, String label, String type, String description, String sprite, String tags, String link) {
        println(format("Deployment_Node(%s, %s, $type=\"%s\", $descr=\"%s\", $sprite=\"%s\", $tags=\"%s\", $link=\"%s\") {", alias, label, type, description, sprite, tags, link));
        increaseTab();
    }

    public void deploymentNodeEnd() {
        decreaseTab();
        println("}");
    }

    public void boundaryStart(C4DiagramElement element) {
        boundaryStart(getAlias(element), element.getName(), formatTags(element));
    }

    protected void boundaryStart(String alias, String label, String tags) {
        println(format("Boundary(%s, \"%s\", $tags=\"%s\") {", alias, label, tags));
        increaseTab();
    }

    public void boundaryEnd() {
        decreaseTab();
        println("}");
    }

    public void container(Element element) {
        container(getAlias(element), element.getName(), null, element.getDescription(), "", formatTags(element), "", "");
    }

    public void container(C4DiagramElement element) {
        container(getAlias(element), element.getName(), null, element.getDescription(), "", formatTags(element), "", "");
    }

    protected void container(String alias, String label, String technology, String description, String sprite, String tags, String link, String shape) {
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

    public void addRelation(DirectRelation relation) {
        addRelation("Rel", relation);
    }

    public void addRelation(Direction direction, DirectRelation relation) {
        addRelation(direction.getRelationKeyword(), relation);
    }

    public void addRelation(String command, DirectRelation relation) {
        addRelation(command, getAlias(relation.getFrom()), getAlias(relation.getTo()), relation.getLabel(), relation.getTechnologies(), relation.getDescription(), relation.getSprite(), formatTags(relation), relation.getLink());
    }

    public void addRelation(C4DiagramRelation relation) {
        addRelation("Rel", relation.getFromAlias(), relation.getToAlias(), relation.getLabel(), relation.getTechnology(), relation.getDescription(), relation.getSprite(), formatTags(relation), relation.getLink());
    }

    protected void addRelation(String command, String aliasFrom, String aliasTo, String label, String technology, String description, String sprite, String tags, String link) {
        //$from, $to, $label, $techn="", $descr="", $sprite="", $tags="", $link=""
        println(format("%s($from=\"%s\", $to=\"%s\", $label=\"%s\", $techn=\"%s\", $descr=\"%s\", $sprite=\"%s\", $tags=\"%s\", $link=\"%s\")", //
                command, aliasFrom, aliasTo, label, technology, description, sprite, tags, link));
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

    protected String getAlias(Element element) {
        return getAlias(element.getUniqueId());
    }

    protected String getAlias(C4DiagramElement element) {
        return getAlias(element.getId());
    }

    protected String getAlias(String id) {
        id = id.replace('-', '_');
        id = id.replace('.', '_');
        id = id.replace(' ', '_');
        return id;
    }

    protected String formatTags(Element element) {
        return formatTags(element.getTags());
    }

    protected String formatTags(C4DiagramElement element) {
        return formatTags(element.getTags());
    }

    protected String formatTags(DirectRelation relation) {
        return formatTags(relation.getTags());
    }

    protected String formatTags(C4DiagramRelation relation) {
        return formatTags(relation.getTags());
    }


    protected String formatTags(List<String> tags) {
        if (tags == null) {
            return null;
        }
        return String.join("&", tags);
    }
}