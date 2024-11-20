package io.github.hangga.delvelin.detectors;

import io.github.hangga.delvelin.properties.Vulnerabilities;

public class ThreadDetector extends BaseDetector {

    public ThreadDetector() {
        this.vulnerabilities = Vulnerabilities.CONSIDER_COROUTINES;
    }

    @Override
    public void detect(String line, int lineNumber) {
        if (!this.extName.equals(".kt")) {
            return;
        }
        if (line.toLowerCase()
            .contains("thread")) {
            setValidVulnerability(specificLocation(lineNumber), line, "");
        }
    }

    @Override
    public void detect(String content) {

    }
}
