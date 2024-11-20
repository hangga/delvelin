package io.github.hangga.delvelin.detectors;

import java.util.regex.Pattern;

import io.github.hangga.delvelin.properties.Vulnerabilities;

public class NonAtomicDetector extends BaseDetector {

    private static final Pattern NUMERIC_VARIABLE_PATTERN = Pattern.compile(
        "^(private|protected|public)?\\s*(final|var|val|lateinit)?\\s*(int|double|float|long|short|byte)\\s+\\w+\\s*(=\\s*[^;]*)?;?\\s*$");

    public NonAtomicDetector() {
        this.vulnerabilities = Vulnerabilities.NON_ATOMIC;
    }

    @Override
    public void detect(String line, int lineNumber) {
        if (!this.extName.equals(".kt") && !this.extName.equals(".java")) {
            return;
        }
        if (NUMERIC_VARIABLE_PATTERN.matcher(line)
            .matches() && !line.toLowerCase()
            .contains("string")) {
            setValidVulnerability(specificLocation(lineNumber), line, "");
        }
    }

    @Override
    public void detect(String content) {

    }
}
