import io.github.hangga.delvelin.Delvelin
import io.github.hangga.delvelin.properties.OutputFileFormat
import org.junit.jupiter.api.Test

class DelvelinUnitTest {
    @Test
    fun `vulnerability test`() {
        Delvelin()
            .setOutputFormat(OutputFileFormat.HTML)
            .setAllowedExtensions(".gradle",".kts",".java",".kt")
            .setAutoLaunchBrowser(true)
            .scan()
    }
}