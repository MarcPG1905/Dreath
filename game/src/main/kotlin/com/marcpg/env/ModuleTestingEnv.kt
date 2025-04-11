package com.marcpg.env

import com.marcpg.Game
import com.marcpg.log.DreathLogger
import com.marcpg.log.dreathLogger
import com.marcpg.util.config.DummySettings
import com.marcpg.util.config.Settings
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.time.TimeSource

object ModuleTestingEnv : GameEnv() {
    override val dir: Path = Game.CLI_ARGS.serverDir.createDirectories()
    override val log: DreathLogger = dreathLogger("Module-Testing")
    override val settings: Settings = DummySettings()

    override fun run(start: TimeSource.Monotonic.ValueTimeMark) {
        log.fine("Starting module testing...")

        when (Game.CLI_ARGS.module) {
            else -> Game.LOG.warn("No module specified. Please specify using -M flag!")
        }
    }
}