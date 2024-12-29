<img src="https://github.com/hangga/delvelin/blob/main/doc/delvelin-soft-black.webp?raw=true" 
alt="Delvelin Scan Demo" width="260">

![Java](https://img.shields.io/badge/Java-8+-blue?logo=java) ![Kotlin](https://img.shields.io/badge/Kotlin-1.5+-blueviolet?logo=kotlin) ![Gradle Plugin](https://img.shields.io/badge/Gradle-Plugin-brightgreen?logo=gradle) ![CWE](https://img.shields.io/badge/CWE-Standards-orange) ![CVSS](https://img.shields.io/badge/CVSS-Severity-red)
[![OSV.dev](https://img.shields.io/badge/OSV.dev-Vulnerability%20Database-blue)](https://google.github.io/osv.dev/)
[![License](https://img.shields.io/badge/license-MIT-green)](LICENSE) 
---
**Delveline** is a tool for Kotlin and Java developers that identifies and categorizes vulnerabilities, helping teams align with security standards like ISO 27001 and improve code security.

### Jump ahead:
- 1.[How it Works](#1-how-it-works)
- 2.[Example Output](#2-example-output)
- 3.[Integrating Delvelin](#3-integrating-delvelin)
    * 3.1. [Using Delvelin Gradle Plugin](#31-using-delvelin-gradle-plugin)
        + [Kotlin DSL](#kotlin-dsl)
        + [Groovy DSL](#groovy-dsl)
        + [Configuration](#configuration)
        + [Running Delvelin Analyzer](#running-delvelin-analyzer)
            - [On Local Machine](#on-local-machine)
            - [On Gitlab CI](#on-gitlab-ci)
    * 3.2. [Using Delvelin Library](#using-delvelin-library)
        + [Gradle Configuration](#gradle-configuration)
        + [Maven Configuration](#maven-configuration)
        + [Best Practices](#best-practices)
        + [Usage on Android](#usage-on-android)
        + [Alternative Examples](#alternative-examples)
        + [Configuration Options](#configuration-options)
- 4.[License](#4-license)
- 5.[Contributing](#5-contributing)
---

![Delvelin Scan Demo](https://delvelin.github.io/assets/img/delvelin-scan-new.gif)

# 1. How it Works

Delveline is a specialized tool designed for Kotlin and Java developers, helping identify and categorize software vulnerabilities effectively. By leveraging the CWE (Common Weakness Enumeration) framework and detecting CVE (Common Vulnerabilities and Exposures), Delveline bridges the gap between secure software development and industry standards like ISO 27001.

Aligned with ISO 27001’s focus on information security and risk management, Delveline provides actionable insights into vulnerabilities within source code and dependencies specific to Kotlin and Java projects. While not a standalone security solution, it serves as a valuable aid in achieving compliance by offering clear categorization of risks and practical guidance for remediation.

We leverage:
- [CWE (Common Weakness Enumeration)](https://cwe.mitre.org/data/slices/699.html): A global standard for identifying and categorizing vulnerabilities.
- [OSV (Open Source Vulnerabilities)](https://google.github.io/osv.dev/): A comprehensive database for open-source vulnerability information.
- **ISO/IEC 27001 Alignment**: Supporting security awareness and risk management practices aligned with global information security standards.

Delveline empowers Kotlin and Java teams to develop secure and resilient applications while aligning their development practices with global security standards.

# 2. Example Output

<div style="display: flex; justify-content: space-between; gap: 10px;">
  <a href="https://github.com/delvelin/blog/blob/master/_posts/delvelin-report-console-1.png?raw=true" target="_blank">
    <img 
      src="https://github.com/delvelin/blog/blob/master/_posts/delvelin-report-console-1.png?raw=true"/>
  </a>
  <a href="https://github.com/delvelin/blog/blob/master/_posts/delvelin-report-console-2.png?raw=true" target="_blank">
    <img 
      src="https://github.com/delvelin/blog/blob/master/_posts/delvelin-report-console-2.png?raw=true"/>
  </a>
</div>

Or view example in <a href="https://delvelin.github.io/docs/vulnerability-report.html">HTML 
 Format</a>

[//]: # (<img )

[//]: # (src="https://github.com/delvelin/blog/blob/master/_posts/delvelin-report-html.png?raw=true" )

[//]: # (alt="Delveline Report HTML View" width="60%" />)

> **Disclaimer**: Delveline may not identify all vulnerabilities but serves as a powerful first step in securing your codebase.

# 3. Integrating Delvelin

To integrate delvelin in Java/Kotlin project, we can use two ways. Choose the way that suits 
your project needs:

## 3.1. Using Delvelin Gradle Plugin

Add the plugin to your Gradle project.

### KTS
```kotlin
plugins {
    id("io.github.hangga.delvelin") version "0.1.2-beta"
}
```

### Groovy
```groovy
plugins {
    id 'io.github.hangga.delvelin' version '0.1.2-beta'
}
```

### Configuration

Configure Delvelin using the `delvelin` extension.

```groovy
delvelin {
    outputFileFormat = 'JSON' // Options: LOG, JSON, HTML
    showSaveDialog = false
}
```

| Configuration Option                     | Description                                                                                  | Default Value |
|------------------------------------------|----------------------------------------------------------------------------------------------|---------------|
| `setOutputFormat` | Set the output format of the analysis (e.g., `HTML`, `JSON`, or `LOG`).                  | `LOG`     |
| `setAutoLaunchBrowser`    | Automatically open the generated HTML report in the browser. Set to `false` to disable.      | `false`       |
| `setShowSaveDialog`       | Display a save dialog for HTML and JSON reports. Set to `false` to disable.                  | `false`       |

### Running Delvelin Analyzer

#### On Local Machine

Run the `delvelinScan` task to analyze your project:
```bash
./gradlew delvelinScan
```
![Delvelin Scan Demo](https://delvelin.github.io/assets/img/delvelin-scan-new.gif)

If we are using Intellij IDEA, we can also use the gradle menu in the sidebar:

<div style="display: flex; justify-content: center; align-items: center;">  
    <img style="width:300px;" 
    src="https://github.com/hangga/delvelin/blob/main/doc/delvelin-scan-gradle-menu.png?raw=true" alt="sidebar"/>
</div>

#### On Gitlab CI
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

## 3.2. Using Delvelin Library

We can use the Delvelin library just like any other Kotlin/Java library. It offers a more flexible way with additional configuration.

### Gradle

```kotlin
repositories {
    maven { url 'https://repo.repsy.io/mvn/hangga/repo' }
}

dependencies {
    testImplementation('io.github.hangga:delvelin-plugin:0.1.2-beta')
}
```

### Maven

```xml

<repository>
    <id>hangga-repsy-repo</id>
    <url>https://repo.repsy.io/mvn/hangga/repo</url>
</repository>

<dependency>
    <groupId>io.github.hangga</groupId>
    <artifactId>delvelin-plugin</artifactId>
    <version>0.1.2-beta</version>
    <scope>test</scope>
</dependency>
```

### Best Practices

It is highly recommended to run the Delvelin library in unit tests to keep your production classes clean. You can also run it in the main class or the project’s main package, but this is not advised.

Here’s an example of a unit test to instantiate and run Delvelin:

```kotlin
@Test
fun `vulnerability test`() {
    Delvelin()
        .setOutputFormat(OutputFileFormat.HTML)
        .setAutoLaunchBrowser(true) // Automatically opens the browser for HTML format
        .setAllowedExtensions(".java") // By default, it supports .java, .kt, .gradle, .kts, and .xml
        .setShowSaveDialog(true) // Only applicable for HTML & JSON formats
        .setShowDate(true) // For Console LOG format
        .scan()
}
```

### Usage on Android

To log messages in LogCat, you can use a custom listener like this:

```kotlin
@Test
fun `vulnerability test with custom listener for android`() {
    Delvelin().setLogListener(object : LogListener {
        override fun onGetLog(s: String) {
            Log.d("DelvelinLog", s)
        }

        override fun onGetLog(stringBuffer: StringBuffer) {
            Log.d("DelvelinLog", stringBuffer.toString())
        }
    }).scan()
}
```

### Alternative Examples

```kotlin
@Test
fun `vulnerability test`() {
    Delvelin()
        .setOutputFormat(OutputFileFormat.HTML)
        .setAutoLaunchBrowser(true) // Automatically opens the browser for HTML format
        .scan()
}

@Test
fun `vulnerability test with save dialog`() {
    Delvelin()
        .setOutputFormat(OutputFileFormat.HTML)
        .setShowSaveDialog(true) // Only applicable for HTML & JSON formats
        .scan()
}
```

![Delvelin Scan Demo](doc/save-dialog-blur_magicstudio_8tefrlgzfnr-cmprz.png?raw=true)

### Configuration Options

| Configuration Option                     | Description                                                                                  | Default Value |
|------------------------------------------|----------------------------------------------------------------------------------------------|---------------|
| `setOutputFormat(OutputFileFormat format)` | Set the output format of the analysis (e.g., `HTML`, `JSON`, or `LOG`).                  | `LOG`     |
| `setAllowedExtensions(String... values)` | Specify file extensions to include in the analysis. By default, allows `.java`, `.kt`, `.gradle`, `.kts`, and `.xml`. | `[".java", ".kt", ".gradle", ".kts", ".xml"]` |
| `setAutoLaunchBrowser(boolean value)`    | Automatically open the generated HTML report in the browser. Set to `false` to disable.      | `false`       |
| `setShowSaveDialog(boolean value)`       | Display a save dialog for HTML and JSON reports. Set to `false` to disable.                  | `false`       |
| `setLogListener(LogListener listener)`   | Set a custom listener for capturing logs during analysis (useful for Android integration).   | `null`        |


> **Important Notes**
> If you choose the JSON or HTML output format, you **must** use either `setAutoLaunchBrowser` or
> `setShowSaveDialog`. These methods ensure that the output is handled properly.

### <a href="https://github.com/delvelin/example-kotlin">See Example Project >></a>

# 4. License
This project is licensed under [MIT License](LICENSE).

# 5. Contributing
Contributions are welcome! Feel free to submit issues or pull requests for new features or improvements.

This project is still a work in progress, and your contributions are highly valuable in helping us improve and refine it.

If you find this project useful and would like to support its development, we would greatly appreciate your donations. Your generosity will go a long way in ensuring the growth and sustainability of this initiative.

---

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/F1F215NPV4)

[![Support via PayPal](https://cdn.rawgit.com/twolfson/paypal-github-button/1.0.0/dist/button.svg)](https://www.paypal.me/hanggaajisayekti/)

Thank you for your support!

### Connect
Feel free to reach out for questions, suggestions, or contributions:
- 📧 Email: bazeniancode@gmail.com
- 🌐 [GitHub Repository](https://github.com/hangga/delvelin)
