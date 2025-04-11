package com.marcpg.log

import com.marcpg.Game
import java.io.PrintStream
import java.nio.file.Files
import java.nio.file.StandardOpenOption

object DreathLoggerFactory {
    private val logFileStream: PrintStream
    private val loaded: MutableMap<String, DreathLogger> = mutableMapOf()
    private val main: DreathLogger

    init {
        val logFile = Files.createDirectories(Game.CLI_ARGS.gameDir.resolve("logs")).resolve("current.log")
        Files.deleteIfExists(logFile)
        logFileStream = PrintStream(Files.newOutputStream(logFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.SYNC))

        main = create("Common")
    }

    internal fun create(name: String): DreathLogger {
        return loaded.getOrPut(name) { DreathLogger(name, logFileStream) }
    }
}

internal fun dreathLogger(name: String): DreathLogger = DreathLoggerFactory.create(name)
