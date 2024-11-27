package io.github.hangga.delvelin.properties;

public enum Vulnerabilities {
    SQL_INJECTION("SQL Injection", "CWE-89", "Critical".toUpperCase().toUpperCase()),
    COMMAND_INJECTION("Command Injection", "CWE-77", "Critical".toUpperCase()),
    XSS("Cross-Site Scripting (XSS)", "CWE-79", "MODERATE"),
    INSECURE_DESERIALIZATION("Insecure Deserialization", "CWE-502", "High".toUpperCase()),
    UNSAFE_STRINGBUILDER("Unsafe StringBuilder Usage in Multithread", "CWE-362", "MODERATE"),
    HARDCODED_SECRETS("Hardcoded Secrets and Credentials", "CWE-798", "High".toUpperCase()),
    HTTP_NO_SSL("HTTP Connection without SSL/TLS", "CWE-319", "High".toUpperCase()),
    WEAK_CRYPTO("Weak Cryptographic Algorithms", "CWE-327", "MODERATE"),
    UNSAFE_OBJECT_CLONE("Unsafe Object.clone() Usage", "CWE-374", "MODERATE"),
    WEAK_SESSION_MANAGEMENT("Weak Session Management", "CWE-384", "MODERATE"),
    INADEQUATE_AUTHENTICATION("Inadequate Authentication", "CWE-306", "High".toUpperCase()),
    RACE_CONDITION("Race Condition in Shared Resources", "CWE-362", "MODERATE"),
    RESOURCE_LEAK("Resource Leak (Unclosed Resources)", "CWE-775", "MODERATE"),
    XXE_INJECTION("XML External Entity (XXE) Injection", "CWE-611", "High".toUpperCase()),
    UNSAFE_REFLECTION("Unsafe Use of Reflection", "CWE-470", "MODERATE"),
    NON_ATOMIC("Assignment to Variable without Proper Synchronization", "CWE-563", "MODERATE"),
    CONSIDER_COROUTINES("Non-Adherence to Coding Standards (Consider using coroutines instead of threads in Kotlin)", "CWE-710", "Low".toUpperCase()),
    NON_THREAD_SAFE_DATA_STRUCTURE("Concurrent Execution using Shared Resource with Improper Synchronization (Race Condition)", "CWE-362", "MODERATE");

    private final String description;
    private final String cweCode;
    private final String priority;

    public String getPriority() {
        return priority;
    }

    public String getDescription() {
        return description;
    }

    public String getCweCode() {
        return cweCode;
    }

    Vulnerabilities(String description, String cweCode, String priority) {
        this.description = "Potential " + description;
        this.cweCode = cweCode;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", description, cweCode);
    }
}


