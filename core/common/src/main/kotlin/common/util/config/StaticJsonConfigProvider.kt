package common.util.config

import com.marcpg.libpg.config.ConfigEntry
import common.util.InternalConstants
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlin.io.path.Path

class StaticJsonConfigProvider(
    json: JsonObject,
    pathSplitter: String = InternalConstants.JSON_PATH_SPLITTER,
) : JsonConfigProvider(path = Path("/"), pathSplitter = pathSplitter) {
    override var configuration: JsonObject? = json

    override fun load() = error("Static JSON configuration provider cannot be loaded/saved")
    override fun save() = error("Static JSON configuration provider cannot be loaded/saved")
    override fun saveDefault(entries: Map<String, ConfigEntry<*>>) = error("Static JSON configuration provider cannot be loaded/saved")

    override fun set(path: String, value: Any?) = error("Cannot set values of static JSON configuration provider")

    fun getMapList(path: String): List<Map<String, Any?>>? = (get(path) as? JsonArray)?.mapNotNull { it as? JsonObject }?.map { it.mapValues { (_, v) -> v.toAny() } }
}
