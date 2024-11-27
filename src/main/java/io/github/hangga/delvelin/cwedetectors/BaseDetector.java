package io.github.hangga.delvelin.cwedetectors;

import java.nio.file.Path;

import io.github.hangga.delvelin.properties.Config;
import io.github.hangga.delvelin.properties.OutputFileFormat;
import io.github.hangga.delvelin.properties.Vulnerabilities;
import io.github.hangga.delvelin.utils.Reports;
import io.github.hangga.delvelin.utils.SourceSet;

public abstract class BaseDetector {

    public String className;
    public String extName;
    public Vulnerabilities vulnerabilities;

    public abstract void detect(String line, int lineNumber);

    public abstract void detect(String content);

    public void setPath(Path path) {
        String pathStr = path.toString();

        extName = pathStr.substring(pathStr.lastIndexOf('.'));

        if (Config.outputFileFormat == OutputFileFormat.HTML || Config.outputFileFormat == OutputFileFormat.JSON) {
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

    public String specificLocation(int lineNumber) {
        return (Config.outputFileFormat == OutputFileFormat.HTML || Config.outputFileFormat == OutputFileFormat.JSON) ? className + ":" + lineNumber : className + extName + ":" + lineNumber;
    }

    public void setValidVulnerability(String cweCode, String desc, String specificLocation, String finding, String message, String priority) {
        Reports.addToReport(cweCode, finding, desc, specificLocation,
            message, priority);
    }

    public void setValidVulnerability(String specificLocation, String finding, String message) {
        Reports.addToReport(vulnerabilities.getCweCode(), finding, vulnerabilities.getDescription(), specificLocation,
            message, vulnerabilities.getPriority());
    }

}
