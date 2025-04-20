plugins {
    id("com.gradleup.shadow") version "8.3.6"
}

base.archivesName.set("Dreath-Server")

dependencies {
    implementation(project(":core:engine"))
}

tasks {
    build {
        dependsOn(shadowJar, processResources)
    }
    shadowJar {
        archiveClassifier.set("")
        manifest {
            attributes["Main-Class"] = "com.marcpg.common.MainKt"
        }
    }
}

tasks.register<JavaExec>("runWithDebug") {
    dependsOn(tasks.build)

    group = "application"
    description = "Runs the JAR file with or without debug support based on the flag."
    workingDir = file("run")
    workingDir.mkdirs()

    val jarFile = file("build/libs/Dreath-Server-$version.jar")

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
