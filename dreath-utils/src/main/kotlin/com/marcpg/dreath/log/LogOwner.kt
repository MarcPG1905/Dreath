package com.marcpg.dreath.log

/**
 * Anything that has its own logger.
 * @author MarcPG
 * @since 0.1.0
 */
interface LoggerOwner {
    /** The logger for this object. */
    val log: DreathLogger
}

/**
 * Can be extended by other classes which own a custom [DreathLogger].
 * This will automatically create the logger as an immutable value based on the provided name.
 *
 * @param loggerName The name/prefix of the logger that will be created. If not specified, this will use the class name.
 *
 * @author MarcPG
 * @since 0.1.0
 */
abstract class LoggerOwnerImpl(loggerName: String? = null) : LoggerOwner {
    override val log: DreathLogger = dreathLogger(loggerName ?: this::class.simpleName ?: "Unknown")
}
