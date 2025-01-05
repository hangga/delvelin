package io.github.hangga.delvelin.properties;

public enum Vulnerabilities {
    SQL_INJECTION("SQL Injection", "CWE-89", "Critical".toUpperCase()),
    COMMAND_INJECTION("Command Injection", "CWE-77", "Critical".toUpperCase()),
    XSS("Cross-Site Scripting (XSS)", "CWE-79", "MODERATE"),
    INSECURE_DESERIALIZATION("Insecure Deserialization", "CWE-502", "High".toUpperCase()),
    UNSAFE_STRINGBUILDER("Unsafe StringBuilder Usage in Multithread", "CWE-362", "MODERATE"),
    HARDCODED_SECRETS("Hardcoded Secrets and Credentials", "CWE-798", "High".toUpperCase()),
    HTTP_NO_SSL("HTTP Connection without SSL/TTLS", "CWE-319", "High".toUpperCase()),
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
    NON_THREAD_SAFE_DATA_STRUCTURE("Concurrent Execution using Shared Resource with Improper Synchronization (Race Condition)", "CWE-362", "MODERATE"),
    BUFFER_OVERFLOW("Buffer Overflow", "CWE-120", "Critical".toUpperCase()),
    INTEGER_OVERFLOW("Integer Overflow", "CWE-190", "High".toUpperCase()),
    UNRESTRICTED_UPLOAD("Unrestricted File Upload", "CWE-434", "High".toUpperCase()),
    UNVALIDATED_REDIRECT("Unvalidated Redirects and Forwards", "CWE-601", "Moderate".toUpperCase()),
    INSUFFICIENT_LOGGING("Insufficient Logging and Monitoring", "CWE-778", "Low".toUpperCase()),
    OPEN_REDIRECT("Open Redirect", "CWE-601", "High".toUpperCase()),
    INSECURE_COOKIE("Insecure Cookie Storage", "CWE-614", "High".toUpperCase()),
    MISSING_FUNCTION_LEVEL_ACCESS_CONTROL("Missing Function Level Access Control", "CWE-862", "Moderate".toUpperCase()),
    INSUFFICIENT_TRANSPORT_LAYER_PROTECTION("Insufficient Transport Layer Protection", "CWE-311", "High".toUpperCase()),
    SECURE_RANDOM("Use of a Broken or Risky Cryptographic Algorithm", "CWE-327", "High".toUpperCase()),
    UNSAFE_API_USAGE("Unsafe API Usage", "CWE-1234", "High".toUpperCase()); // Custom CWE example

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
        this.description = description;
        this.cweCode = cweCode;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", description, cweCode);
    }
}


