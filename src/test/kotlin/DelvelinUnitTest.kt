import io.github.hangga.delvelin.Delvelin
import io.github.hangga.delvelin.cwedetectors.BaseDetector
import io.github.hangga.delvelin.properties.OutputFileFormat
import io.github.hangga.delvelin.properties.Vulnerabilities
import org.junit.jupiter.api.Test
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.security.KeyStore
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

class DelvelinUnitTest {

    class ExampleCustomDetector : BaseDetector() {

        init {
            this.vulnerabilities = Vulnerabilities.UNSAFE_REFLECTION
        }

        override fun detect(line: String, lineNumber: Int) {
            // Implementation of line based detection
            if (line.contains("examplePattern")) {
                val specificLocation = specificLocation(lineNumber)
                setValidVulnerability(
                    specificLocation,
                    "Example finding",
                    "Detected example pattern in the code"
                )
            }
        }

        override fun detect(content: String) {
            // Implementation of full content based detection
            if (content.contains("examplePattern")) {
                val specificLocation = specificLocation(-1) // -1 to denote whole content
                setValidVulnerability(
                    specificLocation,
                    "Example finding",
                    "Detected example pattern in the full content"
                )
            }
        }
    }

    @Test
    fun `vulnerability test`() {
        Delvelin().setOutputFormat(OutputFileFormat.HTML)
            .addCustomDetector(ExampleCustomDetector())
            .setAllowedExtensions(".gradle", ".kts", ".java", ".kt").setAutoLaunchBrowser(true)
            .scan()
    }

    @Test
    fun `example of insecure Http connection`() {
        val urlString = "http://example.com" // Menggunakan HTTP tanpa enkripsi
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            connection.doInput = true

            val responseCode = connection.responseCode
            println("Response Code: $responseCode")

            if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = StringBuilder()
                var line: String?

                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                reader.close()
                println("Response: $response")
            } else {
                println("Failed to connect: $responseCode")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
        }
    }

    @Test
    fun `example of secure Https connection`() {
        val urlString = "https://example.com" // URL menggunakan HTTPS
        val url = URL(urlString)

        // Membuka koneksi HTTPS
        val connection = url.openConnection() as HttpsURLConnection

        try {
            // Menentukan properti koneksi
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            connection.doInput = true

            // Validasi sertifikat (gunakan TrustManager untuk pengaturan lebih lanjut jika perlu)
            val trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            keyStore.load(null, null)
            trustManagerFactory.init(keyStore)

            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustManagerFactory.trustManagers, null)
            connection.sslSocketFactory = sslContext.socketFactory

            // Mendapatkan response code dan membaca data
            val responseCode = connection.responseCode
            println("Response Code: $responseCode")

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = StringBuilder()
                var line: String?

                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                reader.close()
                println("Response: $response")
            } else {
                println("Failed to connect: $responseCode")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
        }
    }
}