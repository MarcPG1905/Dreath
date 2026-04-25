package com.marcpg.dreath.log

import com.marcpg.libpg.color.Ansi

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
