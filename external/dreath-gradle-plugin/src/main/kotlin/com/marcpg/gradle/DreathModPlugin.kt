package com.marcpg.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.attributes
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

class DreathModPlugin : Plugin<Project> {
    companion object {
        internal lateinit var libs: VersionCatalog
    }

    override fun apply(project: Project) {
        libs = project.extensions.getByType<VersionCatalogsExtension>().named("libs")

        val ext = project.extensions.create<DreathModExtension>("dreathMod")

        project.addDependencies()
        project.afterEvaluate {
            attachEntryPoint(ext)
        }
    }

    private fun Project.attachEntryPoint(ext: DreathModExtension) {
        tasks.withType<Jar>().configureEach {
            manifest.attributes(
                "Dreath-Mod-Json" to ext.dreathModJson.get(),
            )
        }
    }

    /**
     * Adds the base `:api` dependency to all source sets, including the default ones.
     */
    private fun Project.addDependencies() {
        // TODO: Make sure all this is also accessible from outside the project if the version catalogue and :api subproject aren't on the same disk:

        dependencies.add("compileOnly", libs.findLibrary("kotlin.stdlib").get())
        dependencies.add("compileOnly", project(":api"))
    }
}
