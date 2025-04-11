package com.marcpg.env.server

import com.marcpg.Game
import com.marcpg.env.GameEnv
import com.marcpg.log.DreathLogger
import com.marcpg.log.dreathLogger
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.time.TimeSource

object ServerEnv : GameEnv() {
    override val dir: Path = Game.CLI_ARGS.serverDir.createDirectories()
    override val log: DreathLogger = dreathLogger("Server")

    override fun run(start: TimeSource.Monotonic.ValueTimeMark) {
        log.fine("Starting server logic ${start.elapsedNow()} into startup.")
    }

    override fun end() {
    }
}