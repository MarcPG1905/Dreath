package com.marcpg.dreath.log

import java.io.PrintStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

object DreathLoggerFactory {
    private lateinit var logFileStream: PrintStream
    private lateinit var main: DreathLogger

    private val loaded: MutableMap<String, DreathLogger> = mutableMapOf()

    fun initialize(logFolder: Path) {
        if (this::main.isInitialized)
            throw IllegalStateException("DreathLoggerFactory already initialized")

        val logFile = Files.createDirectories(logFolder).resolve("current.log")
        Files.deleteIfExists(logFile)
        logFileStream = PrintStream(Files.newOutputStream(logFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.SYNC))

        main = create("Common")
    }

    internal fun create(name: String): DreathLogger {
        return loaded.getOrPut(name) { DreathLogger(name, logFileStream) }
    }
}

fun dreathLogger(name: String): DreathLogger = DreathLoggerFactory.create(name)
