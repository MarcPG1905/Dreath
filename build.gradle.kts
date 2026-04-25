plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.shadow)

    alias(libs.plugins.versionCatalogueUpdate)
}

group = "com.marcpg.dreath"
version = "0.1.0"
description = "Ultra-realistic survival game by MarcPG."

base.archivesName.set("Dreath")

versionCatalogUpdate {
    pin { // Don't auto-update these.
        versions.add("kotlin")
    }
}

fun copiedTasks() = mapOf(
    ":external:mod-example" to "jar",
    ":environment:client" to "shadowJar",
    ":environment:server" to "shadowJar",
)

tasks {
    build {
        dependsOn(shadowJar, "copyJars")
    }

    register<Copy>("copyJars") {
        copiedTasks().forEach { (copyProject, copyTask) ->
            dependsOn("$copyProject:jar")
            from(
                project(copyProject)
                    .tasks.named<Jar>(copyTask)
                    .flatMap { it.archiveFile }
            )
        }

        val outputDir = layout.buildDirectory.dir("platforms")
        into(outputDir)
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

    kotlin {
        jvmToolchain(24)

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

        maven("https://central.sonatype.com/repository/maven-snapshots")
        maven("https://jitpack.io")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}

subprojects {
    version = rootProject.version
}
