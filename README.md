# Delvelin 
![Latest Version](https://img.shields.io/maven-central/v/io.github.hangga/delvelin?color=brightgreen) ![Java Support](https://img.shields.io/badge/Java-8+-blue) ![Kotlin Support](https://img.shields.io/badge/Kotlin-1.5+-blueviolet) ![Build](https://img.shields.io/github/actions/workflow/status/hangga/delvelin/build.yml)

**Delvelin** is a **code vulnerability analyzer** for Java/Kotlin projects. It integrates **CWE (Common Weakness Enumeration)** for classifying vulnerabilities and **CVSS (Common Vulnerability Scoring System)** for scoring their severity. This ensures accurate, standardized reporting to help developers prioritize and resolve security issues effectively.

---
![Delvelin Scan Demo](https://github.com/hangga/delvelin/blob/main/delvelin-scan.gif?raw=true)

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
    id("io.github.hangga.delvelin") version "0.0.18-beta0"
}
```

### **Groovy DSL**
```groovy
plugins {
    id 'io.github.hangga.delvelin' version '0.0.18-beta0'
}
```

---

## **Configuration**

Configure Delvelin using the `delvelin` extension.

```groovy
delvelin {
    outputFileFormat = 'JSON' // Options: LOG, JSON, HTML
    showDate = true
    showSaveDialog = false
}
```

---

## **Running the Analysis**

### On Local

Run the `delvelinScan` task to analyze your project:
```bash
./gradlew delvelinScan
```

If we are using Intellij IDEA, we can also use the gradle menu in the sidebar:

<img width="400" src="https://github.com/hangga/delvelin/blob/main/delvelin-scan-gradle-menu.png?raw=true" alt="sidebar"/>

---

### On Gitlab CI
Add `delvelinScan` gradle task to our pipeline configuration, for example:
```yaml
stages:
  - test

gradle-scan:
  stage: test
  image: gradle:7.6-jdk8
  script:
    - gradle delvelinScan
  only:
    - main
    - develop
```

## **Configuration Parameters**

| **Parameter**    | **Type**  | **Default**       | **Description**                                      |
|------------------|-----------|-------------------|------------------------------------------------------|
| `outputFileFormat`   | `String`  | `LOG`             | `LOG`, `JSON`, `HTML`.               |
| `showDate`       | `Boolean` | `true`            | Show date in the output.                            |
| `showSaveDialog` | `Boolean` | `false`           | Prompt a save dialog after the scan.                |

---

## **Sample Output**

### **HTML Format**

![Delvelin Scan Demo](https://github.com/hangga/delvelin/blob/main/output-sample.png?raw=true)
![Delvelin Scan Demo](https://github.com/hangga/delvelin/blob/main/output-sample-summary.png?raw=true)

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
