# Using Delvelin Library

You can use the Delvelin library just like any other Kotlin/Java library.

### Gradle Configuration

```kotlin
repositories {
    maven { url 'https://repo.repsy.io/mvn/hangga/repo' }
}

dependencies {
    implementation 'io.github.hangga:delvelin:0.1.1-beta'
}
```

### Maven Configuration

```xml
<repositories>
    <repository>
        <id>hangga-repsy-repo</id>
        <url>https://repo.repsy.io/mvn/hangga/repo</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>io.github.hangga</groupId>
        <artifactId>delvelin</artifactId>
        <version>0.1.1-beta</version>
    </dependency>
</dependencies>
```

---

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

---

### Usage on Android

To log messages in LogCat, you can use a custom listener like this:

```kotlin
@Test
fun `vulnerability with custom listener for android`() {
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

---

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

---

> **Important Notes**
> If you choose the JSON or HTML output format, you **must** use either `setAutoLaunchBrowser` or 
> `setShowSaveDialog`. These methods ensure that the output is handled properly.