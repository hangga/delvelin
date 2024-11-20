package io.github.hangga.delvelin.detectors;

import java.nio.file.Path;

import io.github.hangga.delvelin.properties.Config;
import io.github.hangga.delvelin.properties.OutputFormat;
import io.github.hangga.delvelin.properties.Vulnerabilities;
import io.github.hangga.delvelin.utils.Reports;
import io.github.hangga.delvelin.utils.SourceSet;

public abstract class BaseDetector {

    String className;
    String extName;
    Vulnerabilities vulnerabilities;

    public abstract void detect(String line, int lineNumber);

    public abstract void detect(String content);

    public void setPath(Path path) {
        String pathStr = path.toString();

        extName = pathStr.substring(pathStr.lastIndexOf('.'));

        if (Config.outputFormat == OutputFormat.HTML || Config.outputFormat == OutputFormat.JSON) {
            className = pathStr;
        } else {
            if (pathStr.contains("src")) {
                pathStr = "..."+pathStr.substring(pathStr.indexOf("src"));
            }
            className = pathStr.replace(SourceSet.SEP, ".")
                .replaceAll("(.*)\\.[^.]+$", "$1")
                .replaceAll("(.*)\\.(.*)$", "$1($2");
        }
    }

    String specificLocation(int lineNumber) {
        return (Config.outputFormat == OutputFormat.HTML || Config.outputFormat == OutputFormat.JSON) ? className + ":" + lineNumber : className + extName + ":" + lineNumber;
    }

    void setValidVulnerability(String specificLocation, String finding, String message) {
        Reports.addToReport(vulnerabilities.getCweCode(), finding, vulnerabilities.getDescription(), vulnerabilities.getCvssScore(), specificLocation, message);
    }

}
