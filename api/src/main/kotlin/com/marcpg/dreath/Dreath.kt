package com.marcpg.dreath

import com.marcpg.dreath.util.CallOriginCheck
import com.marcpg.dreath.util.DreathVersion
import com.marcpg.dreath.util.ReleaseType

/**
 * Utility object for accessing some data about the game's state.
 * @author MarcPG
 * @since 0.1.0
 */
object Dreath {
    /** The game's current version. */
    val VERSION = DreathVersion(
        verId = 0u,
        moduleName = "Dreath",
        ver = Triple(0u, 1u, 0u),
        verType = ReleaseType.ALPHA,
    )

    private var running: () -> Boolean = { false }
    private var debug: () -> Boolean = { false }

    /** Checks whether the game is currently running. */
    val isRunning get() = running()

    /** Checks whether the game is currently in debug-mode. */
    val isDebug get() = debug()

    /**
     * Method called by the internal game to initialize this object.
     *
     * **A mod should under no circumstances call this!**
     */
    fun initialize(running: () -> Boolean, debug: () -> Boolean) {
        CallOriginCheck.require(setOf("common.Game"))

        this.running = running
        this.debug = debug
    }
}
