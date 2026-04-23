package server.command

import com.marcpg.dreath.command.Command
import com.marcpg.dreath.command.CommandClass
import com.marcpg.dreath.command.command
import protocol.session.SessionManager

class Internal : CommandClass {
    override fun constructCommand(): Command = command("internal", listOf("server"), "Runs internal server-related actions for debugging.") {
        subcommand("clients") {
            action {
                val clients = SessionManager.all()
                if (clients.isEmpty()) {
                    executor.info("There are no clients connected.")
                } else {
                    executor.info("Connected Clients (${clients.size}):")
                    clients.forEach { executor.info("- $it") }
                }
            }
        }
    }.build()
}
