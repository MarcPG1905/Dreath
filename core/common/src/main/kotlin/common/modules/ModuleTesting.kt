package common.modules

import com.marcpg.libpg.util.enumValueNoCase
import com.marcpg.libpg.util.toTitleCase
import common.EnvironmentClass
import common.Game
import common.modules.algorithm.MarkovChain
import common.modules.namegen.LocationNameGen
import common.util.config.DummySettings
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.time.TimeMark

object ModuleTesting : EnvironmentClass<DummySettings>() {
    override val dir: Path = Game.CLI_ARGS.gameDir.createDirectories()
    override val config: DummySettings = DummySettings

    override fun run(start: TimeMark) {
        log.fine("Starting module testing...")

        val data = Game.CLI_ARGS.data
        val dataString = data.joinToString(" ")

        when (Game.CLI_ARGS.module) {
            Module.LOCATION_NAME_GEN -> log.info(dataString.toTitleCase() + " Name: " + LocationNameGen.generate(enumValueNoCase(dataString)))
            Module.MARKOV_CHAIN -> log.info("Name: " + MarkovChain(data.subList(2, data.size), data[0].toInt()).generate(data[1].toInt()))
            else -> log.error("Module not specified or invalid. Use --help for more information.")
        }
    }
}
