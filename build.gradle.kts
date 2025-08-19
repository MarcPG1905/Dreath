plugins {
    kotlin("jvm") version "2.2.0"
    kotlin("multiplatform") version "2.2.0" apply false
    kotlin("plugin.serialization") version "2.2.0"
}

group = "com.marcpg"
version = "0.1.0"
description = "Ultra-realistic survival game by MarcPG."

base.archivesName.set("Dreath")

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()

        maven("https://jitpack.io")
        maven("https://marcpg.com/repo/")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://central.sonatype.com/repository/maven-snapshots")
    }
}

subprojects {
    if (project.path == ":external:launcher") return@subprojects

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

    dependencies {
        implementation(kotlin("reflect"))
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.8.1")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.10.2")
        implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")

        implementation("com.marcpg:ktlibpg-base:2.0.0")

        if (!project.path.startsWith(":external") || project.path == ":external:mod-example") {
            if (project.path != ":api")
                implementation(project(":api"))

            implementation(project(":external:cotton"))

            if (project.path.startsWith(":core") || project.path.startsWith(":environment")) {
                implementation("com.github.ajalt.clikt:clikt:5.0.3")
            }
        }

        if (project.path.startsWith(":environment")) {
            implementation(project(":core:common"))
        }
    }

    plugins.withId("java") {
        val copyToRoot = tasks.register<Copy>("copyToRoot") {
            val jar = tasks.named<Jar>("jar")
            dependsOn()

            // TODO: Finish!
        }
    }
}
