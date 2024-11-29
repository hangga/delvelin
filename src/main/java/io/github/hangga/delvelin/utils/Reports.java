package io.github.hangga.delvelin.utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

import io.github.hangga.delvelin.properties.Ansi;
import io.github.hangga.delvelin.properties.Config;

public class Reports {

    private static class ItemReport {

        String cweCode;
        String vulnerabilityName;
        String finding;
        String specificLocation;
        String message;
        String priority;

        public String getPriority() {
            return priority;
        }

        public String getFinding() {
            return finding.trim();
        }

        public ItemReport(String cweCode, String finding, String vulnerabilityName, String specificLocation, String message, String priority) {
            this.cweCode = cweCode;
            this.finding = finding;
            this.vulnerabilityName = vulnerabilityName;
            this.specificLocation = specificLocation;
            this.message = message;
            this.priority = priority;
        }

        public String getVulnerabilityName() {
            return vulnerabilityName;
        }

        public String getCweCode() {
            return cweCode;
        }

        public String getSpecificLocation() {
            return specificLocation;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ItemReport that = (ItemReport) obj;
            return cweCode.equals(that.cweCode) && specificLocation.equals(that.specificLocation);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cweCode, specificLocation);
        }
    }

    static CopyOnWriteArraySet<ItemReport> itemReports = new CopyOnWriteArraySet<>();

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM, dd - HH:mm:ss");

    private static String formattedDate() {
        return LocalDateTime.now()
            .format(formatter);
    }

    public static void addToReport(String cweCode, String finding, String vulnerabilityName, String location, String message, String priority) {
        ItemReport newItem = new ItemReport(cweCode, finding, vulnerabilityName, location, message, priority);

        // check if exist
        for (ItemReport existingItem : itemReports) {
            if (existingItem.getCweCode()
                .equals(cweCode) && existingItem.getSpecificLocation()
                .equals(location)) {
                return;
            }
        }

        itemReports.add(newItem);
    }

    public static StringBuffer log() {
        if (itemReports.isEmpty()) {
            return null;
        }

        StringBuffer logMessage = new StringBuffer();

        String indentation = "  +------ ";
        //        double totalScore = 0.0;
        // Mengelompokkan itemReports berdasarkan CWE-Code
        Map<String, List<ItemReport>> groupedReports = itemReports.stream()
            .collect(Collectors.groupingBy(ItemReport::getCweCode));

        // Iterasi melalui setiap kelompok CWE-Code
        for (Map.Entry<String, List<ItemReport>> entry : groupedReports.entrySet()) {
            String cweCode = entry.getKey();
            List<ItemReport> reports = entry.getValue();

            // Mengambil deskripsi dari salah satu laporan (asumsi deskripsi sama dalam satu grup)
            String description = reports.get(0)
                .getVulnerabilityName();

            // Judul kelompok CWE-Code dengan deskripsi
            logMessage.append(Ansi.GREEN)
                .append(cweCode)
                .append(" - ")
                .append(description)
                .append(Ansi.RESET)
                .append("\n");

            // Menghitung subtotal score untuk kelompok ini
            //            double subtotalScore = 0.0;
            for (ItemReport itemReport : reports) {
                logMessage.append(indentation)
                    .append("Issues: ")
                    .append(Ansi.YELLOW)
                    .append(itemReport.getFinding()
                        .replace("<code>", "")
                        .replace("</code>", ""))
                    .append(Ansi.RESET)
                    .append("\n")
                    .append(indentation)
                    .append("Message: ")
                    .append(itemReport.getMessage()
                        .replace("<code>", "")
                        .replace("</code>", ""))
                    .append("\n")
                    .append(indentation)
                    .append("at ")
                    .append(itemReport.getSpecificLocation())
                    .append(")\n\n");
            }
        }
        return logMessage;
    }

    public static String trimTitle(String input) {
        if (input == null) {
            return null;
        }
        if (input.length() <= 140) {
            return input;
        }
        return input.substring(0, 140) + "...";
    }

    public static String removeTrail(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.replaceAll(":[0-9]+$", "");
    }

    public static void generateHtmlReport() {
        if (itemReports.isEmpty()) {
            return;
        }

        // Grouping reports by CWE Code
        Map<String, List<ItemReport>> groupedReports = itemReports.stream()
            .collect(Collectors.groupingBy(ItemReport::getCweCode));

        String dateReport = formattedDate();
        StringBuffer htmlBuffer = new StringBuffer();
        buildHtmlHeader(htmlBuffer, dateReport);

        Map<String, Integer> cweCounts = new ConcurrentHashMap<>();
        Map<String, String> priorities = new ConcurrentHashMap<>();

        for (Map.Entry<String, List<ItemReport>> entry : groupedReports.entrySet()) {
            String cweCode = entry.getKey();
            List<ItemReport> reports = entry.getValue();

            String vulnerabilityName = reports.get(0)
                .getVulnerabilityName();

            String priority = reports.get(0)
                .getPriority();

            cweCounts.put(cweCode, reports.size());
            priorities.put(cweCode, priority);

            appendCweSection(htmlBuffer, cweCode, vulnerabilityName, reports, priority);
        }

        buildPieChart(htmlBuffer, cweCounts, priorities);
        buildBarChart(htmlBuffer, cweCounts, priorities);

        if (Config.isShowSaveDialog) {
            FileUtil.saveOutputCustom(htmlBuffer.toString(), ".html");
        } else {
            FileUtil.saveOutputFile(htmlBuffer.toString(), ".html");
        }
    }

    private static void buildHtmlHeader(StringBuffer htmlBuffer, String dateReport) {
        String headerHtml;
        try {
            headerHtml =loadResource("html/header.html");
            headerHtml = headerHtml.replace("${dateReport}", dateReport);
            htmlBuffer.append(headerHtml);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String loadResource(String resourcePath) throws IOException {
        return FileUtil.loadHtmlFromResource(Reports.class,resourcePath);
    }

    private static void appendCweSection(StringBuffer htmlBuffer, String cweCode, String vulnerabilityName,
        List<ItemReport> reports, String priority) {
        try {
            // Load template HTML
            String cweTemplate = loadResource("html/cwe-section.html");
            String rowTemplate = loadResource("html/report-row.html");

            // Generate rows for the table
            StringBuilder reportRows = new StringBuilder();
            for (ItemReport item : reports) {
                String finding = item.getFinding().isEmpty() ? "" : "<pre>" + trimTitle(item.getFinding()) + "</pre>";
                String message = item.getMessage();
                String specificLocation = removeTrail(item.getSpecificLocation());

                // Mengganti placeholder dengan data aktual
                String populatedRow = rowTemplate
                    .replace("${FINDING}", finding)
                    .replace("${MESSAGE}", message)
                    .replace("${SPECIFIC_LOCATION}", specificLocation);

                // Menambahkan hasil ke buffer
                reportRows.append(populatedRow);
            }

            // Replace placeholders
            String populatedHtml = cweTemplate
                .replace("${CWE_CODE}", cweCode)
                .replace("${VULNERABILITY_NAME}", vulnerabilityName)
                .replace("${PRIORITY_COLOR}", getPriorityColor(priority))
                .replace("${ISSUE_COUNT}", String.valueOf(reports.size()))
                .replace("${REPORT_ROWS}", reportRows.toString());

            // Append to HTML buffer
            htmlBuffer.append(populatedHtml);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load or process HTML template", e);
        }
    }

    private static void buildPieChart(StringBuffer htmlBuffer, Map<String, Integer> cweCounts, Map<String, String> priorities) {
        // Prepare data for labels and counts
        String cweLabels = cweCounts.keySet()
                    .stream()
                    .map(cwe -> "\"" + cwe + "\"")
                    .collect(Collectors.joining(","));
                String cweData = cweCounts.values()
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
                String chartColors = priorities.values()
                    .stream()
                    .map(Reports::getPriorityColor)
                    .collect(Collectors.joining("\",\"", "[\"", "\"]"));

        try {
            // Load pie chart template
            String template = loadResource("html/pie-chart.html");

            // Replace placeholders with actual data
            String populatedHtml = template
                .replace("${CWE_LABELS}", cweLabels)
                .replace("${CWE_DATA}", cweData)
                .replace("${CHART_COLORS}", chartColors);

            // Append to HTML buffer
            htmlBuffer.append(populatedHtml);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load or process pie chart template", e);
        }
    }

    private static void buildBarChart(StringBuffer htmlBuffer, Map<String, Integer> cweCounts, Map<String, String> priorities) {
        // Persiapan data untuk labels, data, dan warna
        String cweLabels = cweCounts.keySet()
            .stream()
            .map(cwe -> "\"" + cwe + "\"")
            .collect(Collectors.joining(","));
        String cweData = cweCounts.values()
            .stream()
            .map(String::valueOf)
            .collect(Collectors.joining(","));
        String chartColors = priorities.values()
            .stream()
            .map(Reports::getPriorityColor)
            .collect(Collectors.joining("\",\"", "[\"", "\"]"));

        try {
            String template = loadResource("html/bar-chart.html");

            // Mengganti placeholder dengan data aktual
            String populatedHtml = template
                .replace("${CWE_LABELS}", cweLabels)
                .replace("${CWE_DATA}", cweData)
                .replace("${CHART_COLORS}", chartColors);

            // Menambahkan hasil ke htmlBuffer
            htmlBuffer.append(populatedHtml);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load or process bar chart template", e);
        }
    }

    private static String getPriorityColor(String priority) {
        switch (priority) {
            case "CRITICAL":
                return "#E02401";
            case "HIGH":
                return "#F98404";
            case "MODERATE":
                return "#FFCA03";
            case "LOW":
                return "#95CD41";
            default:
                return "#0000FF";
        }
    }

    public static void generateJson() {
        if (itemReports.isEmpty()) {
            return;
        }

        // Grouping reports by CWE Code
        Map<String, List<ItemReport>> groupedReports = itemReports.stream()
            .collect(Collectors.groupingBy(ItemReport::getCweCode));

        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");

        String dateReport = formattedDate();
        jsonBuilder.append("\"date\": \"")
            .append(dateReport)
            .append("\",");
        jsonBuilder.append("\"vulnerabilities\": [");

        boolean isFirstCwe = true;

        for (Map.Entry<String, List<ItemReport>> entry : groupedReports.entrySet()) {
            if (!isFirstCwe) {
                jsonBuilder.append(",");
            }

            String cweCode = entry.getKey();
            List<ItemReport> reports = entry.getValue();

            String vulnerabilityName = reports.get(0)
                .getVulnerabilityName();

            String priority = reports.get(0)
                .getPriority();

            jsonBuilder.append("{");
            jsonBuilder.append("\"cweCode\": \"")
                .append(cweCode)
                .append("\",");
            jsonBuilder.append("\"vulnerabilityName\": \"")
                .append(vulnerabilityName)
                .append("\",");
            jsonBuilder.append("\"priority\": \"")
                .append(priority)
                .append("\",");
            jsonBuilder.append("\"issues\": [");

            boolean isFirstFinding = true;
            for (ItemReport item : reports) {
                if (!isFirstFinding) {
                    jsonBuilder.append(",");
                }

                jsonBuilder.append("{");
                jsonBuilder.append("\"issues\": \"")
                    .append(escapeJson(item.getFinding()))
                    .append("\",");
                jsonBuilder.append("\"message\": \"")
                    .append(escapeJson(item.getMessage()))
                    .append("\",");
                jsonBuilder.append("\"location\": \"")
                    .append(escapeJson(item.getSpecificLocation()))
                    .append("\"");
                jsonBuilder.append("}");
                isFirstFinding = false;
            }

            jsonBuilder.append("]");
            jsonBuilder.append("}");
            isFirstCwe = false;
        }

        jsonBuilder.append("]");
        jsonBuilder.append("}");

        if (Config.isShowSaveDialog) {
            FileUtil.saveOutputCustom(jsonBuilder.toString(), ".json");
        } else {
            FileUtil.saveOutputFile(jsonBuilder.toString(), ".json");
        }
    }

    private static String escapeJson(String input) {
        return input.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r");
    }
}