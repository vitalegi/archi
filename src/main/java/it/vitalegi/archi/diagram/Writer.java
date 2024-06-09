package it.vitalegi.archi.diagram;

public class Writer {
    private final static String TAB = "    ";
    StringBuilder sb = new StringBuilder();
    private int tabs;

    public String build() {
        return sb.toString().trim();
    }

    protected void print(String str) {
        sb.append(str);
    }

    protected void println(String str) {
        sb.append(str).append("\n");
        printTab();
    }

    protected void increaseTab() {
        tabs++;
    }

    protected void decreaseTab() {
        tabs--;
        if (tabs < 0) {
            throw new RuntimeException("Tabs is negative, order of operation is wrong");
        }
    }

    private void printTab() {
        for (int i = 0; i < tabs; i++) {
            print(TAB);
        }
    }
}
