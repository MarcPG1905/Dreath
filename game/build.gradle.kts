plugins {
    id("com.gradleup.shadow") version "8.3.6"
}

base.archivesName.set("Dreath-Game")

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    implementation(project(":api"))

    implementation("de.fabmax.kool:kool-core:0.17.0")
    implementation("de.fabmax.kool:kool-physics:0.17.0")

    implementation("io.github.vyfor:kpresence-jvm:0.6.5")

    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
}

tasks {
    build {
        dependsOn(shadowJar, processResources)
    }
    shadowJar {
        archiveClassifier.set("")
        manifest {
            attributes["Main-Class"] = "com.marcpg.MainKt"
        }
    }
}

tasks.register<JavaExec>("runWithDebug") {
    dependsOn(tasks.build)

    group = "application"
    description = "Runs the JAR file with or without debug support based on the flag."
    workingDir = file("run")
    workingDir.mkdirs()

    val jarFile = file("build/libs/Dreath-Game-$version.jar")

    if (jarFile.exists()) {
        classpath = files(jarFile)
    } else {
        throw GradleException("JAR file not found: ${jarFile.absolutePath}")
    }

    if (project.hasProperty("debug") && project.property("debug") == "true") {
        jvmArgs = listOf(
            "-Xdebug",
            "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
        )
    }
}
