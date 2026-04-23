package common.util.config

import com.marcpg.libpg.config.ConfigEntry
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlin.io.path.Path

class DummyJsonConfigProvider : JsonConfigProvider(path = Path("/")) {
    override var configuration: JsonObject? = null

    override fun load() {}
    override fun save() {}
    override fun saveDefault(entries: Map<String, ConfigEntry<*>>) {}

    override fun getAllRaw(): Map<String, Any?> = mapOf()
    override fun getRaw(path: String): Any? = null
    override fun setRaw(path: String, value: Any?) {}

    override fun get(path: String): JsonElement? = null
    override fun set(path: String, value: Any?) {}
}
