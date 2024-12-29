package io.github.hangga.delvelin.cwedetectors;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.hangga.delvelin.properties.Vulnerabilities;

public class HardCodedSecretDetector extends BaseDetector {

    String msg = "Warning: Hardcoded secrets or credentials found in the source code. Hardcoding sensitive information such as passwords, tokens, " +
        "and API keys can expose secrets and increase the risk of data leaks.";

    private static final Pattern KEYWORD_PATTERN = Pattern.compile(
        "(password|pwd|passwd|pass|user_password|user_pwd|api_key|apikey|access_token|auth_token|token|session_token|oauth_token|authorization|authkey|client_id|client_secret|access_key|api_secret|secret_key|api_token|jwt_token|jwt_secret|sessionid|oauth_client|refresh_token|secret|key|private_key|public_key|ssh_key|rsa_key|dsa_key|ecdsa_key|x509_key|p12_key|pfx_key|ssl_key|tls_key|encryption_key|secret_key_base|secret_access_key|security_key|symmetric_key|access_key_id|cred|credential|credentials|username|user|auth|authenticator|authentication|login|userid|user_id|user_token|pin|card_number|card_no|cc_number|cvv|cvc|ssn|social_security|license_key|account_number|bank_account|routing_number|mfa_secret|2fa_secret)",
        Pattern.CASE_INSENSITIVE);
    List<String> sensitiveKeys = Arrays.asList("password", "pwd", "passwd", "pass", "user_password", "user_pwd", "api_key", "apikey", "access_token",
        "auth_token", "token", "session_token", "oauth_token", "authorization", "authkey", "client_id", "client_secret", "access_key", "api_secret",
        "secret_key", "api_token", "jwt_token", "jwt_secret", "sessionid", "oauth_client", "refresh_token", "secret", "key", "private_key", "public_key",
        "ssh_key", "rsa_key", "dsa_key", "ecdsa_key", "x509_key", "p12_key", "pfx_key", "ssl_key", "tls_key", "encryption_key", "secret_key_base",
        "secret_access_key", "security_key", "symmetric_key", "access_key_id", "cred", "credential", "credentials", "username", "user", "auth", "authenticator",
        "authentication", "login", "userid", "user_id", "user_token", "pin", "card_number", "card_no", "cc_number", "cvv", "cvc", "ssn", "social_security",
        "license_key", "account_number", "bank_account", "routing_number", "mfa_secret", "2fa_secret");

    public HardCodedSecretDetector() {
        this.vulnerabilities = Vulnerabilities.HARDCODED_SECRETS;
    }

    @Override
    public void detect(String input, int lineNumber) {
        if (isCommonPatternMatch(input, lineNumber) || isContainSensitiveKey(input)) {
            setValidVulnerability(specificLocation(lineNumber), input.trim(), msg);
        }
    }

    private final List<String> ignoredKeys = Arrays.asList(
        "passwordEncoder", "tokenType", "authService", "secretProvider"
    );

    private boolean isContainSensitiveKey(String line) {
        for (String key : sensitiveKeys) {
            if (line.toLowerCase().contains(key.toLowerCase())) {
                if (ignoredKeys.stream().noneMatch(line::contains)) { // Pastikan bukan ignored key
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCommonPatternMatch(String line, int lineNumber) {
        boolean found = false;
        Matcher matcher = KEYWORD_PATTERN.matcher(line);

        while (matcher.find()) {
            int equalIndex = line.indexOf("=", matcher.end());

            if (equalIndex != -1) {
                int startQuote = line.indexOf("\"", equalIndex);
                int endQuote = line.indexOf("\"", startQuote + 1);

                if (startQuote != -1 && endQuote != -1) {
                    found = true;
                }
            }
        }
        return found;
    }

    @Override
    public void detect(String content) {

    }
}
