plugins {
    kotlin("jvm") version "2.0.21"
    id("com.gradle.plugin-publish") version "1.3.0"
}

group = "io.github.hangga"
version = "0.0.1"

repositories {
    mavenCentral()
    maven {url = uri("https://repo.repsy.io/mvn/hangga/repo")}
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.delvelin:delvelin:0.1.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}

tasks.register<Jar>("fatJar") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })

    archiveBaseName.set("delvelin") // Set nama base JAR
    archiveClassifier.set("all") // Set classifier untuk fat JAR
    archiveVersion.set(project.version.toString()) // Set versi dari project

    with(tasks.jar.get()) // Menggabungkan konfigurasi dengan task `jar` default
}

gradlePlugin {
    website.set("https://github.com/hangga/delvelin")
    vcsUrl.set("https://github.com/hangga/delvelin")
    plugins {
        create("delvelinPlugin") {
            id = "io.github.hangga.delvelin"
            implementationClass = "io.github.hangga.delvelin.DelvelinScan"
            displayName = "Delvelin Plugin"
            description = "Java/Kotlin vulnerability analyzer with CWE and CVSS standards."
            tags.set(listOf("testing", "Analyzer", "Vulnerability", "delvelin", "CWE", "CVSS"))
        }
    }
}