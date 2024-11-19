plugins {
    kotlin("jvm") version "2.0.21"
    id("com.gradle.plugin-publish") version "1.3.0"
}

group = "io.github.hangga"
version = "1.0-SNAPSHOT"

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
    jvmToolchain(8)
}