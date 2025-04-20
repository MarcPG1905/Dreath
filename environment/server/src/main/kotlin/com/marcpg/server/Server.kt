package com.marcpg.server

import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import com.marcpg.common.CommandArguments
import com.marcpg.common.EnvironmentClass
import com.marcpg.common.Game
import com.marcpg.common.util.SystemInfo
import com.marcpg.common.util.config.Settings
import com.marcpg.dreath.log.DreathLogger
import com.marcpg.dreath.log.dreathLogger
import java.io.File
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.time.TimeMark

object Server : EnvironmentClass() {
    override val dir: Path = (Game.CLI_ARGS as ServerCommandArguments).serverDir.createDirectories()
    override val log: DreathLogger = dreathLogger("Server")
    override val settings: Settings = Settings(dir, "server").loadThis()

    override fun run(start: TimeMark) {
        log.fine("Starting server logic ${start.elapsedNow()} into startup.")
    }
}

class ServerCommandArguments : CommandArguments() {
    // === Working Directories ===
    val serverDir by option("-S", "--server-dir", help = "Server's working directory").path().default(File(SystemInfo.prop("user.dir")).toPath())
}
