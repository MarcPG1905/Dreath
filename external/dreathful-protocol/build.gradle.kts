plugins {
    `maven-publish`
    alias(libs.plugins.kotlin.dokka)
}

base.archivesName.set("DreathfulProtocol")

group = "com.marcpg.dreathful-protocol"
version = "0.1.0"

java {
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    api(libs.ktlibpg.base)
    api(libs.lz4)
    projectCompileOnly(":dreath-utils")
}

tasks {
    withType<AbstractPublishToMaven> {
        dependsOn(jar)
    }
}

// Set up a simple `maven-publish` publification to `mavenLocal`:
publishing.publications.create<MavenPublication>("maven") { setup(project, "dreathful-protocol-all") }
