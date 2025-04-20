package com.marcpg.common.command

import com.marcpg.dreath.command.Command
import com.marcpg.dreath.command.CommandLike
import com.marcpg.dreath.util.registry.Registrar
import com.marcpg.dreath.util.registry.RegistrarType

/**
 * The global command manager for registering and retrieving commands.
 * This should not be used excessively and only for registering commands at startup.
 */
object CommandRegistrar : Registrar<CommandLike> {
    override val type: RegistrarType = RegistrarType.COMMANDS

    private val LOADED = mutableListOf<Command>()
    private val NAMES_TO_COMMANDS = mutableMapOf<String, Command>()

    /**
     * Registers a new command. This also sets its aliases and all other things.
     * This should only be used on mod startup.
     */
    override fun register(command: CommandLike) {
        val actualCommand = command.asCommand()
        require(actualCommand.name !in NAMES_TO_COMMANDS) { "Command ${actualCommand.name} is already registered" }

        LOADED += actualCommand

        NAMES_TO_COMMANDS[actualCommand.name] = actualCommand
        actualCommand.aliases.forEach { NAMES_TO_COMMANDS.putIfAbsent(it, actualCommand) }
    }

    /**
     * Retrieves the commands given a name. This also includes aliases.
     * This should only be called internally by the game and not by mods.
     */
    fun retrieve(name: String): Command? = NAMES_TO_COMMANDS[name]

    /**
     * Returns an immutable copy of all loaded commands.
     */
    fun allCommands(): List<Command> = LOADED.toList()
}
