# Using Delvelin Gradle Plugin

Add the plugin to your Gradle project.

### **1. Kotlin DSL**
```kotlin
plugins {
    id("io.github.hangga.delvelin") version "0.1.1-beta"
}
```

### **2. Groovy DSL**
```groovy
plugins {
    id 'io.github.hangga.delvelin' version '0.1.1-beta'
}
```

## **Configuration**

Configure Delvelin using the `delvelin` extension.

```groovy
delvelin {
    outputFileFormat = 'JSON' // Options: LOG, JSON, HTML
    showDate = true
    showSaveDialog = false
}
```

## **Running Delvelin Analyzer**

### 1. On Local Machine

Run the `delvelinScan` task to analyze your project:
```bash
./gradlew delvelinScan
```
![Delvelin Scan Demo](https://delvelin.github.io/assets/img/delvelin-scan-new.gif)

If we are using Intellij IDEA, we can also use the gradle menu in the sidebar:

<img width="400" src="https://github.com/hangga/delvelin/blob/main/doc/delvelin-scan-gradle-menu.png?raw=true" alt="sidebar"/>

### 2. On Gitlab CI
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

## Configuration Options

| Configuration Option                     | Description                                                                                  | Default Value |
|------------------------------------------|----------------------------------------------------------------------------------------------|---------------|
| `setOutputFormat` | Set the output format of the analysis (e.g., `HTML`, `JSON`, or `LOG`).                  | `LOG`     |
| `setAutoLaunchBrowser`    | Automatically open the generated HTML report in the browser. Set to `false` to disable.      | `false`       |
| `setShowSaveDialog`       | Display a save dialog for HTML and JSON reports. Set to `false` to disable.                  | `false`       |