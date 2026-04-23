import org.gradle.api.Project

private fun Project.addDep(type: String, dep: Any) {
    dependencies.add(type, dep)
}

fun Project.depOrFile(type: String, notation: String, fallback: String) {
    val config = configurations.detachedConfiguration(dependencies.create(notation))
    try {
        check(config.resolve().isNotEmpty())
        addDep(type, notation)
    } catch (_: Exception) {
        addDep(type, files(fallback))
    }
}

fun Project.implementationOrFile(notation: String, fallback: String) = depOrFile("implementation", notation, fallback)
fun Project.compileOnlyOrFile(notation: String, fallback: String) = depOrFile("compileOnly", notation, fallback)
fun Project.apiOrFile(notation: String, fallback: String) = depOrFile("api", notation, fallback)

private fun Project.addProjectDep(type: String, path: String) {
    if (project.path != path)
        addDep(type, project(path))
}

fun Project.projectImplementation(path: String) = addProjectDep("implementation", path)
fun Project.projectCompileOnly(path: String) = addProjectDep("compileOnly", path)
fun Project.projectApi(path: String) = addProjectDep("api", path)
