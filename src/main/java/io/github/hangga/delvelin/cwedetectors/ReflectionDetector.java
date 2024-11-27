package io.github.hangga.delvelin.cwedetectors;

import java.util.regex.Pattern;

import io.github.hangga.delvelin.properties.Vulnerabilities;

public class ReflectionDetector extends BaseDetector {

    private static final Pattern REFLECTION_PATTERN = Pattern.compile(
        "(?i)(Class\\.forName\\(|getMethod\\(|getDeclaredMethod\\(|setAccessible\\(|newInstance\\()");

    public ReflectionDetector() {
        this.vulnerabilities = Vulnerabilities.UNSAFE_REFLECTION;
    }

    @Override
    public void detect(String line, int lineNumber) {
        if (!this.extName.equals(".kt") && !this.extName.equals(".java")) {
            return;
        }
        if (REFLECTION_PATTERN.matcher(line)
            .find()) {
            setValidVulnerability(specificLocation(lineNumber),  line, "Potential Reflection isuses");
        }
    }

    @Override
    public void detect(String content) {

    }
}
