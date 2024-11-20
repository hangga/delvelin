# Delvelin 
![Latest Version](https://img.shields.io/maven-central/v/io.github.hangga/delvelin?color=brightgreen) ![Java Support](https://img.shields.io/badge/Java-8+-blue) ![Kotlin Support](https://img.shields.io/badge/Kotlin-1.5+-blueviolet) ![Build](https://img.shields.io/github/actions/workflow/status/hangga/delvelin/build.yml)

**Delvelin** is a **code vulnerability analyzer** for Java/Kotlin projects. It integrates **CWE (Common Weakness Enumeration)** for classifying vulnerabilities and **CVSS (Common Vulnerability Scoring System)** for scoring their severity. This ensures accurate, standardized reporting to help developers prioritize and resolve security issues effectively.

---

## **Features**
- Detects vulnerabilities using the CWE classification.
- Provides CVSS-based severity scoring for better prioritization.
- Supports Java and Kotlin codebases.
- Configurable output formats: `LOG`, `JSON`, and `HTML`.
- Easy integration as a Gradle plugin.

---

## **Tech Stack**
![Java](https://img.shields.io/badge/Java-8+-blue?logo=java) ![Kotlin](https://img.shields.io/badge/Kotlin-1.5+-blueviolet?logo=kotlin)  
![Gradle Plugin](https://img.shields.io/badge/Gradle-Plugin-brightgreen?logo=gradle)  
![CWE](https://img.shields.io/badge/CWE-Standards-orange) ![CVSS](https://img.shields.io/badge/CVSS-Severity-red)

---

## **How it Works**
We leverage:
- [CWE (Common Weakness Enumeration)](https://cwe.mitre.org/data/slices/699.html): A global standard for identifying and categorizing vulnerabilities.
- [CVSS (Common Vulnerability Scoring System)](https://www.first.org/cvss/calculator/3.0): A framework for scoring the severity of vulnerabilities.

> **Disclaimer**: Delvelin may not identify all vulnerabilities but serves as a powerful first step in securing your codebase.

---

## **Installation**

Add the plugin to your Gradle project.

### **Kotlin DSL**
```kotlin
plugins {
    id("io.github.hangga.delvelin") version "0.0.8"
}
```

### **Groovy DSL**
```groovy
plugins {
    id 'io.github.hangga.delvelin' version '0.0.8'
}
```

---

## **Configuration**

Configure Delvelin using the `delvelin` extension.

### **Kotlin DSL**
```kotlin
delvelin {
    outputFormat = OutputFileFormat.JSON // Options: LOG, JSON, HTML
    isShowDate = true
    isShowSaveDialog = false
    isIgnoreCommentBlock = true
    isCustomExtensions = false
}
```

### **Groovy DSL**
```groovy
delvelin {
    outputFormat = OutputFileFormat.JSON // Options: LOG, JSON, HTML
    isShowDate = true
    isShowSaveDialog = false
    isIgnoreCommentBlock = true
    isCustomExtensions = false
}
```

---

## **Running the Analysis**

Run the `delvelinScan` task to analyze your project:
```bash
./gradlew delvelinScan
```

---

## **Configuration Parameters**

| **Parameter**         | **Type**           | **Default**       | **Description**                                       |
|------------------------|--------------------|-------------------|-------------------------------------------------------|
| `outputFormat`         | `OutputFileFormat` | `LOG`             | Output format (`LOG`, `JSON`, `HTML`).               |
| `isShowDate`           | `Boolean`          | `true`            | Show date in the output.                             |
| `isShowSaveDialog`     | `Boolean`          | `false`           | Prompt a save dialog after the scan.                 |
| `isIgnoreCommentBlock` | `Boolean`          | `false`           | Skip comment blocks during analysis.                 |
| `isCustomExtensions`   | `Boolean`          | `false`           | Enable custom extensions for additional checks.      |

---

## **Sample Output**

### **LOG Format**
```
[INFO] Delvelin Scan Results:
Date: 2024-11-19
Detected Vulnerabilities:
1. CWE-798: Hard-coded Credentials found in `src/main/java/com/example/App.java`.
2. CWE-605: Non-thread-safe Data Structure found in `List` usage.
```

### **HTML Format**
For a detailed report, output can be saved in HTML format:
[Example HTML Output](https://hangga.github.io/vulnerability-report.html)

---

## **License**
This project is licensed under [MIT License](LICENSE).

---

## **Contributing**
Contributions are welcome! Feel free to submit issues or pull requests for new features or improvements.

---

### **Connect**
Feel free to reach out for questions, suggestions, or contributions:
- 📧 Email: bazeniancode@gmail.com
- 🌐 [GitHub Repository](https://github.com/hangga/delvelin)
