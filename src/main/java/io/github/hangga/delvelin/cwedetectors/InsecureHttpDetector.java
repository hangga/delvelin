package io.github.hangga.delvelin.cwedetectors;

import java.net.MalformedURLException;
import java.net.URL;
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

        if (line.contains("HttpURLConnection") ||
            HTTP_URL_PATTERN.matcher(line).find() ||
            containsHttpUrl(line)) {
            setValidVulnerability(specificLocation(lineNumber), line, "Weak SSL Context configuration. Ensure SSLContext is configured securely.");
        }
    }

    @Override
    public void detect(String content) {

    }

    private boolean containsHttpUrl(String line) {
        String[] words = line.split("\\s+"); // Pecah berdasarkan spasi
        for (String word : words) {
            try {
                URL url = new URL(word);
                if ("http".equalsIgnoreCase(url.getProtocol())) {
                    return true;
                }
            } catch (MalformedURLException ignored) {
            }
        }
        return false;
    }
}
