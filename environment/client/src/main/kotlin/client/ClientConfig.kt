package client

import com.marcpg.libpg.config.ConfigVersion
import common.util.config.GameConfig

object ClientConfig : GameConfig(Client.dir, "client") {
    override val versionHistory: List<ConfigVersion> = listOf(
        ConfigVersion(id = 1)
    )

    override val version: Int = 1
}
