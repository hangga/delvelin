# Using Delvelin Library

We can use the Delvelin library just like any other Kotlin/Java library. It offers a more flexible way with additional configuration.

### Gradle Configuration

```kotlin
repositories {
    maven { url 'https://repo.repsy.io/mvn/hangga/repo' }
}

dependencies {
    testImplementation('io.github.hangga:delvelin-plugin:0.1.1-beta')
}
```

### Maven Configuration

```xml

<repository>
    <id>hangga-repsy-repo</id>
    <url>https://repo.repsy.io/mvn/hangga/repo</url>
</repository>

<dependency>
    <groupId>io.github.hangga</groupId>
    <artifactId>delvelin-plugin</artifactId>
    <version>0.1.1-beta</version>
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

## Configuration Options

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
