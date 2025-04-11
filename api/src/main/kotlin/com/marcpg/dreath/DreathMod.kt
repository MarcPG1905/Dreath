package com.marcpg.dreath

import com.marcpg.libpg.log.Logger
import java.io.File
import java.nio.file.Path

/**
 * Abstract class to be overridden by mods to denote their entry point.
 * This is required for a mod to work and should also be specified inside the `dreath-mod.json` file.
 */
abstract class DreathMod {
    private var enabled: Boolean = false
    private var currentlyEnabling: Boolean = false
    private var currentlyDisabling: Boolean = false

    /** This mod's current logger. */
    lateinit var logger: Logger<*>

    /** This mod's loaded info from the `dreath-mod.json` file. */
    lateinit var info: ModInfo

    /** This mod's base directory. Usually `./mods/<mod-id>/`. */
    lateinit var modDirectory: Path

    /** This mod's data directory. Usually `./mods/<mod-id>/data/`. */
    lateinit var dataDirectory: Path

    /**
     * Called when the game starts and this mod was just loaded.
     *
     * This is executed before [enable] and before the game initializes any major parts like rendering,
     * which also results in most game-related stuff is not accessible yet.
     *
     * This should only be used for stuff like database connections or reading files.
     */
    protected open fun init() {}

    /**
     * Called when the game starts and this mod is enabled.
     */
    protected abstract fun enable()

    /**
     * Called when the game shuts down and this mod is disabled.
     */
    protected abstract fun disable()

    /**
     * Method called by the internal game to initialize this mod.
     *
     * **This should under no circumstances be called by a mod!**
     */
    fun internalInit(logger: Logger<*>, info: ModInfo) {
        this.logger = logger
        this.info = info

        modDirectory = File("mods/${info.name}").toPath()
        dataDirectory = modDirectory.resolve("data")

        init()
    }

    /**
     * Method called by the internal game to re-initialize this mod.
     *
     * **This should under no circumstances be called by a mod!**
     */
    fun internalReInit() = init()

    /**
     * Method called by the internal game to enable this mod.
     *
     * **This should under no circumstances be called by a mod!**
     */
    fun internalEnable() {
        this.currentlyEnabling = true
        enable()
        this.currentlyEnabling = false

        this.enabled = true
    }

    /**
     * Method called by the internal game to disable this mod.
     *
     * **This should under no circumstances be called by a mod!**
     */
    fun internalDisable() {
        this.currentlyDisabling = true
        disable()
        this.currentlyDisabling = false

        this.enabled = false
    }
}