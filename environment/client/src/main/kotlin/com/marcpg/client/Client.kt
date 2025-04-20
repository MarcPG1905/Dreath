package com.marcpg.client

import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import com.marcpg.common.CommandArguments
import com.marcpg.common.EnvironmentClass
import com.marcpg.common.Game
import com.marcpg.common.util.SystemInfo
import com.marcpg.common.util.config.Settings
import com.marcpg.common.util.io.FilteredOutputStream
import com.marcpg.dreath.log.DreathLogger
import com.marcpg.dreath.log.dreathLogger
import com.marcpg.render.initRendering
import java.io.File
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.time.TimeMark

object Client : EnvironmentClass() {
    override val dir: Path = (Game.CLI_ARGS as ClientCommandArguments).clientDir.createDirectories()
    override val log: DreathLogger = dreathLogger("Client")
    override val settings: Settings = Settings(dir, "client").loadThis()

    override fun run(start: TimeMark) {
        log.fine("Starting client logic ${start.elapsedNow()} into startup.")

        FilteredOutputStream.filters += Regex("\\[LWJGL] GLFW_FEATURE_UNAVAILABLE error")
        FilteredOutputStream.filters += Regex("Failed to encode keysym as UTF-8")

        // Starting rendering init and loop.
        log.info("Initializing kool logic for client...")
        initRendering(this)

        log.info("Initializing Discord rich presence...")
        DiscordRichPresence.run()

        log.fine("Initialized kool logic ${start.elapsedNow()} into startup.")
    }
}

class ClientCommandArguments : CommandArguments() {
    // === Startup Flags ===
    val server by option("-s", "--server", help = "Server to connect to after startup").default("none")

    // === Working Directories ===
    val clientDir by option("-C", "--client-dir", help = "Client's working directory").path().default(File(SystemInfo.prop("user.home") + "/.config/dreath").toPath())
}
