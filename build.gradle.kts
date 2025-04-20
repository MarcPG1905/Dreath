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
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

subprojects {
    version = rootProject.version

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

    kotlin {
        jvmToolchain(21)
        compilerOptions {
            freeCompilerArgs.add("-opt-in=kotlin.ExperimentalStdlibApi")
            freeCompilerArgs.add("-opt-in=kotlin.ExperimentalUnsignedTypes")
            freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
            freeCompilerArgs.add("-opt-in=kotlin.uuid.ExperimentalUuidApi")
        }
    }

    repositories {
        mavenLocal()
        mavenCentral()

        maven("https://marcpg.com/repo/")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }

    dependencies {
        implementation("com.marcpg:libpg-base:1.0.1")

        implementation(kotlin("reflect"))
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.8.1")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.10.2")
        implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")

        if (!project.path.startsWith(":external")) {
            if (project.path != ":api")
                implementation(project(":api"))

            if (project.path.startsWith(":core") || project.path.startsWith(":environment")) {
                implementation("com.github.ajalt.clikt:clikt:5.0.3")
            }
        }

        if (project.path.startsWith(":environment")) {
            implementation(project(":core:common"))
        }
    }
}
