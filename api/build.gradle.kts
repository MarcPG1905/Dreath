plugins {
    `maven-publish`
    alias(libs.plugins.kotlin.dokka)
}

base.archivesName.set("Dreath-API")

group = "com.marcpg.dreath"

java {
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    projectApi(":dreath-utils")
    projectApi(":external:dreathful-protocol")

    api(libs.kotlin.serialization.json)
    api(libs.kotlin.coroutines)
    api(libs.kotlin.datetime)

    api(libs.joml)
}

tasks {
    withType<AbstractPublishToMaven> {
        dependsOn(jar)
    }
}

// Set up a simple `maven-publish` publification to `mavenLocal`:
publishing.publications.create<MavenPublication>("maven") { setup(project, "dreath-api") }
