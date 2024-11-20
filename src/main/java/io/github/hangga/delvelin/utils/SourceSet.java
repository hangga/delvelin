package io.github.hangga.delvelin.utils;

import java.nio.file.FileSystems;
import java.util.Arrays;
import java.util.List;

public class SourceSet {

    public static final String SEP = FileSystems.getDefault()
        .getSeparator();

    public void setExtensions(String... extensions) {
        this.extensions = extensions;
    }

    private String[] extensions = { ".java", ".kt", ".kts", ".gradle" };

    public String[] getExtensions() {
        return extensions;
    }

    public List<String> getSourceSets() {
        return Arrays.asList("src" + SEP + "test" + SEP + "java" + SEP, "src" + SEP + "test" + SEP + "kotlin" + SEP, "src" + SEP + "main" + SEP + "java" + SEP,
            "src" + SEP + "main" + SEP + "kotlin" + SEP, "src" + SEP + "integrationTest" + SEP + "java" + SEP,
            "src" + SEP + "integrationTest" + SEP + "kotlin" + SEP, "src" + SEP + "functionalTest" + SEP + "java" + SEP,
            "src" + SEP + "functionalTest" + SEP + "kotlin" + SEP, "src" + SEP + "benchmark" + SEP + "java" + SEP,
            "src" + SEP + "benchmark" + SEP + "kotlin" + SEP, "src" + SEP + "uiTest" + SEP + "java" + SEP, "src" + SEP + "uiTest" + SEP + "kotlin" + SEP,
            "src" + SEP + "test" + SEP + "resources" + SEP, "src" + SEP + "main" + SEP + "resources" + SEP, "src" + SEP // Jika ada file di root src
        );
    }
}
