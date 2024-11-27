package io.github.hangga.delvelin.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
            return finding;
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

        //        public double getScore() {
        //            return score;
        //        }

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
        double totalScore = 0.0;

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

                //                subtotalScore += itemReport.getScore();
            }

            //            totalScore += subtotalScore;

            //            logMessage.append(indentation)
            //                .append(Ansi.BLUE)
            //                .append("Subtotal: ")
            //                .append(String.format("%.2f", subtotalScore))
            //                .append(Ansi.RESET)
            //                .append("\n\n");
        }

        //        logMessage.append(Ansi.GREEN)
        //            .append(indentation)
        //            .append(String.format("Total Score: %,.2f", totalScore))
        //            .append(Ansi.RESET)
        //            .append("\n\n");

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

    // Method to determine priority based on CVSS score
    //    private static String getPriorityBasedOnScore(double cvssScore) {
    //        if (cvssScore >= 9.0) {
    //            return "Critical";
    //        } else if (cvssScore >= 7.0) {
    //            return "High";
    //        } else if (cvssScore >= 4.0) {
    //            return "Medium";
    //        } else {
    //            return "Low";
    //        }
    //    }

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

        Map<String, Integer> cweCounts = new HashMap<>();
        //        Map<String, Double> highestCvss = new HashMap<>();
        Map<String, String> priorities = new HashMap<>();
        //        double totalScore = 0;

        for (Map.Entry<String, List<ItemReport>> entry : groupedReports.entrySet()) {
            String cweCode = entry.getKey();
            List<ItemReport> reports = entry.getValue();

            //            double subtotalScore = calculateSubtotalScore(reports);
            //            double highestScore = calculateHighestScore(reports);
            String vulnerabilityName = reports.get(0)
                .getVulnerabilityName();

            String priority = reports.get(0)
                .getPriority();

            cweCounts.put(cweCode, reports.size());
            //            highestCvss.put(cweCode, highestScore);
            //            String priority = getPriorityBasedOnScore(highestScore);
            //            String priority = getPriorityBasedOnScore(highestScore);
            priorities.put(cweCode, priority);

            appendCweSection(htmlBuffer, cweCode, vulnerabilityName, reports, priority);
            //            totalScore += subtotalScore;
        }

