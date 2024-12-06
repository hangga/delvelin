package io.github.hangga.delvelin.utils;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.time.LocalDate;
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

    private static class ItemFile {

        String specificLocation;
        String ext;

        public ItemFile(String specificLocation, String ext, String className) {
            this.specificLocation = specificLocation;
            this.ext = ext;
            this.className = className;
        }

        String className;

        public String getSpecificLocation() {
            return specificLocation;
        }

        public String getExt() {
            return ext;
        }

        public String getClassName() {
            return className;
        }
    }

    public static void detect(String specificLocation, String ext, String className){
        itemFiles.add(new ItemFile(specificLocation, ext, className));
    }

    private static class ItemReport {

        String cweCode;
        String vulnerabilityName;
        String finding;
        String specificLocation;
        String ext;
        String className;

        public void setClassName(String className) {
            this.className = className;
        }

        public String getClassName() {
            return className;
        }

        public void setExt(String ext) {
            this.ext = ext;
        }

        public String getExt() {
            return ext;
        }

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
    static CopyOnWriteArraySet<ItemFile> itemFiles = new CopyOnWriteArraySet<>();

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM, dd - HH:mm:ss");

    private static String formattedDate() {
        return LocalDateTime.now()
            .format(formatter);
    }

    public static void addToReport(String cweCode, String finding, String vulnerabilityName, String location, String message, String priority, String className,
        String ext) {
        ItemReport newItem = new ItemReport(cweCode, finding, vulnerabilityName, location, message, priority);
        newItem.setExt(ext);
        newItem.setClassName(className);

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
        if (input.length() <= 50) {
            return input;
        }
        return input.substring(0, 50);
    }

    public static String trimLink(String input) {

        String sep = FileSystems.getDefault()
            .getSeparator();
        String[] parts = input.split(String.valueOf(sep));

        // Jika lebih dari 3 bagian, ambil 3 terakhir dan tambahkan '...'
        if (parts.length > 4) {
            StringBuilder result = new StringBuilder("   ...");
            // Ambil tiga bagian terakhir
            for (int i = parts.length - 4; i < parts.length; i++) {
                result.append(parts[i]);
                if (i < parts.length - 1) {
                    result.append(sep); // Menambahkan separator antar bagian
                }
            }
            return result.toString();
        } else {
            return input; // Jika jumlah bagian 3 atau kurang, kembalikan string asli
        }
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

        StringBuffer htmlBuffer = new StringBuffer();
        buildHtmlHeader(htmlBuffer);

        Map<String, Long> fileStats = countFilesByExtensionFromReports();
        buildFileStatsBarChart(htmlBuffer, fileStats);

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
        buildBarChart(htmlBuffer, cweCounts, priorities);
        buildPieChart(htmlBuffer, cweCounts, priorities);
//        buildBarChart(htmlBuffer, cweCounts, priorities);

        appendFooter(htmlBuffer);

        if (Config.isShowSaveDialog) {
            FileUtil.saveOutputCustom(htmlBuffer.toString(), ".html");
        } else {
            FileUtil.saveOutputFile(htmlBuffer.toString(), ".html");
        }
    }

    private static void buildHtmlHeader(StringBuffer htmlBuffer) {
        try {
            htmlBuffer.append(loadResource("html/header.html").replace("${VERSION}", Config.VERSION));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //    private static void buildFileStatsBarChart(StringBuffer htmlBuffer, Map<String, Long> fileStats) {
    //        // Persiapan data untuk labels dan data
    //        String fileLabels = fileStats.keySet()
    //            .stream()
    //            .map(ext -> "\"" + ext + "\"")
    //            .collect(Collectors.joining(","));
    //        String fileData = fileStats.values()
    //            .stream()
    //            .map(String::valueOf)
    //            .collect(Collectors.joining(","));
    //
    //        // Warna default jika tidak ada prioritas
    //        String chartColors = "[\"#4CAF50\", \"#FFC107\", \"#F44336\", \"#2196F3\", \"#9C27B0\"]"; // Kamu bisa tambahkan logika warna jika dibutuhkan
    //
    //        try {
    //            String template = loadResource("html/filestats-bar-chart.html");
    //
    //            // Mengganti placeholder dengan data aktual
    //            String populatedHtml = template.replace("${STAT_LABELS}", fileLabels)
    //                .replace("${STAT_DATA}", fileData)
    //                .replace("${STAT_CHART_COLORS}", chartColors);
    //
    //            // Menambahkan hasil ke htmlBuffer
    //            htmlBuffer.append(populatedHtml);
    //        } catch (IOException e) {
    //            throw new RuntimeException("Failed to load or process bar chart template", e);
    //        }
    //    }

    private static void buildFileStatsBarChart(StringBuffer htmlBuffer, Map<String, Long> fileStats) {
        // Hitung total nilai
        long totalFiles = fileStats.values()
            .stream()
            .mapToLong(Long::longValue)
            .sum();

        // Persiapkan data HTML untuk bar chart
        String barItems = fileStats.entrySet()
            .stream()
            .map(entry -> {
                String ext = entry.getKey();
                long count = entry.getValue();
                double percentage = (double) count / totalFiles * 100;

                // Pilih warna berdasarkan ekstensi file
                String color = getBarColor(ext);

                // Buat elemen HTML untuk satu bar
                return String.format("<li style=\"margin-bottom: 8px; display: flex; align-items: center;\">" +
                    "<span style=\"width: 80px; text-align: right; padding-right: 10px; font-weight: bold;\">%s</span>" +
                    "<div style=\"flex: 1; height: 18px; background-color: #ddd; border-radius: 4px; overflow: hidden; margin: 0 10px;\">" +
                    "<div style=\"width: %.1f%%; height: 100%%; background-color: %s; border-radius: 4px;\"></div>" + "</div>" +
                    "<span style=\"width: 50px; text-align: left;\">%.1f%%</span>" + "</li>", ext, percentage, color, percentage);
            })
            .collect(Collectors.joining("\n"));

        try {
            String template = loadResource("html/filestats-bar-chart.html");

            // Gantikan placeholder dengan data aktual
            String populatedHtml = template.replace("${BAR_ITEMS}", barItems)
                .replace("${ROOT_DIR}", getDirName())
                .replace("${ROOT_PATH}","Path: "+FileUtil.getBasePath().toString());

            // Tambahkan hasil ke htmlBuffer
            htmlBuffer.append(populatedHtml);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load or process progress bar template", e);
        }
    }

    /**
     * Mendapatkan warna untuk setiap ekstensi file.
     * @param ext Ekstensi file.
     * @return Warna dalam format hex.
     */
    private static String getBarColor(String ext) {
        switch (ext) {
            case ".java":
                return "#B07219";
            case ".kt":
                return "#8B5DFF";
            case ".xml":
                return "#F44336";
            case ".properties":
                return "#2196F3";
            case ".json":
                return "#9C27B0";
            default:
                return "#757575"; // Warna default
        }
    }

    private static String loadResource(String resourcePath) throws IOException {
        return FileUtil.loadHtmlFromResource(Reports.class, resourcePath);
    }

    private static void appendCweSection(StringBuffer htmlBuffer, String cweCode, String vulnerabilityName, List<ItemReport> reports, String priority) {
        try {
            String cweTemplate = loadResource("html/cwe-section.html");
            String rowTemplate = loadResource("html/report-row.html");

            StringBuilder reportRows = new StringBuilder();
            String lastMessage = "";

            for (ItemReport item : reports) {
                String finding = item.getFinding()
                    .trim()
                    .isEmpty() ? "" : "<pre>" + trimTitle(item.getFinding()) + "</pre>";
                String message = item.getMessage();
                String specificLocation = removeTrail(item.getSpecificLocation());

                // Memeriksa apakah pesan sama dengan pesan terakhir
                if (message.equals(lastMessage)) {
                    message = "";  // Jika pesan sama, kosongkan pesan
                } else {
                    lastMessage = message;  // Jika berbeda, perbarui pesan terakhir
                }

                String trimedLink = trimLink(specificLocation);
                // Mengganti placeholder dengan data aktual
                String populatedRow = rowTemplate.replace("${FINDING}", finding)
                    .replace("${MESSAGE}", message.isEmpty() ? "" : "<div class='msg'>" + message + "</div>")
                    .replace("${SPECIFIC_LOCATION}", specificLocation)
                    .replace("${TITLE_SPECIFIC_LOCATION}", trimedLink);

                reportRows.append(populatedRow);
            }

            // Replace placeholders
            String populatedHtml = cweTemplate.replace("${CWE_CODE}", cweCode)
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
            String populatedHtml = template.replace("${CWE_LABELS}", cweLabels)
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
            String populatedHtml = template.replace("${CWE_LABELS_BAR}", cweLabels)
                .replace("${CWE_DATA_BAR}", cweData)
                .replace("${CHART_COLORS_BAR}", chartColors);

            // Menambahkan hasil ke htmlBuffer
            htmlBuffer.append(populatedHtml);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load or process bar chart template", e);
        }
    }

    private static void appendFooter(StringBuffer htmlBuffer) {
        try {
            String footerTemplate = loadResource("html/footer.html");

            String populatedFooter = footerTemplate.replace("${TOOL_NAME}", "Delveline")
                .replace("${TOOL_VERSION}", Config.VERSION)
                .replace("${GENERATION_DATE}", getCurrentDateTime())
                .replace("${TOOL_URL}", "https://delvelin.github.io")
                .replace("${CURRENT_YEAR}", String.valueOf(LocalDate.now()
                    .getYear()))
                .replace("${COPYRIGHT_HOLDER}", "Delveline Team");

            htmlBuffer.append(populatedFooter);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load or process footer template", e);
        }
    }

    private static String getCurrentDateTime() {
        return LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
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

    static String getDirName() {
        String path = FileUtil.getBasePath()
            .toString();
        return path.substring(path.lastIndexOf(SourceSet.SEP) + 1);
    }

    //    String getDirName(){
    //        String path = String.valueOf(FileUtil.getBasePath());
    //        String[] end = path.split(SourceSet.SEP);
    //        return end[end.length - 1];
    //    }

    //    public static void generateJson() {
    //        if (itemReports.isEmpty()) {
    //            return;
    //        }
    //
    //        // Grouping reports by CWE Code
    //        Map<String, List<ItemReport>> groupedReports = itemReports.stream()
    //            .collect(Collectors.groupingBy(ItemReport::getCweCode));
    //
    //        StringBuilder jsonBuilder = new StringBuilder();
    //        jsonBuilder.append("{");
    //
    //        String dateReport = formattedDate();
    //        jsonBuilder.append("\"date\": \"")
    //            .append(dateReport)
    //            .append("\",");
    //
    //        jsonBuilder.append("\"projectRoot\": \"")
    //            .append(getDirName())
    //            .append("\",");
    //
    //        jsonBuilder.append("\"vulnerabilities\": [");
    //
    //        boolean isFirstCwe = true;
    //
    //        for (Map.Entry<String, List<ItemReport>> entry : groupedReports.entrySet()) {
    //            if (!isFirstCwe) {
    //                jsonBuilder.append(",");
    //            }
    //
    //            String cweCode = entry.getKey();
    //            List<ItemReport> reports = entry.getValue();
    //
    //            String vulnerabilityName = reports.get(0)
    //                .getVulnerabilityName();
    //
    //            String priority = reports.get(0)
    //                .getPriority();
    //
    //            jsonBuilder.append("{");
    //            jsonBuilder.append("\"cweCode\": \"")
    //                .append(cweCode)
    //                .append("\",");
    //            jsonBuilder.append("\"vulnerabilityName\": \"")
    //                .append(vulnerabilityName)
    //                .append("\",");
    //            jsonBuilder.append("\"priority\": \"")
    //                .append(priority)
    //                .append("\",");
    //            jsonBuilder.append("\"issues\": [");
    //
    //            boolean isFirstFinding = true;
    //            for (ItemReport item : reports) {
    //                if (!isFirstFinding) {
    //                    jsonBuilder.append(",");
    //                }
    //
    //                jsonBuilder.append("{");
    //                jsonBuilder.append("\"issues\": \"")
    //                    .append(escapeJson(item.getFinding()))
    //                    .append("\",");
    //                jsonBuilder.append("\"message\": \"")
    //                    .append(escapeJson(item.getMessage()))
    //                    .append("\",");
    //                jsonBuilder.append("\"location\": \"")
    //                    .append(escapeJson(item.getSpecificLocation()))
    //                    .append("\"");
    //                jsonBuilder.append("}");
    //                isFirstFinding = false;
    //            }
    //
    //            jsonBuilder.append("]");
    //            jsonBuilder.append("}");
    //            isFirstCwe = false;
    //        }
    //
    //        jsonBuilder.append("]");
    //        jsonBuilder.append("}");
    //
    //        if (Config.isShowSaveDialog) {
    //            FileUtil.saveOutputCustom(jsonBuilder.toString(), ".json");
    //        } else {
    //            FileUtil.saveOutputFile(jsonBuilder.toString(), ".json");
    //        }
    //    }

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
            .append("\",\n");

        jsonBuilder.append("\"projectRoot\": \"")
            .append(getDirName())
            .append("\",\n");

        // Adding file extension statistics
        Map<String, Long> fileStats = countFilesByExtensionFromReports();
        jsonBuilder.append("\"fileExtensions\": {\n");
        boolean isFirstExt = true;
        for (Map.Entry<String, Long> entry : fileStats.entrySet()) {
            if (!isFirstExt) {
                jsonBuilder.append(",\n");
            }
            jsonBuilder.append("\"")
                .append(entry.getKey())
                .append("\": ")
                .append(entry.getValue());
            isFirstExt = false;
        }
        jsonBuilder.append("\n},\n");

        jsonBuilder.append("\"vulnerabilities\": [\n");

        boolean isFirstCwe = true;

        for (Map.Entry<String, List<ItemReport>> entry : groupedReports.entrySet()) {
            if (!isFirstCwe) {
                jsonBuilder.append(",\n");
            }

            String cweCode = entry.getKey();
            List<ItemReport> reports = entry.getValue();

            String vulnerabilityName = reports.get(0)
                .getVulnerabilityName();

            String priority = reports.get(0)
                .getPriority();

            jsonBuilder.append("{\n");
            jsonBuilder.append("\"cweCode\": \"")
                .append(cweCode)
                .append("\",\n");
            jsonBuilder.append("\"vulnerabilityName\": \"")
                .append(vulnerabilityName)
                .append("\",\n");
            jsonBuilder.append("\"priority\": \"")
                .append(priority)
                .append("\",\n");
            jsonBuilder.append("\"issues\": [\n");

            boolean isFirstFinding = true;
            for (ItemReport item : reports) {
                if (!isFirstFinding) {
                    jsonBuilder.append(",\n");
                }

                jsonBuilder.append("{\n");
                jsonBuilder.append("\"issues\": \"")
                    .append(escapeJson(item.getFinding()))
                    .append("\",\n");
                jsonBuilder.append("\"message\": \"")
                    .append(escapeJson(item.getMessage()))
                    .append("\",\n");
                jsonBuilder.append("\"location\": \"")
                    .append(escapeJson(item.getSpecificLocation()))
                    .append("\"\n");
                jsonBuilder.append("}");
                isFirstFinding = false;
            }

            jsonBuilder.append("\n]");
            jsonBuilder.append("}");
            isFirstCwe = false;
        }

        jsonBuilder.append("\n]");
        jsonBuilder.append("}");

        if (Config.isShowSaveDialog) {
            FileUtil.saveOutputCustom(jsonBuilder.toString(), ".json");
        } else {
            FileUtil.saveOutputFile(jsonBuilder.toString(), ".json");
        }
    }

//    private static Map<String, Long> countFilesByExtensionFromReports() {
//        return itemReports.stream()
//            .map(ItemReport::getExt)
//            .filter(ext -> ext != null && !ext.isEmpty())
//            .collect(Collectors.groupingBy(ext -> ext, Collectors.counting()));
//    }

    private static Map<String, Long> countFilesByExtensionFromReports() {
        return itemFiles.stream()
            .map(ItemFile::getExt)
            .filter(ext -> ext != null && !ext.isEmpty())
            .collect(Collectors.groupingBy(ext -> ext, Collectors.counting()));
    }

    private static String escapeJson(String input) {
        return input.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r");
    }
}