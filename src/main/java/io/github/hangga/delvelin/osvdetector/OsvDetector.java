package io.github.hangga.delvelin.osvdetector;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

import io.github.hangga.delvelin.cwedetectors.BaseDetector;
import io.github.hangga.delvelin.osvdetector.OsvVulnerabilityParser.OsvVulnerability;

/**
 * <a href="https://google.github.io/osv.dev/">Introduction to OSV</a>
 */

public class OsvDetector extends BaseDetector {

    @Override
    public void detect(String line, int lineNumber) {
    }

    @Override
    public void detect(String content) {
        if (!isSupportedFile()) {
            return;
        }
        detectDependencies(content);
    }

    private boolean isSupportedFile() {
        return ".gradle".equals(this.extName) || ".pom".equals(this.extName) || ".kts".equals(this.extName);
    }

    void checkOnOsv(String packageName, String version) throws Exception {
        HttpURLConnection connection = getHttpURLConnection(packageName, version);

        int statusCode = connection.getResponseCode();
        if (statusCode == HttpURLConnection.HTTP_OK) {
            try (java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line.trim());
                }
                handleSuccessfulResponse(packageName, version, response.toString());
            }
        } else {
            handleErrorResponse(statusCode, connection.getResponseMessage());
        }

        connection.disconnect();
    }

    private static @NotNull HttpURLConnection getHttpURLConnection(String packageName, String version) throws IOException {
        String requestBody = String.format("{\"package\": {\"ecosystem\": \"Maven\", \"name\": \"%s\"}, \"version\": \"%s\"}", packageName, version);

        URL url = new URL("https://api.osv.dev/v1/query");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return connection;
    }

    List<OsvVulnerabilityParser.OsvVulnerability> vulns;

    private void handleSuccessfulResponse(String packageName, String version, String response) {
        try {
            vulns = new OsvVulnerabilityParser().parseJson(response)
                .getVulns();
            for (OsvVulnerability vuln : vulns) {
                String msg = vuln.getSummary() + " <a target=\"_blank\" href=" + vuln.getReferences()
                    .get(0)
                    .getUrl() + ">" + vuln.getReferences()
                    .get(0)
                    .getUrl() + "</a>";
                setValidVulnerability(vuln.getDatabaseSpecific()
                    .getCweIds()
                    .get(0), vuln.getSummary(), specificLocation(currentLine), packageName + ":" + version, msg, vuln.getDatabaseSpecific()
                    .getSeverity());

            }
        } catch (IOException e) {
            System.out.println("TERJADI ERROR Disisi :" + e.getMessage());
        }
    }

    private void handleErrorResponse(int statusCode, String errorResponse) {
        System.out.println("Request failed with HTTP status code: " + statusCode);
        System.out.println("Error response body:");
        System.out.println(errorResponse);
    }

    int currentLine = 1;

    private void detectDependencies(String content) {
        String regex = "dependencies\\s*\\{([^}]*)}";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);

        // Cek apakah ada blok dependencies
        String[] lineArray = content.split("\n");
        while (matcher.find()) {
            String dependenciesBlock = matcher.group(1); // Isi blok dependencies
            int matchPosition = matcher.start();
            // Baris pertama dimulai dari 1
            int characterCount = 0;

            for (String line : lineArray) {
                characterCount += line.length() + 1;  // +1 untuk karakter newline
                if (characterCount > matchPosition) {
                    break;
                }
                currentLine++;
            }
            extractPackageAndVersion(dependenciesBlock);
        }
    }

    private void extractPackageAndVersion(String dependenciesBlock) {
        String packageRegex = "([\\w.-]+:[\\w.-]+:[\\w.-]+)";
        Pattern packagePattern = Pattern.compile(packageRegex);
        Matcher packageMatcher = packagePattern.matcher(dependenciesBlock);

        while (packageMatcher.find()) {
            String[] parts = packageMatcher.group(1)
                .split(":");
            if (parts.length == 3) {
                String packageName = parts[0] + ":" + parts[1]; // group:artifact
                String version = parts[2]; // version
                try {
                    checkOnOsv(packageName, version);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
