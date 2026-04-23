plugins {
    `maven-publish`
    alias(libs.plugins.kotlin.dokka)
}

base.archivesName.set("Dreath-Utils")

group = "com.marcpg.dreath"

java {
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    projectApi(":external:cotton")
    api(libs.ktlibpg.base)
}

tasks {
    withType<AbstractPublishToMaven> {
        dependsOn(jar)
    }
}

// Set up a simple `maven-publish` publification to `mavenLocal`:
publishing.publications.create<MavenPublication>("maven") { setup(project, "dreath-utils") }
