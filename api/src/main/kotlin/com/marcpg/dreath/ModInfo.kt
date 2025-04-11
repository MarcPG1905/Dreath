package com.marcpg.dreath

import com.marcpg.dreath.ModInfo.Dependencies.Dependency.LoadOrder.AUTO
import java.net.URI
import kotlin.reflect.KClass

/**
 * Represents information for a mod stripped from its `dreath-mod.json` file.
 *
 * It includes this information:
 * - Unique ID
 * - Name
 * - Version
 * - Description
 * - Main Class Reference
 * - Developer List
 * - Contact Information
 * - Dependency List
 */
data class ModInfo(
    val id: String,
    val name: String,
    val version: String,
    val description: String,
    val main: KClass<out DreathMod>,
    val developers: List<String>,
    val contact: Contact,
    val dependencies: Dependencies
) {
    /**
     * Represents contact information for a mod stripped from its `dreath-mod.json` file.
     *
     * It includes this information:
     * - Website Link
     * - Discord Server Invite
     * - EMail Address
     * - Source Code Link
     * - Additional Extra Links
     */
    data class Contact(
        val website: URI,
        val discord: URI,
        val email: String,
        val source: URI,
        val extra: List<URI>
    )

    /**
     * Represents dependencies for a mod stripped from its `dreath-mod.json` file.
     *
     * It includes this information:
     * - Required Dreath Version
     * - External Dependencies
     */
    data class Dependencies(val dreath: String, val external: List<Dependency>) {
        /**
         * Represents a single dependency linking to another mod.
         *
         * It includes this information:
         * - Unique ID
         * - Minimum Required Version
         * - If Required
         * - Load Order
         */
        data class Dependency(val id: String, val minVersion: String, val required: Boolean, val load: LoadOrder) {
            /**
             * Represents a load order for a dependency in relation to the current mod.
             *
             * [AUTO] will decide automatically, which usually makes it load before the current mod.
             */
            enum class LoadOrder { BEFORE, AFTER, AUTO }
        }
    }
}
