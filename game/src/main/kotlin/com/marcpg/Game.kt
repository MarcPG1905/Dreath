package com.marcpg

import com.marcpg.log.DreathLogger
import com.marcpg.log.DreathLoggerFactory
import com.marcpg.log.dreathLogger
import com.marcpg.util.*
import java.nio.file.Files
import java.nio.file.Path
import kotlin.time.TimeSource

object Game {
    lateinit var MAIN_THREAD: Thread
    lateinit var CLI_ARGS: CommandArguments

    lateinit var DIR: Path
    lateinit var LOG: DreathLogger

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

        LOG.info("Starting Dreath main logic...")
        val start = TimeSource.Monotonic.markNow()

        LOG.config("JVM: ${SystemInfo.jvm()}")
        LOG.config("System: ${SystemInfo.os()}")
        LOG.config("User: ${SystemInfo.user()}")
        LOG.config("Environment: ${CLI_ARGS.environment.name.makeCapitalized()}")

        LOG.info("Loading static objects...")
        Constants; DreathLoggerFactory
        LOG.fine("Loaded static objects ${start.elapsedNow()} into startup.")

        CLI_ARGS.environment.instances().forEach { it.run(start) }

        LOG.info("Done! Took ${start.elapsedNow()}.")

        onShutdownProcess { end() }
        keepAliveProcess()
    }

    /**
     * Called when the game ends. Shuts down the game.
     */
    private fun end() {
        LOG.info("Shutting down...")

        CLI_ARGS.environment.instances().forEach { it.end() }

        LOG.info("Done, bye!")
    }
}