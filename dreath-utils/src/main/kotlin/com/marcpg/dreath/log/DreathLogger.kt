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

/**
 * Represents a logger used in the Dreath game, with some basic logging and also some utility stuff.
 *
 * New instances of this should be created using the [DreathLoggerFactory].
 *
 * @see LogLevel
 * @see DreathLoggerFactory
 *
 * @author MarcPG
 * @since 0.1.0
 */
class DreathLogger internal constructor(
    override val name: String,
    private val logFileStream: PrintStream
) : Logger<PrintStream>(DreathLoggerFactory.actualSystemOut), Receiver {
    companion object {
        private val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM HH:mm:ss")
        private const val LOG_LEVEL_LENGTH = 4

        /**
         * The minimum log level (inclusive) that can be printed.
         *
         * This can be modified at runtime and will take effect immediately after changed.
         */
        var logLevel: CustomLogLevel = LogLevel.FINE

        /**
         * If the logs should use ANSI formatting in the console.
         *
         * This can be modified at runtime and will take effect immediately after changed.
         */
        var ansiFormatting = true
    }

    override val locale: Locale = Locale.getDefault()

    /**
     * Logs a message at [fine level][LogLevel.FINE].
     * @param msg The message to log.
     */
    fun fine(msg: String?) = log(LogLevel.FINE, msg)

    /**
     * Logs a message at [configuration level][LogLevel.CONFIG].
     * @param msg The message to log.
     */
    fun config(msg: String?) = log(LogLevel.CONFIG, msg)

    /**
     * Logs a message at [success level][LogLevel.SUCCESS].
     * @param msg The message to log.
     */
    fun success(msg: String?) = log(LogLevel.SUCCESS, msg)

    /**
     * Logs a message at [info level][LogLevel.INFO].
     * @param msg The message to log.
     */
    override fun info(msg: String?) = log(LogLevel.INFO, msg)

    /**
     * Logs a message at [warning level][LogLevel.WARN].
     * @param msg The message to log.
     */
    override fun warn(msg: String?) = log(LogLevel.WARN, msg)

    /**
     * Logs a message at [error level][LogLevel.ERROR].
     * @param msg The message to log.
     */
    override fun error(msg: String?) = log(LogLevel.ERROR, msg)

    /**
     * Logs a message at [error level][LogLevel.ERROR] with a throwable that gets printed as well.
     * This will simply call [error] and Throwable.[printStackTrace].
     * @param msg The message to log.
     * @param e The throwable to print.
     */
    override fun error(msg: String?, e: Throwable) {
        error(msg)
        e.printStackTrace(this)
    }

    internal fun log(level: CustomLogLevel, msg: String?) {
        if (msg == null) return

        val timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER)
        val paddedLevel = level.abb.padStart(LOG_LEVEL_LENGTH)

        val raw = "[$timestamp - $paddedLevel] $name: $msg"

        if (level.weight >= logLevel.weight)
            nativeLogger.println(if (ansiFormatting) "${level.ansi}$raw\u001B[0m" else raw)
        logFileStream.println(raw)
    }

    override fun sendChat(msg: Boll) = info(msg.renderAnsi(this) + Ansi.RESET)

    override fun sendToast(title: Boll, msg: Boll, stay: Time) {
        info(Ansi.bold(title.renderAnsi(this) + Ansi.RESET))
        info("> " + msg.renderAnsi(this) + Ansi.RESET)
    }

    override fun sendTitle(title: Boll, subtitle: Boll?, stay: Time) {
        info(Ansi.bold(title.renderAnsi(this) + Ansi.RESET))
        if (subtitle != null)
            info("> " + subtitle.renderAnsi(this) + Ansi.RESET)
    }

    override fun displayMessage(msg: Boll, stay: Time, posX: Double, posY: Double) = info(msg.renderAnsi(this) + Ansi.RESET)

    // Cannot clear console stuff.
    override fun clearChat() {}
    override fun clearToast() {}
    override fun clearTitle() {}
    override fun clearDisplayMessage(posX: Double, posY: Double) {}
    override fun clearDisplayMessages() {}
}

/**
 * Prints a throwable to a specified logger, by getting the stack trace and
 * logging each line to the specified logger at [error level][LogLevel.ERROR].
 * @param log The logger to print the stack trace to.
 */
fun Throwable.printStackTrace(log: Logger<*>) {
    stackTraceToString().split("\n").forEach { log.error(it) }
}

/**
 * Prints a throwable to a specified logger, by getting the stack trace and
 * logging each line to the specified logger at [error level][LogLevel.ERROR].
 * @param log The logger to print the stack trace to.
 */
fun Throwable.printStackTraceFine(log: DreathLogger) {
    stackTraceToString().split("\n").forEach { log.fine(it) }
}

/**
 * Represents a logging level which can be used for getting ANSI formatting,
 * short versions, and filtering them based on weight.
 *
 * @author MarcPG
 * @since 0.1.0
 */
interface CustomLogLevel {
    /** The abbreviation used in the console and log file. Should not be longer than 4 characters. */
    val abb: String

    /** The ansi formatting to be used for any logs with this level. */
    val ansi: String

    /** The weight used for including/excluding certain levels. A higher level is more likely to be shown. */
    val weight: Int
}

/**
 * A preset enum of some default [CustomLogLevel] implementations used for the [DreathLogger].
 *
 * @author MarcPG
 * @since 0.1.0
 */
enum class LogLevel(override val abb: String, override val ansi: String, override val weight: Int) : CustomLogLevel {
    /** Configuration-related things, system properties, etc. */
    CONFIG("CONF", Ansi.DARK_GRAY.get(), 0),

    /** Debugging stuff for testing. */
    FINE("FINE", Ansi.GRAY.get(), 0),

    /** Notification that something was successful. */
    SUCCESS("DONE", Ansi.LIME.get(), 1),

    /** Usage of [System.out], which should not be done. */
    SYSTEM_OUT("SOUT", Ansi.BROWN.get(), 5),

    /** Normal logging. */
    INFO("INFO", Ansi.WHITE.get(), 5),

    /** Something that went wrong but won't cause lasting damage. */
    WARN("WARN", Ansi.YELLOW.get(), 10),

    /** Something that went wrong and may cause lasting damage, or even require stopping the application. */
    ERROR("ERR", Ansi.RED.get(), 15)
}
