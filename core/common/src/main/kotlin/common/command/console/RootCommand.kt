package common.command.console

import com.marcpg.dreath.command.AbstractCommand
import com.marcpg.dreath.command.CommandContext
import com.marcpg.dreath.command.Parameter
import com.marcpg.dreath.util.registry.RegistrarType
import com.marcpg.dreath.util.registry.Registration

object RootCommand : AbstractCommand() {
    override val name: String = "root"
    override val aliases: List<String> = listOf()
    override val description: String? = null
    override val parameters: List<Parameter<*>> = listOf()
    override val action: (CommandContext.() -> Unit)? = null

    override val subcommands: Map<String, AbstractCommand>
        get() = Registration.getInstances(RegistrarType.COMMANDS).map { it() }.associateBy { it.name }

    override fun invoke(): AbstractCommand = this

    override val allNames: List<String> = listOf()
    override fun matches(name: String): Boolean = false
}
