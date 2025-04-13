package com.marcpg.dreath

import com.marcpg.libpg.log.Logger
import java.io.File
import java.nio.file.Path

/**
 * Abstract class to be overridden by mods to denote their entry point.
 * This is required for a mod to work and should also be specified inside the `dreath-mod.json` file.
 */
abstract class DreathMod {
    /**
     * Whether this mod is currently enabled or not.
     *
     * This turns `true` after [enable] got called and back to `false` when [disable] is called.
     * The [init] method does not affect this.
     */
    var enabled = false
        private set

    private var _enabling = false
    private var _disabling = false

    /** This mod's current logger. */
    lateinit var logger: Logger<*>
        private set

    /** This mod's loaded info from the `dreath-mod.json` file. */
    lateinit var info: ModInfo
        private set

    /** This mod's base directory. Usually `./mods/<mod-id>/`. */
    lateinit var modDirectory: Path
        private set

    /** This mod's data directory. Usually `./mods/<mod-id>/data/`. */
    lateinit var dataDirectory: Path
        private set

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
        this.modDirectory = File("mods/${info.name}").toPath()
        this.dataDirectory = this.modDirectory.resolve("data")

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
        _enabling = true
        enable()
        _enabling = false
        enabled = true
    }

    /**
     * Method called by the internal game to disable this mod.
     *
     * **This should under no circumstances be called by a mod!**
     */
    fun internalDisable() {
        _disabling = true
        disable()
        _disabling = false
        enabled = false
    }
}