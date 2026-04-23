plugins {
    alias(libs.plugins.shadow)
}

base.archivesName.set("Dreath-Server")

dependencies {
    implementation(kotlin("reflect"))

    projectApi(":core:engine")
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        archiveClassifier.set("")
        manifest.attributes["Main-Class"] = "common.MainKt"
        manifest.attributes["Enable-Native-Access"] = "ALL-UNNAMED"
    }
}

tasks.register<JavaExec>("runWithDebug") {
    dependsOn("build")

    group = "application"
    description = "Runs the built JAR file with or without debug support based on the flag."
    workingDir = file("run")
    workingDir.mkdirs()

    val jarFile = file("build/libs/${base.archivesName.get()}-$version.jar")

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
