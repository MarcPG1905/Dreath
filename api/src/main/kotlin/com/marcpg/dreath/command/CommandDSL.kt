package com.marcpg.dreath.command

import com.marcpg.dreath.util.DreathDsl

/**
 * Utility class for building a command using a simple Kotlin DSL.
 * This provides all options needed for anything from nested commands to flags and options.
 *
 * @see command
 *
 * @property name This command's name.
 * @property aliases Optional command aliases besides its [name].
 * @property description An optional description for this command.
 *
 *
 * @author MarcPG
 * @since 0.1.0
 */
class CommandBuilder(
    val name: String,
    val aliases: List<String> = mutableListOf(),
    val description: String? = null,
) : CommandLike {
    private val subcommands = mutableMapOf<String, Command>()
    private val parameters = mutableListOf<Parameter<*>>()
    private var action: CommandContext.() -> Unit = {}

    /**
     * Appends a new sub-command to this command.
     * The following block can contain all logic that normal commands can contain.
     * @see CommandBuilder
     */
    @DreathDsl
    fun subcommand(name: String, block: CommandBuilder.() -> Unit) {
        val builder = CommandBuilder(name)
        builder.block()
        subcommands[name] = builder.build()
    }

    /**
     * Appends a new sub-command with a set action to this command.
     * The following block is the action executed when this subcommand is run.
     *
     * This does the same as using [subcommand] and setting [action] manually, just in one method.
     * @see CommandBuilder
     */
    @DreathDsl
    fun action(name: String, block: CommandContext.() -> Unit) {
        val builder = CommandBuilder(name)
        builder.action(block)
        subcommands[name] = builder.build()
    }

    /**
     * Appends a new option to this command's accepted parameters.
     * @see Option
     */
    @DreathDsl
    fun <T> option(
        name: String,
        short: Char? = null,
        converter: (String) -> T,
        validator: (Any?) -> Boolean = { true },
        description: String? = null,
        required: Boolean = false
    ) {
        parameters += Option(name, short, converter, validator, description, required)
    }

    /**
     * Appends a new flag to this command's accepted parameters.
     * @see Flag
     */
    @DreathDsl
    fun flag(name: String, short: Char? = null, description: String? = null) {
        parameters += Flag(name, short, description)
    }

    /**
     * Sets the action executed when this command is run.
     */
    @DreathDsl
    fun action(block: CommandContext.() -> Unit) {
        action = block
    }

    /**
     * Builds this command builder into a immutable [Command],
     * which can then be registered inside the [com.marcpg.dreath.util.registry.Registration].
     */
    fun build(): Command = Command(name, aliases, description, subcommands, parameters, action)

    override operator fun invoke(): Command = build()
}

/**
 * Utility method for creating a new command builder with given options.
 * An additional block allows for adding other special stuff like parameters, and more.
 * Same as constructing a new [CommandBuilder].
 *
 * @author MarcPG
 * @since 0.1.0
 */
@DreathDsl
fun command(name: String, aliases: List<String> = mutableListOf(), description: String? = null, block: CommandBuilder.() -> Unit): CommandBuilder {
    return CommandBuilder(name, aliases, description).apply(block)
}

/**
 * Utility method for creating a new command builder with given options, but no block.
 * Same as constructing a new [CommandBuilder].
 *
 * @author MarcPG
 * @since 0.1.0
 */
@DreathDsl
fun command(name: String, aliases: List<String> = mutableListOf(), description: String? = null): CommandBuilder {
    return CommandBuilder(name, aliases, description)
}
