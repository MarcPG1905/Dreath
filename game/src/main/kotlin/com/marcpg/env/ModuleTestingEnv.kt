package com.marcpg.env

import com.marcpg.Game
import com.marcpg.log.DreathLogger
import com.marcpg.log.dreathLogger
import com.marcpg.modules.Module
import com.marcpg.modules.algorithm.MarkovChain
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
            Module.MARKOV_CHAIN -> Game.LOG.info("Name: " + MarkovChain(Game.CLI_ARGS.data.subList(2, Game.CLI_ARGS.data.size), order = Game.CLI_ARGS.data[0].toInt()).generate(Game.CLI_ARGS.data[1].toInt()))
            else -> Game.LOG.warn("No module specified. Please specify using -M flag!")
        }
    }
}