//        buildSummaryTable(htmlBuffer, cweCounts, priorities);
        //        buildSummaryTable(htmlBuffer, cweCounts, highestCvss, priorities, totalScore);
        buildPieChart(htmlBuffer, cweCounts, priorities);
        buildBarChart(htmlBuffer, cweCounts, priorities);

        if (Config.isShowSaveDialog) {
            FileUtil.saveOutputCustom(htmlBuffer.toString(), ".html");
        } else {
            FileUtil.saveOutputFile(htmlBuffer.toString(), ".html");
        }
    }

    private static void buildHtmlHeader(StringBuffer htmlBuffer, String dateReport) {
        htmlBuffer.append("<html><head>")
            .append("<style>")
            .append("body { background-color: #f4f7fc; margin: 0; padding: 100px; }")
            .append("h2 { color: #979CAB; }")
            .append("h2 { text-align: center; }")
            .append("h4 { text-align: center; }")
            .append(
                "pre{display:block;font-family:monospace;white-space:pre-wrap;background-color:#fbf4db;border:1px solid #fde7bb;border-radius:4px;padding:10px;margin:10px 0;color:#aa5486;overflow-x:auto;max-width:100%;word-wrap:break-word}")
            .append("hr { border: 1px solid #979CAB; margin-top: 20px; }")
            .append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }")
            .append("th, td { padding: 4px; text-align: left; border: 1px solid #ddd; }")
            .append("th { background-color: white; }")
            .append("code{display:inline-block;font-family:monospace;background-color:#FBF4DB;border:1px solid #FDE7BB;border-radius:3px;padding:2px 5px;" +
                "margin:0 2px;color:#AA5486;white-space:nowrap}")
            .append("</style>")
            .append("</head><body>")
            .append("<h2>Vulnerability Report</h2>")
            .append("<p style=\"text-align: center;\">")
            .append(dateReport)
            .append("</p>")
            .append("<hr/>")
            .append("<div>")
            .append(
                "<p>We use <span class=\"highlight\"><a target=\"_blank\" href=\"https://cwe.mitre.org/data/slices/699.html\">CWE (Common Weakness Enumeration)</a></span> as the standard for categorizing and reporting vulnerabilities. This ensures that identified issues are described using a globally recognized taxonomy, making it easier to understand, track, and address them effectively.</p>")
            .append(
                "<p>For scoring vulnerabilities, we adhere to the <span class=\"highlight\"><a target=\"_blank\" href=\"https://www.first.org/cvss/calculator/3.0\">CVSS (Common Vulnerability Scoring System)</a></span>. This system provides a consistent way to assess the severity of vulnerabilities, enabling developers and security teams to prioritize their efforts based on the potential impact on their systems.</p>")
            .append(
                "<p><strong>Disclaimer:</strong> While this approach may not be entirely accurate in identifying every possible vulnerability, it provides a helpful starting point to secure your code. Ultimately, vigilance and proactive measures play a crucial role in maintaining robust security.</p>")
            .append("</div>")
            .append("<hr/>");
    }

    //    private static double calculateSubtotalScore(List<ItemReport> reports) {
    //        return reports.stream()
    //            .mapToDouble(ItemReport::getScore)
    //            .sum();
    //    }
    //
    //    private static double calculateHighestScore(List<ItemReport> reports) {
    //        return reports.stream()
    //            .mapToDouble(ItemReport::getScore)
    //            .max()
    //            .orElse(0.0);
    //    }

    private static void appendCweSection(StringBuffer htmlBuffer, String cweCode, String vulnerabilityName, List<ItemReport> reports, String priority) {
        htmlBuffer.append("<style>")
            .append("details summary:hover {")
            .append("    background-color: #6A669D;") // Warna saat hover
            .append("    color: #FFFFFF;")          // Warna teks saat hover
            .append("}")
            .append("</style>");

        htmlBuffer.append("<details>")
            .append(
                "<summary style='cursor: pointer; margin: 4px 0; padding: 10px 20px; font-size: 16px; font-weight: bold; border-radius: " + "6px; text-align:" +
                    " left; box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1); background-color:#979CAB;")
            .append("transition: all 0.3s ease; border: 1px solid #ddd; position: relative;'>")
            .append("<span style='display:inline;'>")
            .append(cweCode)
            .append(" - ")
            .append(vulnerabilityName)
            .append("</span>")
            .append("<span style='box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1); font-size: 14px;  border-radius: 6px; display: block; " +
                "padding: 6px; position: absolute; right: " + "20px; top: 50%; " + "transform: translateY(-50%); " + "background-color:")
            .append(getPriorityColor(priority))
            .append("; '> ")
            .append(reports.size())
            .append(" issues </span>")
            .append("</summary>");

        // Add Table CSS (Optional)
        htmlBuffer.append("<style>")
            .append("table { padding: 10px; width: 100%; border-collapse: collapse; margin-bottom: 40px; margin-top:5px;}")
            .append("th, td { border: 1px solid #D1D2D4; padding-left: 8px; padding-right: 8px; padding-top:2px; padding-bottom:2px; text-align: left; " +
                "word-wrap: break-word; word-break: " + "break-word;}")
            .append("td:nth-child(1) { width: 40%; }")
            .append("td:nth-child(3) { width: 10%; text-align: right; }")
            .append("td:nth-child(4) { width: 10%; text-align: right; }")
            .append("td:nth-child(5) { width: 8%; }")
            .append("</style>");

        // Add Table Content
        htmlBuffer.append("<table>")
            .append("<tr>")
            .append("<th>Issues</th>")
            .append("<th>Location</th>")
            //            .append("<th style=\"text-align: center;\">Score</th>")
            .append("</tr>");

        //        double totalScore = 0.0;
        int idx = 0;
        for (ItemReport item : reports) {
            //            double score = item.getScore();
            //            totalScore += score;
            idx++;
            htmlBuffer.append("<tr>")
                .append("<td>")
                .append(
                    "<span style='border: 1px solid #AE445A; min-width: 15px; text-align:center; position:absolute; background-color:#FFF; color: #000; font-size: 12px;  " +
                        "border-radius: 10px; display: " + "block; " + "padding:2px; '>")
                .append(idx)
                .append("</span>")
                .append("<pre>")
                .append(trimTitle(item.getFinding()))
                .append("</pre>")
                .append(item.getMessage())
                .append("</td>")
                .append("<td><a target=\"_blank\" href=\"")
                .append(removeTrail(item.getSpecificLocation()))
                .append("\">")
                .append(item.getSpecificLocation())
                .append("</a></td>")
                //                .append("<td>")
                //                .append(String.format("%.2f", score))
                //                .append("</td>")
                .append("</tr>");
        }

        // Add Subtotal Row
        htmlBuffer
//            .append("<tr style=\"font-weight: bold; background-color: #f9f9f9;\">")
//            .append("<td colspan=\"2\" style=\"text-align: right;\">Subtotal:</td>")
//            //            .append("<td style=\"text-align: right;\">")
//            //            .append(String.format("%.2f", totalScore))
//            //            .append("</td>")
//            .append("</tr>")
            .append("</table>");

        // Close details tag
        htmlBuffer.append("</details>");
    }

    private static void buildSummaryTable(StringBuffer htmlBuffer, Map<String, Integer> cweCounts, Map<String, String> priorities) {
        htmlBuffer.append("<h3 style=\"text-align: center;\">Summary</h3>")
            //            .append("<table><tr><th>CWE Code</th><th>Total Found</th><th>CVSS Score</th><th>Subtotal</th><th>Priority</th></tr>");
            .append("<table><tr><th>CWE Code</th><th>Total Found</th><th>Priority</th></tr>");

        int totalFindings = 0;
        for (String cweCode : cweCounts.keySet()) {
            int findings = cweCounts.get(cweCode);
            //            double cvss = highestCvss.get(cweCode);
            String priority = priorities.get(cweCode);

            totalFindings += findings;

            htmlBuffer.append("<tr><td><a target=\"_blank\" href=\"https://cwe.mitre.org/data/definitions/")
                .append(cweCode.replace("CWE-", ""))
                .append(".html\">")
                .append(cweCode)
                .append("</a></td>")
                .append("<td>")
                .append(findings)
                .append("</td>")
                //                .append("<td>")
                //                .append(String.format("%.2f", cvss))
                //                .append("</td>")
                //                .append("<td>")
                //                .append(String.format("%.2f", cvss * findings))
                //                .append("</td>")
                .append("<td style='background-color:")
                .append(getPriorityColor(priority))
                .append(";'>")
                .append(priority)
                .append("</td></tr>");
        }

        htmlBuffer.append("<tr style='font-weight:bold;'>")
            .append("<td>Total</td>")
            .append("<td>")
            .append(totalFindings)
            .append("</td>")
            .append("<td></td>")
            //            .append("<td>")
            //            .append(String.format("%.2f", totalScore))
            //            .append("</td>")
            //            .append("<td>-</td></tr>")
            .append("</table>");
    }

    private static void buildPieChart(StringBuffer htmlBuffer, Map<String, Integer> cweCounts, Map<String, String> priorities) {
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

        htmlBuffer.append("<h3 style=\"text-align: center;\">Issues Distribution</h3>")
            .append("<div style=\"width: 100%; height: 400px;\"><canvas style=\"margin:auto;\" id='findingsChart'></canvas></div>")
            .append("<script src='https://cdn.jsdelivr.net/npm/chart.js'></script>")
            .append("<script>")
            .append("const ctx = document.getElementById('findingsChart').getContext('2d');")
            .append("new Chart(ctx, { type: 'pie', data: { labels: [")
            .append(cweLabels)
            .append("], ")
            .append("datasets: [{ data: [")
            .append(cweData)
            .append("], backgroundColor: ")
            .append(chartColors)
            .append(", hoverOffset: 4 }] } });")
            .append("</script>");
    }

    private static void buildBarChart(StringBuffer htmlBuffer, Map<String, Integer> cweCounts, Map<String, String> priorities) {
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

        htmlBuffer.append("<div style=\"width: 100%; height: 800px;\"><canvas style=\"margin:auto;\" id='findingsBarChart'></canvas></div>")
            .append("<script src='https://cdn.jsdelivr.net/npm/chart.js'></script>")
            .append("<script>")
            .append("const barCtx = document.getElementById('findingsBarChart').getContext('2d');")
            .append("new Chart(barCtx, {")
            .append("type: 'bar',")
            .append("data: { labels: [")
            .append(cweLabels)
            .append("], datasets: [{ label: 'Findings', data: [")
            .append(cweData)
            .append("], backgroundColor: ")
            .append(chartColors)
            .append(", borderWidth: 1 }] },")
            .append("options: {")
            .append("indexAxis: 'y',") // Membuat grafik horizontal
            .append("scales: { x: { beginAtZero: true } },")
            .append("plugins: { legend: { display: false } }")
            .append("}")
            .append("});")
            .append("</script>");
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
        //        double totalScore = 0.0; // Total score for all CWE groups

        for (Map.Entry<String, List<ItemReport>> entry : groupedReports.entrySet()) {
            if (!isFirstCwe) {
                jsonBuilder.append(",");
            }

            String cweCode = entry.getKey();
            List<ItemReport> reports = entry.getValue();

            //            double subtotalScore = calculateSubtotalScore(reports);
            //            totalScore += subtotalScore; // Add to the overall total score
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
