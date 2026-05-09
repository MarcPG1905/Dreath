package server

import com.marcpg.libpg.config.ConfigVersion
import common.util.config.GameConfig

object ServerConfig : GameConfig(Server.dir, "server") {
    override val versionHistory: List<ConfigVersion> = listOf(
        ConfigVersion(id = 1)
    )

    override val version: Int = 1

    val generalCommandLogs by boolean("general.command-logs", true)

    val generalPlayersAdminsBypassLimit by boolean("general.players.admins-bypass-limit", true)
    val generalPlayersLimit by int("general.players.limit", 10)
    val generalPlayersShowCount by boolean("general.players.show-count", true)

    val networkAbstractIps by boolean("network.abstract-ips", true)
    val networkPort by int("network.port", 42069)

    enum class CollisionSolution { PHYSICS }
}
