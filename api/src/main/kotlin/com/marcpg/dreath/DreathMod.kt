package com.marcpg.dreath

import com.marcpg.dreath.log.DreathLogger
import com.marcpg.dreath.util.CallOriginCheck
import java.io.File
import java.nio.file.Path

/**
 * Abstract class to be overridden by mods to denote their entry point.
 * This is required for a mod to work and should also be specified inside the `dreath-mod.json` file.
 *
 * @property logger This mod's current logger.
 * @property info This mod's loaded info from the `dreath-mod.json` file.
 * @property modDirectory This mod's base directory. Usually `./mods/<mod-id>/`.
 * @property dataDirectory This mod's data directory. Usually `./mods/<mod-id>/data/`.
 *
 * @author MarcPG
 * @since 0.1.0
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

    lateinit var logger: DreathLogger   private set
    lateinit var info: ModInfo          private set
    lateinit var modDirectory: Path     private set
    lateinit var dataDirectory: Path    private set

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
     * **A mod should under no circumstances call this!**
     */
    fun internalInit(logger: DreathLogger, info: ModInfo) {
        CallOriginCheck.require(setOf("common.mods.ModLoader"))

        this.logger = logger
        this.info = info
        this.modDirectory = File("mods/${info.name}").toPath()
        this.dataDirectory = this.modDirectory.resolve("data")

        init()
    }

    /**
     * Method called by the internal game to re-initialize this mod.
     *
     * **A mod should under no circumstances call this!**
     */
    fun internalReInit() {
        CallOriginCheck.require(setOf("common.mods.ModLoader"))

        init()
        internalEnable()
    }

    /**
     * Method called by the internal game to enable this mod.
     *
     * **A mod should under no circumstances call this!**
     */
    fun internalEnable() {
        CallOriginCheck.require(setOf("common.mods.ModLoader"))

        _enabling = true
        enable()
        _enabling = false
        enabled = true
    }

    /**
     * Method called by the internal game to disable this mod.
     *
     * **A mod should under no circumstances call this!**
     */
    fun internalDisable() {
        CallOriginCheck.require(setOf("common.mods.ModLoader"))

        _disabling = true
        disable()
        _disabling = false
        enabled = false
    }
}
