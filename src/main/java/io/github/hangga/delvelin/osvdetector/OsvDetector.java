package io.github.hangga.delvelin.osvdetector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.hangga.delvelin.cwedetectors.BaseDetector;
import io.github.hangga.delvelin.osvdetector.OsvVulnerabilityParser.OsvVulnerability;
import io.github.hangga.delvelin.services.ApiServices;

/**
 * <a href="https://google.github.io/osv.dev/">Introduction to OSV</a>
 */

public class OsvDetector extends BaseDetector {

    List<OsvVulnerability> vulns;

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
        return ".gradle".equals(this.extName) || ".xml".equals(this.extName) || ".kts".equals(this.extName);
    }

    void checkOnOsv(String packageName, String version) {
        String requestBody = String.format("{\"package\": {\"ecosystem\": \"Maven\", \"name\": \"%s\"}, \"version\": \"%s\"}", packageName, version);

        HttpURLConnection connection = null;
        try {
            connection = new ApiServices().createHttp("https://api.osv.dev/v1/query", requestBody);
            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
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
        } catch (IOException e) {
//            e.printStackTrace();
        }

        if (connection != null) connection.disconnect();
    }

    private void handleSuccessfulResponse(String packageName, String version, String response) {
        try {
            vulns = new OsvVulnerabilityParser().parseJson(response)
                .getVulns();
            for (OsvVulnerability vuln : vulns) {
                String msg = vuln.getDetails() + " <a target=\"_blank\" href=" + vuln.getReferences()
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
        String regex = "";
        if (extName.equalsIgnoreCase(".gradle") || extName.equalsIgnoreCase(".kts")) {
            regex = "dependencies\\s*\\{([^}]*)}";
        } else if (extName.equalsIgnoreCase(".xml")) {
            regex = "<dependency>(.*?)</dependency>";
        }
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
        if (extName.equalsIgnoreCase(".gradle") || extName.equalsIgnoreCase(".kts")) {
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
                    } catch (Exception ignored) {
                    }
                }
            }
        } else if (extName.equalsIgnoreCase(".xml")) {
            String groupIdRegex = "<groupId>(.*?)</groupId>";
            String artifactIdRegex = "<artifactId>(.*?)</artifactId>";
            String versionRegex = "<version>(.*?)</version>";

            String groupId = extractFirstMatch(dependenciesBlock, groupIdRegex);
            String artifactId = extractFirstMatch(dependenciesBlock, artifactIdRegex);
            String version = extractFirstMatch(dependenciesBlock, versionRegex);

            String packageName = groupId + ":" + artifactId; // group:artifact

            try {
                checkOnOsv(packageName, version);
            } catch (Exception ignored) {
            }
        }
    }

    private String extractFirstMatch(String content, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
