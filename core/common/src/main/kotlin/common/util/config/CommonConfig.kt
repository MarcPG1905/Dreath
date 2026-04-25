package common.util.config

import com.marcpg.dreath.log.LogLevel
import com.marcpg.libpg.config.ConfigVersion
import common.Game

object CommonConfig : GameConfig(Game.dir, "common") {
    override val versionHistory: List<ConfigVersion> = listOf(
        ConfigVersion(id = 1)
    )

    override val version: Int = 1

    val loggerLevel by enum<LogLevel>("logger.level", LogLevel.FINE)
    val loggerAnsiFormatting by boolean("logger.ansi-formatting", true)

    val loadMods by boolean("general.load-mods", true)
}
