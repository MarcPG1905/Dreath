package com.marcpg.dreath.command

import com.marcpg.dreath.util.Location
import java.util.*

/**
 * Represents any type of command parameter and should not be used raw.
 *
 * @property name The name of this parameter.
 * @property short The short key for this parameter.
 * @property description The description of this parameter.
 * @property required Whether this parameter is required or not.
 *
 * @author MarcPG
 * @since 0.1.0
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
 *
 * @property converter A function which converts the input string to the desired type.
 * @property validator A function which validates the parsed value.
 *
 * @author MarcPG
 * @since 0.1.0
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
 *
 * @author MarcPG
 * @since 0.1.0
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
 * This can be registered inside the [com.marcpg.dreath.util.registry.Registration].
 *
 * @property name The name of this command.
 * @property aliases A list of aliases for this command.
 * @property description A description of this command.
 * @property subcommands A map of subcommands for this command.
 * @property parameters A list of parameters for this command.
 * @property action The action to run when this command is executed.
 *
 * @author MarcPG
 * @since 0.1.0
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
    override operator fun invoke(): Command = this
}

/**
 * Context provided when running a command, including all parameters with their values,
 * what executed this command, and more.
 *
 * @property executor The executor of this command.
 * @property values A map of all parameters and their values.
 *
 * @author MarcPG
 * @since 0.1.0
 */
class CommandContext(val executor: CommandExecutor) {
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
 *
 * @author MarcPG
 * @since 0.1.0
 */
interface CommandExecutor {
    /** Gets the location of this executor. */
    fun location(): Location

    /** Gets the name of this executor. */
    fun name(): String

    /** Gets the locale/language of this executor. */
    fun locale(): Locale

    /** Shows a successful/neutral message to the executor. */
    fun info(raw: String)

    /** Shows a warning message to the executor. */
    fun warn(raw: String)

    /** Shows an error message to the executor. */
    fun error(raw: String)
}
