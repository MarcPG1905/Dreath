package common.command.console

import com.marcpg.dreath.command.AbstractCommand
import com.marcpg.dreath.command.Option
import com.marcpg.dreath.util.registry.RegistrarType
import com.marcpg.dreath.util.registry.Registration
import org.jline.reader.LineReader
import org.jline.reader.ParsedLine

interface ConsolePart {
    fun dissect(reader: LineReader, line: ParsedLine): CommandDissectionResult? {
        // Split by '=', because "--param=value" and "--param value" are treated equally.
        val words = line.words().flatMap { it.split('=') }

        val options = mutableMapOf<String, String>()
        val flags = mutableListOf<String>()

        var currentCommand: AbstractCommand? = null
        var finalCommand = false
        var currentParameter: String? = null

        for (word in words.take(words.size - 1)) {
            when {
                word.startsWith('-') -> {
                    if (currentCommand == null) return null
                    finalCommand = true

                    if (currentParameter != null)
                        flags.add(currentParameter)

                    currentParameter = word.substringAfter(word.dropWhile { it == '-' })
                }
                currentParameter != null -> {
                    options[currentParameter] = word
                    currentParameter = null
                }
                !finalCommand -> {
                    val registeredCommands = Registration.getInstances(RegistrarType.COMMANDS).map { it() }
                    currentCommand = (currentCommand?.subcommands?.values ?: registeredCommands).firstOrNull { it.matches(word) } ?: break
                }
            }
        }

        return CommandDissectionResult(currentCommand, options, flags)
    }

    fun possibleChildren(command: AbstractCommand, options: Map<String, String>, flags: List<String>): Map<String, String?> {
        val values = mutableMapOf<String, String?>()

        for (subcommand in command.subcommands.values) {
            for (name in subcommand.allNames) {
                values[name] = subcommand.description
            }
        }

        command.parameters
            .filterNot { it.name in options || it.name in flags }
            .forEach { parameter ->
                if (parameter is Option) {
                    values["--${parameter.name}="] = parameter.description
                } else {
                    values["--${parameter.name}"] = parameter.description
                }
                values["-${parameter.short}"] = parameter.description
            }

        return values
    }
}

data class CommandDissectionResult(
    val command: AbstractCommand?,
    val options: Map<String, String>,
    val flags: List<String>,
)
