package common.command

import com.marcpg.dreath.command.AbstractCommand
import com.marcpg.dreath.command.CommandContext
import com.marcpg.dreath.command.CommandExecutor
import common.Game
import common.util.config.CommonConfig

object CommandParser {
    fun parse(executor: CommandExecutor, args: List<String>) {
        require(args.isNotEmpty()) { "No command provided" }

        val command = CommandRegistrar.retrieve(args.first())
            ?: throw IllegalArgumentException("Unknown command: ${args.first()}")

        if (command.requirements.any { !it(executor) })
            throw IllegalArgumentException("No permission to execute $command")

        val context = CommandContext(executor)
        val action = parseCommand(command, args.drop(1).iterator(), context)
            ?: throw IllegalArgumentException("Missing subcommand/parameter after '${args.last()}'")

        if (CommonConfig.logCommands)
            Game.log.info("$executor is executing /$args")
        action.invoke(context)
    }

    private fun parseCommand(command: AbstractCommand, args: Iterator<String>, context: CommandContext): (CommandContext.() -> Unit)? {
        while (args.hasNext()) {
            val arg = args.next()

            command.subcommands[arg]?.let {
                return parseCommand(it, args, context)
            }

            command.parameters.find { "--${it.name}" == arg || (it.short != null && "-${it.short}" == arg) }?.let { param ->
                val value = param.parse(args)
                param.validate(value)
                context.setArg(param.name, value)
            }
        }

        command.parameters.filter { it.required }.forEach {
            requireNotNull(context.values()[it.name]) { "Missing required option '${it.name}'" }
        }

        return command.action
    }
}
