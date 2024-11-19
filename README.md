### Delvelin

**Purpose**: This plugin is designed to analyze code vulnerabilities in Java/Kotlin projects with CWE and CVSS standards.

<p>We use <span class="highlight">
          <a target="_blank" href="https://cwe.mitre.org/data/slices/699.html">CWE (Common Weakness Enumeration)</a>
        </span> as the standard for categorizing and reporting vulnerabilities. This ensures that identified issues are described using a globally recognized taxonomy, making it easier to understand, track, and address them effectively. </p>
      <p>For scoring vulnerabilities, we adhere to the <span class="highlight">
          <a target="_blank" href="https://www.first.org/cvss/calculator/3.0">CVSS (Common Vulnerability Scoring System)</a>
        </span>. This system provides a consistent way to assess the severity of vulnerabilities, enabling developers and security teams to prioritize their efforts based on the potential impact on their systems. </p>
      <p>
        <strong>Disclaimer:</strong> While this approach may not be entirely accurate in identifying every possible vulnerability, it provides a helpful starting point to secure your code. Ultimately, vigilance and proactive measures play a crucial role in maintaining robust security.
      </p>

---

#### **Installation**

1. **Add the plugin to `build.gradle.kts` (Kotlin DSL)**:
   ```kotlin
   plugins {
       id("io.github.hangga.delvelin") version "0.0.1"
   }
   ```

2. **Or to `build.gradle` (Groovy DSL)**:
   ```groovy
   plugins {
       id 'io.github.hangga.delvelin' version '0.0.1'
   }
   ```

---

#### **Configuration**

The plugin provides an extension named `delvelin` to configure analysis parameters. You can configure it in the `build.gradle.kts` or `build.gradle` file.

**Kotlin DSL**:
```kotlin
delvelin {
    outputFormat = OutputFormat.JSON // Output format: LOG, JSON, or XML
    isShowDate = true               // Show date in the results
    isShowSaveDialog = false        // Display a save dialog after analysis
    isIgnoreCommentBlock = true     // Ignore comment blocks during analysis
    isCustomExtensions = false      // Enable custom extensions
}
```

**Groovy DSL**:
```groovy
delvelin {
    outputFormat = OutputFormat.JSON // Output format: LOG, JSON, or XML
    isShowDate = true                // Show date in the results
    isShowSaveDialog = false         // Display a save dialog after analysis
    isIgnoreCommentBlock = true      // Ignore comment blocks during analysis
    isCustomExtensions = false       // Enable custom extensions
}
```

---

#### **Running the Analysis**

Run the Gradle task named `delvelinScan` to start the code analysis:
```bash
./gradlew delvelinScan
```

---

#### **Configuration Parameters**

| **Parameter**         | **Type**              | **Default**       | **Description**                                                              |
|------------------------|-----------------------|-------------------|------------------------------------------------------------------------------|
| `outputFormat`         | `OutputFormat`       | `LOG`             | Output format of the results (`LOG`, `JSON`, `XML`).                        |
| `isShowDate`           | `Boolean`            | `true`            | Display the date in the output results.                                     |
| `isShowSaveDialog`     | `Boolean`            | `false`           | Show a save dialog after analysis completes.                                |
| `isIgnoreCommentBlock` | `Boolean`            | `false`           | Ignore comment blocks during the analysis.                                  |
| `isCustomExtensions`   | `Boolean`            | `false`           | Enable custom extensions.                                                   |

---

#### **Sample Output**
If `outputFormat` is set to `LOG`, the results will be displayed directly in the console, for example:
```
[INFO] Delvelin Scan Results:
Date: 2024-11-19
Detected Vulnerabilities:
1. CWE-798: Hard-coded Credentials found in file: `src/main/java/com/example/App.java`.
2. CWE-605: Non-thread-safe Data Structure found in `List` usage.
```

For other formats (`JSON` or `XML`), the results will be saved as configured.

---

#### **Adjusting Output Format**
To customize the output further, add logic to the plugin code or use the `isCustomExtensions` feature with your own extension definitions.

---

Let me know if you need any additional details! 😊