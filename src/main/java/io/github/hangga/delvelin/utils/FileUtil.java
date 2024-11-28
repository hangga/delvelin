package io.github.hangga.delvelin.utils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;

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

    public static void saveOutputFile(String content, String extName) {
        Path path = FileUtil.getBasePath();
        Path outputPath = Paths.get(path.toString(), "vulnerability-report" + extName);
        try {
            Files.write(outputPath, content.getBytes());
            if (Desktop.isDesktopSupported() && extName.equalsIgnoreCase(".html")) {
                Desktop.getDesktop()
                    .browse(outputPath.toUri());
            } else {
                System.out.println("Report saved: file://" + outputPath.toAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save HTML report to file: " + path, e);
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

}

