package com.marcpg.command

import com.marcpg.dreath.command.Command
import com.marcpg.dreath.command.CommandContext
import com.marcpg.dreath.command.CommandExecutor
import com.marcpg.dreath.command.CommandManager

object CommandParser {
    fun parse(executor: CommandExecutor, args: List<String>) {
        require(args.isNotEmpty()) { "No command provided" }

        val command = CommandManager.retrieve(args.first()) ?: throw IllegalArgumentException("Unknown command: ${args.first()}")
        val context = CommandContext(executor)
        parseCommand(command, args.iterator(), context)
        command.action(context)
    }

    private fun parseCommand(command: Command, args: Iterator<String>, context: CommandContext) {
        val currentArgs = args.asSequence().toMutableList()
        val iterator = currentArgs.iterator()

        while (iterator.hasNext()) {
            val arg = iterator.next()

            command.subcommands[arg]?.let {
                parseCommand(it, iterator, context)
                return
            }

            command.parameters.filter { it.name == arg || it.short?.toString() == arg }.forEach { param ->
                val value = param.parse(iterator)
                param.validate(value)
                context.setArg(param.name, value)
            }

            command.parameters.filter { it.required }.forEach {
                requireNotNull(context.values()[it.name]) { "Missing required option: ${it.name}" }
            }
        }
    }
}