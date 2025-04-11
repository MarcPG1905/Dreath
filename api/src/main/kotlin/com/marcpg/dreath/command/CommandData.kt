package com.marcpg.dreath.command

import com.marcpg.dreath.util.Location
import java.util.*

/**
 * Represents any type of command parameter and should not be used raw.
 */
sealed class Parameter<T>(
    open val name: String,
    open val short: Char? = null,
    open val description: String? = null,
    open val required: Boolean = false
) {
    abstract fun parse(args: Iterator<String>): T
    abstract fun validate(value: Any?)
}

/**
 * Represents a command option which has a key and takes a value.
 *
 * This is based on the [Parameter] and extends its functionality.
 */
class Option<T>(
    override val name: String,
    override val short: Char? = null,
    val converter: (String) -> T,
    val validator: (Any?) -> Boolean = { true },
    override val description: String? = null,
    override val required: Boolean = false
) : Parameter<T>(name, short, description, required) {
    override fun parse(args: Iterator<String>): T {
        return converter(args.next())
    }

    override fun validate(value: Any?) {
        require(validator(value)) { "Invalid value for option $name." }
    }
}

/**
 * Represents a command flag which has a key and does not take a value.
 * This can be used for simple [Boolean] values which turn true when present and are false by default.
 *
 * This is based on the [Parameter] and extends its functionality.
 */
class Flag(
    override val name: String,
    override val short: Char? = null,
    override val description: String?
) : Parameter<Boolean>(name, short, description) {
    override fun parse(args: Iterator<String>): Boolean = true
    override fun validate(value: Any?) {}
}

/**
 * Represents a full command with subcommands, parameters, etc.
 * This can be registered inside the [CommandManager].
 */
@ConsistentCopyVisibility
data class Command internal constructor(
    val name: String,
    val aliases: List<String>,
    val description: String? = null,
    val subcommands: Map<String, Command> = emptyMap(),
    val parameters: List<Parameter<*>> = emptyList(),
    val action: CommandContext.() -> Unit
) : CommandLike {
    override fun asCommand(): Command = this
}

/**
 * Context provided when running a command, including all parameters with their values,
 * what executed this command, and more.
 */
class CommandContext(
    val executor: CommandExecutor
) {
    private val values = mutableMapOf<String, Any?>()

    fun values(): Map<String, Any?> = values

    @Suppress("UNCHECKED_CAST")
    fun <T> getArg(name: String): T = values[name] as T

    fun hasArg(name: String): Boolean = name in values

    fun <T> setArg(name: String, value: T) {
        values[name] = value
    }
}

/**
 * Something that executed a command with information like the location, name, and locale,
 * but also some utility methods for sending command feedback messages.
 *
 * Usually an entity, user, or the console.
 */
interface CommandExecutor {
    fun location(): Location
    fun name(): String
    fun locale(): Locale

    fun info(raw: String)
    fun warn(raw: String)
    fun error(raw: String)
}
