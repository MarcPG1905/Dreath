package com.marcpg.common

import com.marcpg.common.command.CommandRegistrar
import com.marcpg.common.command.Console
import com.marcpg.common.mods.ModLoader
import com.marcpg.common.modules.Module
import com.marcpg.common.modules.ModuleTesting
import com.marcpg.common.util.*
import com.marcpg.common.util.config.Settings
import com.marcpg.common.util.io.FilteredOutputStream
import com.marcpg.dreath.log.DreathLogger
import com.marcpg.dreath.log.DreathLoggerFactory
import com.marcpg.dreath.log.dreathLogger
import java.nio.file.Files
import java.nio.file.Path
import kotlin.time.TimeSource

object Game {
    val MAIN_THREAD: Thread = Thread.currentThread()
    val ENVIRONMENT: Environment = Environment.current()
    lateinit var CLI_ARGS: CommandArguments

    lateinit var DIR: Path
    lateinit var SETTINGS: Settings
    lateinit var LOG: DreathLogger
    private lateinit var CONSOLE: Console

    var running: Boolean = true
        internal set

    /**
     * Called when the game starts. Runs the main logic of the game.
     * @param args The command line arguments used.
     */
    fun run(args: CommandArguments) {
        val start = TimeSource.Monotonic.markNow()

        CLI_ARGS = args
        DIR = Files.createDirectories(CLI_ARGS.gameDir)
        SETTINGS = Settings(DIR, "common").loadThis()

        // Prevents errors which aren't from the DreathLogger from being printed.
        System.setErr(FilteredOutputStream(System.err))

        DreathLoggerFactory.initialize(CLI_ARGS.gameDir.resolve("logs"))
        DreathLogger.logLevel = enumValueNoCase(SETTINGS.getString("logger.level", "fine"))
        DreathLogger.ansiFormatting = SETTINGS.getBoolean("logger.ansi-formatting", true)
        LOG = dreathLogger("Common")

        LOG.config("JVM: ${SystemInfo.jvm()}")
        LOG.config("System: ${SystemInfo.os()}")
        LOG.config("User: ${SystemInfo.user()}")
        LOG.config("Environment: ${ENVIRONMENT.name.makeCapitalized()}")

        if (CLI_ARGS.module != Module.NONE) {
            ModuleTesting.run(start)
            return
        }

        if (System.console() != null)
            CONSOLE = Console()

        LOG.info("Starting Dreath main logic...")

        LOG.info("Loading static objects...")
        Constants; DreathLoggerFactory
        LOG.fine("Loaded static objects ${start.elapsedNow()} into startup.")

        val loadMods = !CLI_ARGS.noMods && SETTINGS.getBoolean("general.load-mods", true)

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
        CommandRegistrar.register(com.marcpg.common.command.Dreath())
        CommandRegistrar.register(com.marcpg.common.command.Help())
        CommandRegistrar.register(com.marcpg.common.command.Mods())
        CommandRegistrar.register(com.marcpg.common.command.Restart())
        CommandRegistrar.register(com.marcpg.common.command.Stop())
        LOG.fine("Registered base commands ${start.elapsedNow()} into startup.")

        ENVIRONMENT.instance()?.run(start)

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

        ENVIRONMENT.instance()?.end()

        LOG.info("Done, bye!")
    }
}
