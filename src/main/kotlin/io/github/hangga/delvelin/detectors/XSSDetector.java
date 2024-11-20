package io.github.hangga.delvelin.detectors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.hangga.delvelin.properties.Vulnerabilities;

public class XSSDetector extends BaseDetector {

    public XSSDetector() {
        this.vulnerabilities = Vulnerabilities.XSS;
    }

    @Override
    public void detect(String content) {
        String regex = "(request\\.getParameter\\([\"'][a-zA-Z0-9_]+[\"']\\))|(@RequestParam\\s*\\([\"'][a-zA-Z0-9_]+[\"']\\))";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        // Membagi string lines menjadi array baris
        String[] lineArray = content.split("\n");

        while (matcher.find()) {
            // Menghitung nomor baris
            int matchPosition = matcher.start();
            int currentLine = 1;  // Baris pertama dimulai dari 1
            int characterCount = 0;

            for (String line : lineArray) {
                characterCount += line.length() + 1;  // +1 untuk karakter newline
                if (characterCount > matchPosition) {
                    break;
                }
                currentLine++;
            }
            setValidVulnerability(specificLocation(currentLine), matcher.group(),"");
        }
    }

    @Deprecated
    @Override
    public void detect(String lines, int lineNUmber) {
        String regex = "(request\\.getParameter\\([\"'][a-zA-Z0-9_]+[\"']\\))|(@RequestParam\\s*\\([\"'][a-zA-Z0-9_]+[\"']\\))";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(lines);

        if (matcher.find()) {
            setValidVulnerability(specificLocation(lineNUmber), lines, "");
        }
    }

}
