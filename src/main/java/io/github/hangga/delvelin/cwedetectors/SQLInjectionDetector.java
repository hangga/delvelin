package io.github.hangga.delvelin.cwedetectors;

import java.util.regex.Pattern;

import io.github.hangga.delvelin.properties.Vulnerabilities;

public class SQLInjectionDetector extends BaseDetector {

    boolean hasSafeQuery = false;

    private static final Pattern SQLI_PATTERN = Pattern.compile("(?i)(select\\s+.*\\s+from|insert\\s+into\\s+.*|update\\s+.*\\s+set|delete\\s+from)\\s+.*(['\"].*['\"]|\\+|concat|or\\s+1\\s*=\\s*1|--|#)");
    private static final Pattern VULN_INPUT_PATTERN = Pattern.compile(".*(request\\.getParameter|scanner\\.nextLine).*");

    public SQLInjectionDetector() {
        this.vulnerabilities = Vulnerabilities.SQL_INJECTION;
    }

    @Override
    public void detect(String line, int lineNumber) {
        if (!this.extName.equals(".kt") && !this.extName.equals(".java")) {
            return;
        }
        if ((isCommonPatternMatch(line) || isVulnerableInput(line)) && !hasSafeQuery) {
            setValidVulnerability(specificLocation(lineNumber), line, "");
        }
    }

    @Override
    public void detect(String content) {
        hasSafeQuery = isSafeQuery(content);
    }

    /*
    match common regex patterns
     */
    private boolean isCommonPatternMatch(String line) {
        return SQLI_PATTERN.matcher(line)
            .find();
    }

    /*
    whether the data in the query comes from an unvalidated source.
     */
    private boolean isVulnerableInput(String line) {
        return VULN_INPUT_PATTERN.matcher(line).find() && line.toLowerCase()
            .contains("select");
    }

    /*
    does it contain safe queries
     */
    private boolean isSafeQuery(String content) {
        return content.contains("PreparedStatement");
    }
}
