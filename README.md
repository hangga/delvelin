### Documentation for **DelvelinScan** Gradle Plugin

**Plugin Name**: `Delvelin`  
**Purpose**: This plugin is designed to analyze code vulnerabilities in Java/Kotlin projects with CWE and CVSS standards.

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