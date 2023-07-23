import org.apache.tools.ant.filters.FixCrLfFilter
import org.apache.tools.ant.filters.ReplaceTokens
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.archivesName

val exposedVersion = extra["exposed.version"] as String
val kotlinVersion = extra["kotlin.version"] as String

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    kotlin("jvm") version "1.7.10"
}

group = "dev.activecraft"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://libraries.minecraft.net/")
}

dependencies {
    implementation("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    implementation(kotlin("stdlib"))
    implementation("com.mojang:authlib:1.5.21")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")
    implementation("org.jetbrains.exposed:exposed-core:${exposedVersion}")
    implementation("org.jetbrains.exposed:exposed-dao:${exposedVersion}")
    implementation("org.jetbrains.exposed:exposed-jdbc:${exposedVersion}")
    implementation("org.jetbrains.exposed:exposed-java-time:${exposedVersion}")
    implementation("mysql:mysql-connector-java:8.0.30")
    testImplementation(kotlin("test-junit", kotlinVersion))
    implementation(kotlin("stdlib"))
}

val targetJavaVersion = 17
java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
}

tasks {
    processResources {
        filter(FixCrLfFilter::class)
        filter(ReplaceTokens::class, "tokens" to mapOf("version" to project.version))
        filteringCharset = "UTF-8"
    }
    jar {
        // Disabled, because we use the shadowJar task for building our jar
        enabled = false
    }
    build {
        dependsOn(shadowJar)
    }
}