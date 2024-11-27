package io.github.hangga.delvelin.cwedetectors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.hangga.delvelin.properties.Vulnerabilities;

public class WeakCryptographicDetector extends BaseDetector {

    public WeakCryptographicDetector() {
        this.vulnerabilities = Vulnerabilities.WEAK_CRYPTO;
    }

    private static class WeakAlgorithm {

        final String regex;
        final String safeAlternative;

        WeakAlgorithm(String regex, String safeAlternative) {
            this.regex = regex;
            this.safeAlternative = safeAlternative;
        }
    }

    private final WeakAlgorithm[] weakAlgorithms = { new WeakAlgorithm("(?i)MessageDigest\\s*\\.\\s*getInstance\\s*\\(\\s*\"MD5\"\\s*\\)", "SHA-256 or SHA-3"),
        new WeakAlgorithm("(?i)MessageDigest\\s*\\.\\s*getInstance\\s*\\(\\s*\"SHA-1\"\\s*\\)", "SHA-256 or SHA-3"),
        new WeakAlgorithm("(?i)Cipher\\s*\\.\\s*getInstance\\s*\\(\\s*\"DES\"\\s*\\)", "AES with key size of 128-bit or higher"),
        new WeakAlgorithm("(?i)Cipher\\s*\\.\\s*getInstance\\s*\\(\\s*\"DESede\"\\s*\\)", "AES with key size of 128-bit or higher"),
        new WeakAlgorithm("(?i)Cipher\\s*\\.\\s*getInstance\\s*\\(\\s*\"RC4\"\\s*\\)", "AES-GCM or ChaCha20"),
        new WeakAlgorithm("(?i)Cipher\\s*\\.\\s*getInstance\\s*\\(\\s*\"Blowfish\"\\s*\\)", "AES or ChaCha20"),
        new WeakAlgorithm("(?i)KeyPairGenerator\\s*\\.\\s*getInstance\\s*\\(\\s*\"RSA\"\\s*\\)\\s*;.*?\\.initialize\\s*\\(\\s*(512|1024|1536)\\s*\\)",
            "RSA with 2048-bit or higher, or ECC with at least 256-bit"),
        new WeakAlgorithm("(?i)KeyPairGenerator\\s*\\.\\s*getInstance\\s*\\(\\s*\"DSA\"\\s*\\)\\s*;.*?\\.initialize\\s*\\(\\s*(512|1024|1536)\\s*\\)",
            "RSA 2048-bit or ECC 256-bit (e.g., ECDSA)"),
        new WeakAlgorithm("(?i)Mac\\s*\\.\\s*getInstance\\s*\\(\\s*\"HmacMD5\"\\s*\\)", "HMAC-SHA-256 or HMAC-SHA-3") };

    @Override
    public void detect(String lines, int lineNumber) {
        for (WeakAlgorithm weakAlgorithm : weakAlgorithms) {
            Pattern pattern = Pattern.compile(weakAlgorithm.regex);
            Matcher matcher = pattern.matcher(lines);

            if (matcher.find()) {
                setValidVulnerability(specificLocation(lineNumber), lines, "Consider using " + weakAlgorithm.safeAlternative);
            }
        }
    }

    @Override
    public void detect(String content) {

    }
}
