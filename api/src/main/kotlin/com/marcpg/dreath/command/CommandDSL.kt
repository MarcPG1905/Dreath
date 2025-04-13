package com.marcpg.dreath.command

import com.marcpg.dreath.util.DreathDsl

/**
 * Utility class for building a command using a simple Kotlin DSL.
 * This provides all options needed for anything from nested commands to flags and options.
 */
class CommandBuilder(
    val name: String,
    val aliases: List<String> = mutableListOf(),
    val description: String? = null,
) : CommandLike {
    private val subcommands = mutableMapOf<String, Command>()
    private val parameters = mutableListOf<Parameter<*>>()
    private var action: CommandContext.() -> Unit = {}

    @DreathDsl
    fun subcommand(name: String, block: CommandBuilder.() -> Unit) {
        val builder = CommandBuilder(name)
        builder.block()
        subcommands[name] = builder.build()
    }

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

    @DreathDsl
    fun flag(name: String, short: Char? = null, description: String? = null) {
        parameters += Flag(name, short, description)
    }

    @DreathDsl
    fun action(block: CommandContext.() -> Unit) {
        action = block
    }

    fun build(): Command = Command(name, aliases, description, subcommands, parameters, action)

    override fun asCommand(): Command = build()
}

/**
 * Utility method for creating a new command builder with given options.
 * An additional block allows for adding other special stuff like parameters, and more.
 * Same as constructing a new [CommandBuilder].
 */
@DreathDsl
fun command(name: String, aliases: List<String> = mutableListOf(), description: String? = null, block: CommandBuilder.() -> Unit): CommandBuilder {
    return CommandBuilder(name, aliases, description).apply(block)
}

/**
 * Utility method for creating a new command builder with given options, but no block.
 * Same as constructing a new [CommandBuilder].
 */
@DreathDsl
fun command(name: String, aliases: List<String> = mutableListOf(), description: String? = null): CommandBuilder {
    return CommandBuilder(name, aliases, description)
}
