package com.marcpg.common.modules

import com.marcpg.common.EnvironmentClass
import com.marcpg.common.Game
import com.marcpg.common.modules.algorithm.MarkovChain
import com.marcpg.common.modules.namegen.LocationNameGen
import com.marcpg.common.util.config.DummySettings
import com.marcpg.common.util.config.Settings
import com.marcpg.common.util.enumValueNoCase
import com.marcpg.common.util.makeCapitalized
import com.marcpg.dreath.log.DreathLogger
import com.marcpg.dreath.log.dreathLogger
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.time.TimeMark

object ModuleTesting : EnvironmentClass() {
    override val dir: Path = Game.CLI_ARGS.gameDir.createDirectories()
    override val log: DreathLogger = dreathLogger("Module-Testing")
    override val settings: Settings = DummySettings()

    override fun run(start: TimeMark) {
        log.fine("Starting module testing...")

        val data = Game.CLI_ARGS.data
        val dataString = data.joinToString(" ")

        when (Game.CLI_ARGS.module) {
            Module.LOCATION_NAME_GEN -> log.info(dataString.makeCapitalized() + " Name: " + LocationNameGen.generate(enumValueNoCase(dataString)))
            Module.MARKOV_CHAIN -> log.info("Name: " + MarkovChain(data.subList(2, data.size), data[0].toInt()).generate(data[1].toInt()))
            // TODO: Test peg-protocol module here.
            else -> log.error("Module not specified or invalid. Use --help for more information.")
        }
    }
}