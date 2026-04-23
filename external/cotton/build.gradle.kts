plugins {
    `maven-publish`
    alias(libs.plugins.kotlin.dokka)
}

base.archivesName.set("Cotton")

group = "com.marcpg.cotton"
version = "0.1.0"

java {
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    api(libs.ktlibpg.base)
    api(libs.kotlin.serialization.json)
}

tasks {
    withType<AbstractPublishToMaven> {
        dependsOn(jar)
    }
}

// Set up a simple `maven-publish` publification to `mavenLocal`:
publishing.publications.create<MavenPublication>("maven") { setup(project, "cotton-all") }
