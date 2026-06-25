package client.command

import com.marcpg.dreath.command.Command
import com.marcpg.dreath.command.CommandClass
import com.marcpg.dreath.command.command
import protocol.session.SessionManager
import protocol.socket.SocketManager
import java.net.InetAddress
import java.net.InetSocketAddress

class Internal : CommandClass {
    override fun constructCommand(): Command = command("internal", listOf("client"), "Runs internal client-related actions for debugging.") {
        subcommand(name = "server", description = "Server-specific features.") {
            action {
                val server = SessionManager[0u]
                if (server == null) {
                    executor.info("Not connected to any server.")
                } else {
                    executor.info("Connected to $server")
                }
            }
            subcommand(name = "connect", description = "Connect to a server.") {
                require { SessionManager[0u] == null }

                option("address", 'a', "Address to connect to.", { InetAddress.getByName(it) }, { it is InetAddress }, required = true)
                option("port", 'p', "Port to connect to.", { it.toInt() }, { it is Int }, required = true)

                action {
                    val address = InetSocketAddress(getArg<InetAddress>("address"), getArg("port"))
                    executor.info("Server Socket Address: $address")

                    SocketManager.current.connectToServer(address)
                }
            }
        }
    }.build()
}
