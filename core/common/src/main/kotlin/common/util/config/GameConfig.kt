package common.util.config

import com.marcpg.libpg.config.Config
import com.marcpg.libpg.config.ConfigProvider
import java.nio.file.Path

abstract class GameConfig(provider: ConfigProvider) : Config(provider) {
    constructor(dir: Path, platform: String) : this(JsonConfigProvider(dir, "$platform-config.json"))

    open fun init() {
        load()
        save()
    }
}
