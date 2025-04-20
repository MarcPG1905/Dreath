package com.marcpg.common.util.config

import com.marcpg.common.util.Constants
import kotlinx.serialization.json.JsonElement
import java.nio.file.Path

class DummySettings : Settings(Path.of(Constants.OPERATING_SYSTEM.group.nullPath)) {
    override fun load() {}
    override fun save() {}

    override operator fun set(path: String, value: Any) {}
    override fun get(path: String): JsonElement? = null
}
