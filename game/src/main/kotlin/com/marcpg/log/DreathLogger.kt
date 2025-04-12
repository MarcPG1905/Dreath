package com.marcpg.log

import com.marcpg.libpg.log.Logger
import io.github.vyfor.kpresence.logger.ILogger
import java.io.PrintStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DreathLogger internal constructor(private val name: String, private val logFileStream: PrintStream) : Logger<PrintStream>(System.out), ILogger {
    companion object {
        private val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM HH:mm:ss")
        private const val LOG_LEVEL_LENGTH = 4
    }

    fun fine(message: String) = log(LogLevel.FINE, message)
    fun config(message: String) = log(LogLevel.CONFIG, message)
    override fun info(message: String) = log(LogLevel.INFO, message)
    override fun warn(message: String) = log(LogLevel.WARN, message)
    override fun error(message: String) = log(LogLevel.ERROR, message)

    // Ignore these to prevent KPresence from spamming the console:
    override fun trace(message: String) {}
    override fun debug(message: String) {}

    override fun error(message: String, throwable: Throwable) {
        error(message)
        throwable.printStackTrace(this)
    }

    private fun log(level: LogLevel, msg: String) {
        val timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER)
        val paddedLevel = level.abb.padStart(LOG_LEVEL_LENGTH)

        val raw = "[$timestamp - $paddedLevel] $name: $msg"

        nativeLogger.println("${level.ansi}$raw\u001B[0m")
        logFileStream.println(raw)
    }
}

fun Throwable.printStackTrace(log: Logger<*>) {
    stackTraceToString().split("\n").forEach { log.error(it) }
}
