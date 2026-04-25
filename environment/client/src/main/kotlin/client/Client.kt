package client

import client.command.Internal
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import com.marcpg.dreath.util.registry.RegistrarType
import com.marcpg.dreath.util.registry.Registration
import common.CommandArguments
import common.EnvironmentClass
import common.Game
import common.command.CommandRegistrar
import common.registrars.ChannelRegistrar
import common.util.SystemInfo
import common.util.elapsedMs
import java.io.File
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.time.TimeMark

object Client : EnvironmentClass<ClientConfig>() {
    override val dir: Path = (Game.CLI_ARGS as ClientCommandArguments).clientDir.createDirectories()
    override val config: ClientConfig = ClientConfig

    override fun run(start: TimeMark) {
        log.fine("Starting client logic ${start.elapsedMs()} into startup.")
        super.run(start)

        Registration.initialize(mapOf(
            RegistrarType.COMMANDS to CommandRegistrar,
            RegistrarType.PROTOCOL_CHANNELS to ChannelRegistrar,
        ))

        Registration.register(RegistrarType.COMMANDS, listOf(
            Internal(),
        ))

        log.fine("Initializing Discord rich presence...")
        DiscordRichPresence.run()
        log.success("Initialized Discord rich presence ${start.elapsedMs()} into startup.")

        log.success("Started client logic ${start.elapsedMs()} into startup.")
    }
}

class ClientCommandArguments : CommandArguments() {
    // === Working Directories ===
    val clientDir by option("-C", "--client-dir", help = "Client's working directory").path().default(File(SystemInfo.prop("user.home") + "/.config/dreath").toPath())

    // === Startup Flags ===
    val server by option("-s", "--server", help = "Server to connect to after startup").default("none")
}
