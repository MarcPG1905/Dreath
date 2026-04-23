package com.marcpg.gradle

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

/**
 * Configure stuff related to this Dreath mod and its required subprojects and source sets.
 * @author MarcPG
 * @since 0.1.0
 */
abstract class DreathModExtension @Inject constructor(objects: ObjectFactory) {
    /**
     * The path inside the built JAR file to the `dreath-mod.json` file.
     * An example of a custom location could be `data/dreath-mod-info-file.json`.
     *
     * By default, this is set to just set to `dreath-mod.json`.
     */
    val dreathModJson: Property<String> = objects.property<String>().convention("dreath-mod.json")

    internal var hasOwnApi: Boolean = false

    /**
     * Creates an additional `api` source set be available and automatically published
     * when the mod is published via maven-publish is run.
     */
    fun hasOwnApi() { hasOwnApi = true }
}
