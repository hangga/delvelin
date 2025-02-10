package io.github.hangga.delvelin.utils;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;

import io.github.hangga.delvelin.properties.Config;

public class FileUtil {

    private static final Path basePath = Paths.get(System.getProperty("user.dir"));

    public static Stream<Path> getStream() throws IOException {
        return Files.walk(basePath);
    }

    public static Path getBasePath() {
        return basePath;
    }

    public static String extract(Path path) throws IOException {
        return new String(Files.readAllBytes(path));
    }

//    public static void saveOutputFile(String content, String extName) {
//        Path path = FileUtil.getBasePath();
//        Path outputPath = Paths.get(path.toString(), "vulnerability-report" + extName);
//        try {
//            Files.write(outputPath, content.getBytes());
//            if (Desktop.isDesktopSupported() && extName.equalsIgnoreCase(".html") && Config.isAutoLaunchBrowser) {
//                Desktop.getDesktop()
//                    .browse(outputPath.toUri());
//            } else {
//                System.out.println("Report saved: file://" + outputPath.toAbsolutePath());
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to save HTML report to file: " + path, e);
//        }
//    }

    public static void saveOutputFile(String content, String extName) {
        Path basePath;

        if (Files.exists(Paths.get("target", "surefire-reports"))) {
            basePath = Paths.get("target", "surefire-reports"); // Maven Surefire (Unit Test)
        } else if (Files.exists(Paths.get("target", "failsafe-reports"))) {
            basePath = Paths.get("target", "failsafe-reports"); // Maven Failsafe (Integration Test)
        } else if (Files.exists(Paths.get("build", "reports", "tests", "test"))) {
            basePath = Paths.get("build", "reports", "tests", "test"); // Gradle JUnit
        } else {
            basePath = FileUtil.getBasePath(); // Fallback jika tidak ditemukan
        }

        saveFile(content, extName, basePath);
    }

    private static void saveFile(String content, String extName, Path basePath) {
        try {
            Files.createDirectories(basePath);
            Path outputPath = basePath.resolve("vulnerability-report" + extName);
            Files.write(outputPath, content.getBytes());

            if (Desktop.isDesktopSupported() && extName.equalsIgnoreCase(".html") && Config.isAutoLaunchBrowser) {
                Desktop.getDesktop().browse(outputPath.toUri());
            } else {
                System.out.println("Report saved: file://" + outputPath.toAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save report to file: " + basePath, e);
        }
    }


    public static void saveOutputCustom(String content, String extName) {
        JFileChooser fileChooser = getJFileChooser(extName);
        // Show save dialog and get the user's choice
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Ensure file has the correct extension
            String filePath = selectedFile.getAbsolutePath();
            if (!filePath.endsWith(extName)) {
                filePath += extName;
            }

            try {
                // Save the file
                Files.write(Paths.get(filePath), content.getBytes());
                System.out.println("Report saved: file://" + new File(filePath).getAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException("Failed to save report to file: " + filePath, e);
            }
        } else {
            System.out.println("Save operation was cancelled.");
        }
    }

    private static @NotNull JFileChooser getJFileChooser(String extName) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Vulnerability Report");

        // Set file extension filter (HTML or JSON)
        if (".html".equals(extName)) {
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("HTML Files", "html"));
        } else if (".json".equals(extName)) {
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JSON Files", "json"));
        }

        // Set default file name
        fileChooser.setSelectedFile(new File("vulnerability-report" + extName));
        return fileChooser;
    }

    public static String loadHtmlFromResource(Class className, String resourcePath) throws IOException {
        try (InputStream inputStream = className.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new FileNotFoundException("Resource not found: " + resourcePath);
            }
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }
            return content.toString();
        }
    }


}

