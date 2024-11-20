package io.github.hangga.delvelin.detectors;

import java.util.regex.Pattern;

import io.github.hangga.delvelin.properties.Vulnerabilities;

public class InsecureHttpDetector extends BaseDetector {

    private static final Pattern HTTP_URL_PATTERN = Pattern.compile("(?i)(http://[a-zA-Z0-9._%-]+)");

    public InsecureHttpDetector() {
        this.vulnerabilities = Vulnerabilities.HTTP_NO_SSL;
    }

    @Override
    public void detect(String line, int lineNumber) {
        if (!this.extName.equals(".kt") && !this.extName.equals(".java")) {
            return;
        }
        if (line.contains("HttpURLConnection") && HTTP_URL_PATTERN.matcher(line)
            .find()) {
            setValidVulnerability(specificLocation(lineNumber), line, "Insecure HTTP detected");
        }
    }

    @Override
    public void detect(String content) {

    }
}
