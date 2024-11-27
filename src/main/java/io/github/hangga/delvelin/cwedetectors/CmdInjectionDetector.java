package io.github.hangga.delvelin.cwedetectors;

import java.util.regex.Pattern;

import io.github.hangga.delvelin.properties.Vulnerabilities;

public class CmdInjectionDetector extends BaseDetector {

    private static final Pattern CMD_PATTERN = Pattern.compile("Runtime\\.getRuntime\\(\\)\\.exec\\(\\s*\".*\"\\s*\\+.*\\);");

    public CmdInjectionDetector() {
        this.vulnerabilities = Vulnerabilities.COMMAND_INJECTION;
    }

    @Override
    public void detect(String line, int lineNumber) {
        if (!this.extName.equals(".kt") && !this.extName.equals(".java")) {
            return;
        }
        if (CMD_PATTERN.matcher(line)
            .find()) {
            setValidVulnerability(specificLocation(lineNumber), line, "");
        }
    }

    @Override
    public void detect(String content) {

    }
}
