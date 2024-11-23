plugins {
    java
    kotlin("jvm") version "2.0.21"
    id("com.gradle.plugin-publish") version "1.3.0"
}

group = "io.github.hangga"
version = "0.0.17"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8) // Menetapkan toolchain untuk kompatibilitas Java 8
}

//tasks.register<Jar>("fatJar") {
//    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//
//    dependsOn(tasks.classes)
//
//    from(sourceSets.main.get().output) // Tambahkan semua output dari sourceSets
//    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
//
//    archiveBaseName.set("delvelin")
//    archiveClassifier.set("all")
//    archiveVersion.set(project.version.toString())
//}
//
//tasks.named("build") {
//    dependsOn("fatJar") // Pastikan fat JAR dibuat dalam task build
//}

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