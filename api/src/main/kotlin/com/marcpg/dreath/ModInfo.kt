package com.marcpg.dreath

/**
 * Represents information for a mod stripped from its `dreath-mod.json` file.
 *
 * It includes this information:
 * @property id A mod ID for internal identification, which should be unique.
 * @property name A mod name that will be displayed to the user.
 * @property version The current version of your mod.
 * @property description A short description of your mod and what it does.
 * @property main Class path to the mod's main class.
 * @property developers List of developers who worked on this mod.
 * @property contact All contact links related to this mod.
 * @property dependencies List of dependencies for this mod.
 *
 * @author MarcPG
 * @since 0.1.0
 */
data class ModInfo(
    val id: String,
    val name: String,
    val version: String,
    val description: String,
    val main: kotlin.reflect.KClass<out DreathMod>,
    val developers: List<String>,
    val contact: Contact,
    val dependencies: Dependencies
) {
    /**
     * Represents contact information for a mod stripped from its `dreath-mod.json` file.
     *
     * It includes this information:
     * @property website Link to this mod's website.
     * @property discord A discord server invite.
     * @property email Email address for support regarding this mod.
     * @property source Link to this mod's source code. Usually GitHub, GitLab, or Bitbucket.
     * @property extra Additional links that are not one of the types above.
     *
     * @author MarcPG
     * @since 0.1.0
     */
    data class Contact(
        val website: java.net.URI,
        val discord: java.net.URI,
        val email: String,
        val source: java.net.URI,
        val extra: List<java.net.URI>,
    )

    /**
     * Represents dependencies for a mod stripped from its `dreath-mod.json` file.
     *
     * It includes this information:
     * @property dreath The minimum Dreath API version.
     * @property external List of external dependencies, which are also mods.
     *
     * @author MarcPG
     * @since 0.1.0
     */
    data class Dependencies(
        val dreath: String,
        val external: List<Dependency>,
    ) {
        /**
         * Represents a single dependency linking to another mod.
         *
         * It includes this information:
         * @property id The mod ID of the dependency.
         * @property minVersion The minimum version of the dependency.
         * @property required Whether this dependency is required.
         * @property load When this dependency should be loaded in relation to this mod.
         *
         * @author MarcPG
         * @since 0.1.0
         */
        data class Dependency(
            val id: String,
            val minVersion: String,
            val required: Boolean,
            val load: LoadOrder,
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
