package net.cavitos.phantom.domain;

public class Capture {

    private String title;
    private String captureId;
    private String fileName;

    public Capture(String title, String captureId, String fileName) {
        this.title = title;
        this.captureId = captureId;
        this.fileName = fileName;
    }

    public String getTitle() {
        return title;
    }

    public String getCaptureId() {
        return captureId;
    }

    public String getFileName() {
        return fileName;
    }
}
