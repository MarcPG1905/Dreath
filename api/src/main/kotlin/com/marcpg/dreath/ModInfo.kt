package com.marcpg.dreath

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
 * @author MarcPG
 * @since 0.1.0
 */
data class ModInfo(
    /** A mod ID for internal identification, which should be unique. */
    val id: String,

    /** A mod name that will be displayed to the user. */
    val name: String,

    /** The current version of your mod. */
    val version: String,

    /** A short description of your mod and what it does. */
    val description: String,

    /** Class path to the mod's main class. */
    val main: KClass<out DreathMod>,

    /** List of developers who worked on this mod. */
    val developers: List<String>,

    /** All contact links related to this mod. */
    val contact: Contact,

    /** List of dependencies for this mod. */
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
        /** Link to this mod's website. */
        val website: URI,

        /** A discord server invite. */
        val discord: URI,

        /** Email address for support regarding this mod. */
        val email: String,

        /** Link to this mod's source code. Usually GitHub, GitLab, or Bitbucket. */
        val source: URI,

        /** Additional links that are not one of the types above. */
        val extra: List<URI>
    )

    /**
     * Represents dependencies for a mod stripped from its `dreath-mod.json` file.
     *
     * It includes this information:
     * - Required Dreath Version
     * - External Dependencies
     */
    data class Dependencies(
        /** The minimum Dreath API version. */
        val dreath: String,

        /** List of external dependencies, which are also mods. */
        val external: List<Dependency>
    ) {
        /**
         * Represents a single dependency linking to another mod.
         *
         * It includes this information:
         * - Unique ID
         * - Minimum Required Version
         * - If Required
         * - Load Order
         */
        data class Dependency(
            /** This dependency's mod ID. */
            val id: String,

            /** The minimum version of this dependency. */
            val minVersion: String,

            /** Whether this dependency is required. */
            val required: Boolean,

            /** When this dependency should be loaded in relation to this mod. */
            val load: LoadOrder
        ) {
            /**
             * Represents a load order for a dependency in relation to the current mod.
             */
            enum class LoadOrder {
                /** Before the current mod. */
                BEFORE,

                /** After the current mod. */
                AFTER,

                /** Decide automatically, which usually makes it load before the current mod. */
                AUTO,
            }
        }
    }
}
