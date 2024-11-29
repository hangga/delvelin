plugins {
    java
    kotlin("jvm") version "2.0.21"
    id("com.gradle.plugin-publish") version "1.3.0"
}

group = "io.github.hangga"
version = "0.0.20-beta"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
//    implementation("com.google.guava:guava:31.1-jre")
//    implementation("org.hibernate.orm:hibernate-core:7.0.0.Beta1")
//    implementation("org.apache.logging.log4j:log4j-core:2.0-beta9")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8) // Menetapkan toolchain untuk kompatibilitas Java 8
}

tasks.register<Jar>("fatJar") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    dependsOn(tasks.classes)

    from(sourceSets.main.get().output) // Tambahkan semua output dari sourceSets
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })

    artifacts { name }
    archiveBaseName.set("delvelin")
    archiveClassifier.set("all")
    archiveVersion.set(project.version.toString())
}

tasks.named("build") {
    dependsOn("fatJar") // Pastikan fat JAR dibuat dalam task build
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            // Publikasikan fat JAR dengan nama khusus
            artifact(tasks.named("fatJar").get()) {
                classifier = "all" // Sertakan classifier agar nama unik
            }

            // Metadata publikasi
            groupId = project.group.toString()
            artifactId = "delvelin"
            version = project.version.toString()
        }
    }

    repositories {
        maven {
            url = uri("https://repo.repsy.io/mvn/hangga/repo")
            credentials {
                username = project.findProperty("repoUsername") as String? ?: ""
                password = project.findProperty("repoPassword") as String? ?: ""
            }
        }
    }
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

tasks.register<JavaExec>("scanDelvelin") {
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("io.github.hangga.delvelin.Delvelin")
    args("format_html")
//    args("format_json")
}
