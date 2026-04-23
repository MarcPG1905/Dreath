import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.named

fun MavenPublication.setup(project: Project, artifact: String) {
//    artifact(project.tasks.getByName("shadowJar")) {
//        classifier = ""
//    }
    artifact(project.tasks.getByName("sourcesJar")) {
        classifier = "sources"
    }
    artifact(project.tasks.getByName("javadocJar")) {
        classifier = "javadoc"
    }

    groupId = project.group.toString()
    artifactId = artifact
    version = project.version.toString()

    pom.withXml {
        val dependenciesNode = asNode().appendNode("dependencies")
        project.configurations.named<org.gradle.api.artifacts.Configuration>("implementation").get().dependencies
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
