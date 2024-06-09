package it.vitalegi.archi.diagram.writer;

import it.vitalegi.archi.plantuml.LayoutDirection;

public class PlantUMLWriter extends Writer {

    public void startuml() {
        println("@startuml");
    }

    public void enduml() {
        println("@enduml");
    }

    public void set(String name, String value) {
        print("set ");
        print(name);
        print(" ");
        println(value);
    }

    public void title(String title) {
        print("title ");
        println(title);
    }

    public void direction(LayoutDirection direction) {
        println(direction.getDirection());
    }

    public void include(String include) {
        print("!include ");
        println(include);
    }

    public void skinParam(String key, String value) {
        println("skinparam " + key + " " + value);
    }
}
