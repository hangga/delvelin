package io.github.hangga.delvelin.properties;

public enum OutputFileFormat {
    HTML("html"),
    JSON("json"),
    LOG("log");

    private final String extension;

    OutputFileFormat(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
