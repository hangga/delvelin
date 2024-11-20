package io.github.hangga.delvelin.properties;

public enum OutputFormat {
    HTML("html"),
    JSON("json"),
    LOG("log");

    private final String extension;

    OutputFormat(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
