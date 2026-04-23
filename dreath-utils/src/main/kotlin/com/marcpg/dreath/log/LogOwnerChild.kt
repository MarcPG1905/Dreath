package com.marcpg.dreath.log

/**
 * Anything that uses the same logger as some other [LoggerOwner].
 *
 * @property log The logger for this object.
 *
 * @author MarcPG
 * @since 0.1.0
 */
interface LoggerOwnerChild : LoggerOwner {
    val parent: LoggerOwner

    override val log: DreathLogger
        get() = parent.log
}

/**
 * Can be extended by other classes which use the same logger as some other [LoggerOwner].
 *
 * This is especially useful when the parent is an `object`, allowing something like this:
 * ```kt
 * object Universe : LoggerOwnerImpl("Universe")
 *
 * class Apple : LoggerOwnerChildImpl(Universe) {
 *     fun takeABite() {
 *         log.info("yum")
 *     }
 * }
 *
 * // Calling the method will then use the parent's logger:
 * Apple().takeABite()
 *
 * // Output:
 * "[06-09 13:14:15 - DONE] Universe: yum"
 * ```
 *
 * @param parent The parent logger owner with the logger to use.
 *
 * @author MarcPG
 * @since 0.1.0
 */
abstract class LoggerOwnerChildImpl(override val parent: LoggerOwner) : LoggerOwnerChild {
    // Get it only once here instead of using a getter:
    override val log: DreathLogger = parent.log
}
