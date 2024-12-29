package io.github.hangga.delvelin.cwedetectors;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import io.github.hangga.delvelin.osvdetector.OsvDetector;
import io.github.hangga.delvelin.properties.Config;
import io.github.hangga.delvelin.utils.FileUtil;

public class GeneralScanner {

    private final List<BaseDetector> detectors = Arrays.asList(
        new ThreadDetector(),
        //        new NonAtomicDetector(),
        new HardCodedSecretDetector(),
        new NonThreadSafeDetector(),
        new XSSDetector(),
        new SQLInjectionDetector(),
        new CmdInjectionDetector(),
        new WeakCryptographicDetector(),
        new InsecureHttpDetector(),
        new OsvDetector()
        // add new detector here
    );

    private boolean shouldSkipLine(String line) {
        return line.startsWith("import") || line.startsWith("public void") || line.startsWith("private void") || line.startsWith("protected void") ||
            line.startsWith("void") || line.endsWith("{");
    }

    private boolean skipComment(String line) {
        return line.startsWith("//") || line.startsWith("/*") || line.startsWith("/***");
    }

    public void scan(Path path) {
        String content;
        try {
            content = FileUtil.extract(path);
            String[] lines = content.split("\\r?\\n");

            setPath(path); // Set path for all detectors in one call

            for (int lineNumber = 0; lineNumber < lines.length; lineNumber++) {
                String line = lines[lineNumber].trim();

                if (shouldSkipLine(line) || (skipComment(line) && Config.ignoreCommentBlock)) {
                    continue;
                }

                detectByLine(line, lineNumber + 1); // Line-by-line analysis
            }

            if (skipComment(content) && Config.ignoreCommentBlock) {
                return;
            }

            detectByContent(content); // Full content detections for XSS
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void detectByLine(String line, int lineNumber) {
        detectors.forEach(detector -> detector.detect(line, lineNumber));
    }

    private void detectByContent(String content) {
        detectors.forEach(detector -> detector.detect(content));
    }

    private void setPath(Path path) {
        detectors.forEach(detector -> detector.setPath(path));
    }
}

