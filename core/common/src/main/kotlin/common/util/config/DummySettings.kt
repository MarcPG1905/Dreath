package common.util.config

import com.marcpg.libpg.config.ConfigVersion

object DummySettings : GameConfig(DummyJsonConfigProvider()) {
    override val versionHistory: List<ConfigVersion> = listOf()
    override val version: Int = 0

    override fun init() {}
}
