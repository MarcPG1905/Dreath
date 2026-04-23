plugins {
    alias(libs.plugins.shadow)
}

base.archivesName.set("Dreath-Client")

dependencies {
    implementation(kotlin("reflect"))

    projectApi(":core:render")
    implementation(libs.kpresence)
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
    args = listOf("--debug")

    val jarFile = file("build/libs/${base.archivesName.get()}-$version.jar")

    if (jarFile.exists()) {
        classpath = files(jarFile)
    } else {
        throw GradleException("JAR file not found: ${jarFile.absolutePath}")
    }

    jvmArgs = if (project.hasProperty("debug") && project.property("debug") == "true") {
        listOf(
            "--enable-native-access=ALL-UNNAMED",
            "-Xdebug",
            "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
        )
    } else {
        listOf("--enable-native-access=ALL-UNNAMED")
    }
}
