package com.marcpg

import com.marcpg.command.Console
import com.marcpg.dreath.command.CommandManager
import com.marcpg.log.DreathLogger
import com.marcpg.log.DreathLoggerFactory
import com.marcpg.log.dreathLogger
import com.marcpg.mods.ModLoader
import com.marcpg.util.*
import com.marcpg.util.config.Settings
import java.nio.file.Files
import java.nio.file.Path
import kotlin.time.TimeSource

object Game {
    lateinit var MAIN_THREAD: Thread
    lateinit var CLI_ARGS: CommandArguments
    lateinit var SETTINGS: Settings

    lateinit var DIR: Path
    lateinit var LOG: DreathLogger
    private lateinit var CONSOLE: Console

    var running: Boolean = true
        internal set

    /**
     * Called when the game starts. Runs the main logic of the game.
     * @param args The command line arguments used.
     */
    fun run(args: CommandArguments) {
        MAIN_THREAD = Thread.currentThread()
        CLI_ARGS = args

        DIR = Files.createDirectories(CLI_ARGS.gameDir)
        LOG = dreathLogger("Common")

        SETTINGS = Settings(DIR, "common").loadThis()

        DreathLogger.logLevel = enumValueNoCase(SETTINGS.getString("logger.level", "fine"))
        DreathLogger.ansiFormatting = SETTINGS.getBoolean("logger.ansi-formatting", true)

        val loadMods = !CLI_ARGS.noMods && SETTINGS.getBoolean("general.load-mods", true)

        if (System.console() != null)
            CONSOLE = Console()

        LOG.info("Starting Dreath main logic...")
        val start = TimeSource.Monotonic.markNow()

        LOG.config("JVM: ${SystemInfo.jvm()}")
        LOG.config("System: ${SystemInfo.os()}")
        LOG.config("User: ${SystemInfo.user()}")
        LOG.config("Environment: ${CLI_ARGS.environment.name.makeCapitalized()}")

        LOG.info("Loading static objects...")
        Constants; DreathLoggerFactory
        LOG.fine("Loaded static objects ${start.elapsedNow()} into startup.")

        if (loadMods) {
            LOG.info("Loading mods...")
            val loaded = ModLoader.init()
            if (loaded.first > 0) {
                LOG.fine("Found and initialized ${loaded.first} mods ${start.elapsedNow()} into startup.")
            } else {
                LOG.fine("No mods found.")
            }
            LOG.fine("Total mods loaded: ${loaded.second}")
        } else {
            LOG.config("Skipping mod loading due to flag or configuration.")
        }

        LOG.info("Registering base commands...")
        CommandManager.register(com.marcpg.command.Dreath())
        CommandManager.register(com.marcpg.command.Help())
        CommandManager.register(com.marcpg.command.Mods())
        CommandManager.register(com.marcpg.command.Restart())
        CommandManager.register(com.marcpg.command.Stop())
        LOG.fine("Registered base commands ${start.elapsedNow()} into startup.")

        CLI_ARGS.environment.instances().forEach { it.run(start) }

        if (loadMods) {
            LOG.info("Enabling mods...")
            ModLoader.enable()
            LOG.info("All mods were successfully enabled.")
        } // No else, because we don't need to send the same message twice.

        LOG.info("Done! Took ${start.elapsedNow()}.")

        onShutdownProcess { end() }
        keepAliveProcess()
    }

    /**
     * Called when the game ends. Shuts down the game.
     */
    private fun end() {
        LOG.info("Shutting down...")

        LOG.info("Stopping console, commands will no longer be received...")
        if (System.console() != null)
            CONSOLE.stop()

        LOG.info("Unloading ${ModLoader.loaded()} mods...")
        ModLoader.unload()

        CLI_ARGS.environment.instances().forEach { it.end() }

        LOG.info("Done, bye!")
    }
}