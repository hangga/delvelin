package io.github.hangga.delvelin.properties;

public enum Vulnerabilities {
    SQL_INJECTION("SQL Injection", "CWE-89", 9.8),
    COMMAND_INJECTION("Command Injection", "CWE-77", 9.8),
    XSS("Cross-Site Scripting (XSS)", "CWE-79", 6.1),
    INSECURE_DESERIALIZATION("Insecure Deserialization", "CWE-502", 8.0),
    UNSAFE_STRINGBUILDER("Unsafe StringBuilder Usage in Multithread", "CWE-362", 5.5),
    HARDCODED_SECRETS("Hardcoded Secrets and Credentials", "CWE-798", 7.5),
    HTTP_NO_SSL("HTTP Connection without SSL/TLS", "CWE-319", 7.4),
    WEAK_CRYPTO("Weak Cryptographic Algorithms", "CWE-327", 5.6),
    UNSAFE_OBJECT_CLONE("Unsafe Object.clone() Usage", "CWE-374", 4.3),
    WEAK_SESSION_MANAGEMENT("Weak Session Management", "CWE-384", 6.5),
    INADEQUATE_AUTHENTICATION("Inadequate Authentication", "CWE-306", 7.5),
    RACE_CONDITION("Race Condition in Shared Resources", "CWE-362", 5.5),
    RESOURCE_LEAK("Resource Leak (Unclosed Resources)", "CWE-775", 4.3),
    XXE_INJECTION("XML External Entity (XXE) Injection", "CWE-611", 7.5),
    UNSAFE_REFLECTION("Unsafe Use of Reflection", "CWE-470", 6.5),
    NON_ATOMIC("Assignment to Variable without Proper Synchronization", "CWE-563", 5.5),
    CONSIDER_COROUTINES("Non-Adherence to Coding Standards (Consider using coroutines instead of threads in Kotlin)", "CWE-710", 2.0),
    NON_THREAD_SAFE_DATA_STRUCTURE("Concurrent Execution using Shared Resource with Improper Synchronization (Race Condition)", "CWE-362", 6.5);

    private final String description;
    private final String cweCode;
    private final double cvssScore;

    public String getDescription() {
        return description;
    }

    public String getCweCode() {
        return cweCode;
    }

    Vulnerabilities(String description, String cweCode, double cvssScore) {
        this.description = "Potential " + description;
        this.cweCode = cweCode;
        this.cvssScore = cvssScore;
    }

    public double getCvssScore() {
        return cvssScore;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", description, cweCode);
        //  return String.format("%s (%s) - CVSS Score: %.1f", description, cweCode, cvssScore);
    }
}


