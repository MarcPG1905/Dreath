plugins {
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.serialization") version "2.1.10"
}

group = "com.marcpg"
version = "0.1.0"
description = "Ultra-realistic survival game by MarcPG."

base.archivesName.set("Dreath")

repositories {
    mavenLocal()
    mavenCentral()

    maven("https://marcpg.com/repo/")
}

subprojects {
    version = rootProject.version

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

    kotlin {
        jvmToolchain(21)
        compilerOptions {
            freeCompilerArgs.add("-opt-in=kotlin.ExperimentalUnsignedTypes")
        }
    }

    repositories {
        mavenLocal()
        mavenCentral()

        maven("https://marcpg.com/repo/")
    }

    dependencies {
        implementation("com.marcpg:libpg-base:1.0.0")
        implementation("com.marcpg:libpg-storage-json:1.0.0")
        implementation("com.github.ajalt.clikt:clikt:5.0.3")

        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.8.1")
    }
}
