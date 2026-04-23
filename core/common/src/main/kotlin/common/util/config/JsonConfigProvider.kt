package common.util.config

import com.marcpg.libpg.config.ConfigEntry
import com.marcpg.libpg.config.ConfigProvider
import com.marcpg.libpg.config.ConfigValueType
import common.util.InternalConstants
import common.util.ResourceUtil
import kotlinx.serialization.json.*
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.io.path.writeText

open class JsonConfigProvider(
    val resource: String = "settings.json",
    path: Path,
    val pathSplitter: String = InternalConstants.JSON_PATH_SPLITTER,
) : ConfigProvider(path) {
    constructor(dir: Path, name: String, pathSplitter: String = InternalConstants.JSON_PATH_SPLITTER) :
            this(name, dir.resolve(name), pathSplitter)

    private val order = mapOf<ConfigValueType, (JsonPrimitive) -> Any>(
        ConfigValueType.BOOLEAN to { it.boolean },
        ConfigValueType.LONG to { it.long },
        ConfigValueType.DOUBLE to { it.double },
        ConfigValueType.STRING to { it.content },
    )

    protected open var configuration: JsonObject? = null

    override fun load() {
        configuration = InternalConstants.JSON.parseToJsonElement(path.readText()).jsonObject
    }

    override fun save() {
        path.writeText(InternalConstants.JSON.encodeToString(JsonObject.serializer(), configuration!!))
    }

    override fun saveDefault(entries: Map<String, ConfigEntry<*>>) {
        ResourceUtil.saveResource(resource, path)
        load()
        if (entries.isNotEmpty()) {
            entries.forEach { (key, entry) ->
                set(key, entry.get())
            }
            save()
        }
    }

    override fun approximatePathType(path: String): ConfigValueType? = when (val element = get(path)) {
        is JsonPrimitive -> order.firstNotNullOfOrNull {
            try {
                it.value(element)
                return@firstNotNullOfOrNull it.key
            } catch (_: Exception) {
                return@firstNotNullOfOrNull null
            }
        }
        is JsonArray -> ConfigValueType.LIST
        is JsonObject -> ConfigValueType.MAP
        JsonNull -> ConfigValueType.NULL
        else -> null
    }

    // TODO: Implement these:
    override fun getAllRaw(): Map<String, Any?> = mapOf()
    override fun getRaw(path: String): Any? = null
    override fun setRaw(path: String, value: Any?) {}

    open operator fun get(path: String) : JsonElement? {
        var current: JsonElement? = configuration
            ?: return null

        for (part in path.split(pathSplitter)) {
            current = (current as? JsonObject)?.get(part) ?: return null
        }
        return current
    }

    open operator fun set(path: String, value: Any?) {
        val parts = path.split(pathSplitter)
        val current: JsonObject = configuration!!

        fun setRecursive(current: JsonObject, keys: List<String>, value: JsonElement): JsonObject {
            val key = keys.first()
            return if (keys.size == 1) {
                buildJsonObject {
                    current.forEach { (k, v) -> put(k, v) }
                    put(key, value)
                }
            } else {
                val next = current[key]?.jsonObject ?: buildJsonObject { }
                buildJsonObject {
                    current.forEach { (k, v) -> put(k, v) }
                    put(key, setRecursive(next, keys.drop(1), value))
                }
            }
        }

        configuration = setRecursive(current, parts, value.toJsonElement())
    }

    fun JsonElement.toAny(): Any? = when (this) {
        is JsonPrimitive -> {
            order.forEach {
                try {
                    return it.value(this)
                } catch (_: Exception) {}
            }
        }
        is JsonArray -> map { it.toAny() }
        is JsonObject -> mapValues { it.value.toAny() }
        JsonNull -> null
    }

    fun Any?.toJsonElement(): JsonElement = when (this) {
        null -> JsonNull
        is JsonElement -> this

        is Boolean -> JsonPrimitive(this)
        is Number -> JsonPrimitive(this)
        is String -> JsonPrimitive(this)
        is Char -> JsonPrimitive(this.toString())

        is Collection<*> -> JsonArray(map { it.toJsonElement() })
        is Map<*, *> -> JsonObject(mapKeys { it.toString() }.mapValues { it.toJsonElement() })

        else -> error("Invalid value: $this")
    }

    override fun getString(path: String, def: String): String = getStringOrNull(path) ?: def
    override fun getInt(path: String, def: Int): Int = getIntOrNull(path) ?: def
    override fun getLong(path: String, def: Long): Long = getLongOrNull(path) ?: def
    override fun getDouble(path: String, def: Double): Double = getDoubleOrNull(path) ?: def
    override fun getBoolean(path: String, def: Boolean): Boolean = getBooleanOrNull(path) ?: def
    override fun getSection(path: String, def: Map<String, Any?>): Map<String, Any?> = getSectionOrNull(path) ?: def

    override fun getStringOrNull(path: String): String? = (get(path) as? JsonPrimitive)?.contentOrNull
    override fun getIntOrNull(path: String): Int? = (get(path) as? JsonPrimitive)?.intOrNull
    override fun getLongOrNull(path: String): Long? = (get(path) as? JsonPrimitive)?.longOrNull
    override fun getDoubleOrNull(path: String): Double? = (get(path) as? JsonPrimitive)?.doubleOrNull
    override fun getBooleanOrNull(path: String): Boolean? = (get(path) as? JsonPrimitive)?.booleanOrNull
    override fun getSectionOrNull(path: String): Map<String, Any?>? = (get(path) as? JsonObject)?.mapValues { e -> e.value.toAny() }

    override fun getStringList(path: String): List<String>? = (get(path) as? JsonArray)?.mapNotNull { (it as? JsonPrimitive)?.contentOrNull }
    override fun getIntList(path: String): List<Int>? = (get(path) as? JsonArray)?.mapNotNull { (it as? JsonPrimitive)?.intOrNull }
    override fun getLongList(path: String): List<Long>? = (get(path) as? JsonArray)?.mapNotNull { (it as? JsonPrimitive)?.longOrNull }
    override fun getDoubleList(path: String): List<Double>? = (get(path) as? JsonArray)?.mapNotNull { (it as? JsonPrimitive)?.doubleOrNull }
    override fun getBooleanList(path: String): List<Boolean>? = (get(path) as? JsonArray)?.mapNotNull { (it as? JsonPrimitive)?.booleanOrNull }
    override fun getSectionList(path: String): List<Map<String, Any?>>? = ((get(path) as? JsonArray))?.mapNotNull { (it as? JsonObject)?.mapValues { e -> e.value.toAny() } }

    override fun getList(path: String): List<*>? = (get(path) as? JsonArray)?.mapNotNull { it.toAny() }

    override fun setString(path: String, value: String?) = set(path, JsonPrimitive(value))
    override fun setInt(path: String, value: Int?) = set(path, JsonPrimitive(value))
    override fun setLong(path: String, value: Long?) = set(path, JsonPrimitive(value))
    override fun setDouble(path: String, value: Double?) = set(path, JsonPrimitive(value))
    override fun setBoolean(path: String, value: Boolean?) = set(path, JsonPrimitive(value))
    override fun setSection(path: String, value: Map<String, Any?>?) = set(path, value)

    override fun setStringList(path: String, value: List<String>?) = set(path, JsonArray(value?.map { JsonPrimitive(it) } ?: listOf()))
    override fun setIntList(path: String, value: List<Int>?) = set(path, JsonArray(value?.map { JsonPrimitive(it) } ?: listOf()))
    override fun setLongList(path: String, value: List<Long>?) = set(path, JsonArray(value?.map { JsonPrimitive(it) } ?: listOf()))
    override fun setDoubleList(path: String, value: List<Double>?) = set(path, JsonArray(value?.map { JsonPrimitive(it) } ?: listOf()))
    override fun setBooleanList(path: String, value: List<Boolean>?) = set(path, JsonArray(value?.map { JsonPrimitive(it) } ?: listOf()))
    override fun setSectionList(path: String, value: List<Map<String, Any?>>?) = set(path, JsonArray(value?.map { it.toJsonElement() } ?: listOf()))

    override fun setList(path: String, value: List<*>) = set(path, value.toJsonElement())
}
