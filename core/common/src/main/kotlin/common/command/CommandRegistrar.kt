package common.command

import com.marcpg.dreath.command.Command
import com.marcpg.dreath.command.CommandLike
import com.marcpg.dreath.util.registry.Registrar
import com.marcpg.dreath.util.registry.RegistrarType

/**
 * The global command manager for registering and retrieving commands.
 * This should not be used excessively and only for registering commands at startup.
 */
object CommandRegistrar : Registrar<CommandLike> {
    override val type: RegistrarType<CommandLike> = RegistrarType.COMMANDS

    private val loaded = mutableListOf<Command>()
    private val namesToCommands = mutableMapOf<String, Command>()

    /**
     * Registers a new command. This also sets its aliases and all other things.
     * This should only be used on mod startup.
     */
    override fun register(instance: CommandLike) {
        val actualCommand = instance()
        require(actualCommand is Command) { "Cannot register custom implementation of Command" }
        require(actualCommand.name !in namesToCommands) { "Command '${actualCommand.name}' is already registered" }

        loaded += actualCommand

        namesToCommands[actualCommand.name] = actualCommand
        actualCommand.aliases.forEach { namesToCommands.putIfAbsent(it, actualCommand) }
    }

    override fun loaded(): List<CommandLike> = loaded.toList() // Only return a copy, never the actual mutable list.

    /**
     * Retrieves the commands given a name. This also includes aliases.
     * This should only be called internally by the game and not by mods.
     */
    fun retrieve(name: String): Command? = namesToCommands[name]

    /**
     * Returns an immutable copy of all loaded commands.
     */
    fun allCommands(): List<Command> = loaded.toList()
}
