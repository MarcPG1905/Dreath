package server

import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import com.marcpg.dreath.util.registry.RegistrarType
import com.marcpg.dreath.util.registry.Registration
import common.ChannelRegistrar
import common.CommandArguments
import common.EnvironmentClass
import common.Game
import common.command.CommandRegistrar
import common.util.SystemInfo
import common.util.elapsedMs
import engine.EventRegistrar
import engine.FeatureRegistrar
import server.command.Internal
import server.testing.StartRender
import java.io.File
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.time.TimeMark

object Server : EnvironmentClass<ServerConfig>() {
    override val dir: Path = (Game.CLI_ARGS as ServerCommandArguments).serverDir.createDirectories()
    override val config: ServerConfig = ServerConfig

    override fun run(start: TimeMark) {
        log.fine("Starting server logic ${start.elapsedMs()} into startup.")
        super.run(start)

        Registration.initialize(mapOf(
            RegistrarType.COMMANDS to CommandRegistrar,
            RegistrarType.PROTOCOL_CHANNELS to ChannelRegistrar,
            RegistrarType.FEATURES to FeatureRegistrar,
            RegistrarType.EVENTS to EventRegistrar,
        ))

        Registration.register(RegistrarType.COMMANDS, listOf(
            Internal(),
            StartRender(),
        ))

//        DataSocket; GameplaySocket; SocialSocket

        log.success("Started server logic ${start.elapsedMs()} into startup.")
    }
}

class ServerCommandArguments : CommandArguments() {
    // === Working Directories ===
    val serverDir by option("-S", "--server-dir", help = "Server's working directory").path().default(File(SystemInfo.prop("user.dir")).toPath())
}
