package net.cavitos.phantom.domain;

public class Capture {

    private String title;
    private String source;

    public Capture(String title, String source) {
        this.title = title;
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public String getSource() {
        return source;
    }
}
