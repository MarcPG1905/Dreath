plugins {
    id("maven-publish")
    id("com.gradleup.shadow") version "8.3.6"
}

base.archivesName.set("Dreath-API")
val artifact = "dreath-api"

java {
    withSourcesJar()
    withJavadocJar()
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        archiveClassifier.set("")
    }
    withType<AbstractPublishToMaven> {
        dependsOn(jar)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifact(tasks["shadowJar"]) {
                classifier = ""
            }
            artifact(tasks["sourcesJar"]) {
                classifier = "sources"
            }
            artifact(tasks["javadocJar"]) {
                classifier = "javadoc"
            }

            groupId = group.toString()
            artifactId = "${artifact}-all"
            version = version.toString()

            pom.withXml {
                val dependenciesNode = asNode().appendNode("dependencies")
                configurations.implementation.get().dependencies
                    .filter { it !is ProjectDependency }
                    .forEach { dep ->
                        val dependencyNode = dependenciesNode.appendNode("dependency")
                        dependencyNode.appendNode("groupId", dep.group)
                        dependencyNode.appendNode("artifactId", dep.name)
                        dependencyNode.appendNode("version", dep.version)
                        dependencyNode.appendNode("scope", "runtime")
                    }
            }
        }
    }
}
