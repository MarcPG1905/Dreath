package com.marcpg.dreath.log

import com.marcpg.cotton.boll.Boll
import com.marcpg.cotton.receiver.Receiver
import com.marcpg.libpg.color.Ansi
import com.marcpg.libpg.data.time.Time
import com.marcpg.libpg.log.Logger
import java.io.PrintStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DreathLogger internal constructor(override val name: String, private val logFileStream: PrintStream) : Logger<PrintStream>(System.out), Receiver {
    companion object {
        private val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM HH:mm:ss")
        private const val LOG_LEVEL_LENGTH = 4

        var logLevel = LogLevel.FINE
        var ansiFormatting = true
    }

    override val locale: Locale
        get() = TODO("Not yet implemented")

    fun fine(message: String) = log(LogLevel.FINE, message)
    fun config(message: String) = log(LogLevel.CONFIG, message)
    override fun info(message: String) = log(LogLevel.INFO, message)
    override fun warn(message: String) = log(LogLevel.WARN, message)
    override fun error(message: String) = log(LogLevel.ERROR, message)

    override fun error(message: String, throwable: Throwable) {
        error(message)
        throwable.printStackTrace(this)
    }

    private fun log(level: LogLevel, msg: String) {
        val timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER)
        val paddedLevel = level.abb.padStart(LOG_LEVEL_LENGTH)

        val raw = "[$timestamp - $paddedLevel] $name: $msg"

        if (level.weight >= logLevel.weight)
            nativeLogger.println(if (ansiFormatting) "${level.ansi}$raw\u001B[0m" else raw)
        logFileStream.println(raw)
    }

    override fun sendChat(msg: Boll) = info(msg.renderAnsi(this))

    override fun sendToast(title: Boll, msg: Boll, stay: Time) {
        info(Ansi.bold(title.renderAnsi(this)))
        info("> " + msg.renderAnsi(this))
    }

    override fun sendTitle(title: Boll, subtitle: Boll?, stay: Time) {
        info(Ansi.bold(title.renderAnsi(this)))
        if (subtitle != null)
            info("> " + subtitle.renderAnsi(this))
    }

    override fun displayMessage(msg: Boll, stay: Time, posX: Double, posY: Double) = info(msg.renderAnsi(this))

    // Cannot clear console stuff.
    override fun clearChat() {}
    override fun clearToast() {}
    override fun clearTitle() {}
    override fun clearDisplayMessage(posX: Double, posY: Double) {}
    override fun clearDisplayMessages() {}
}

fun Throwable.printStackTrace(log: Logger<*>) {
    stackTraceToString().split("\n").forEach { log.error(it) }
}

enum class LogLevel(val abb: String, val ansi: String, val weight: Int) {
    FINE("FINE", Ansi.GRAY.get(), 0),
    CONFIG("CONF", Ansi.DARK_GRAY.get(), 0),
    INFO("INFO", Ansi.WHITE.get(), 1),
    WARN("WARN", Ansi.YELLOW.get(), 2),
    ERROR("ERR", Ansi.RED.get(), 3)
}
