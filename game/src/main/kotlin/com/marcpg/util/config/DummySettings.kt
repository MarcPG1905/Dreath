package com.marcpg.util.config

import kotlinx.serialization.json.JsonElement
import java.nio.file.Path

class DummySettings : Settings(Path.of("/dev/null")) {
    override fun load() {}
    override fun save() {}

    override operator fun set(path: String, value: Any) {}
    override fun get(path: String): JsonElement? = null
}