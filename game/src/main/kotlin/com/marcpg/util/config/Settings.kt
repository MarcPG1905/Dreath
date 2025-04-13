package com.marcpg.util.config

import com.marcpg.util.Constants
import com.marcpg.util.ResourceUtil
import kotlinx.serialization.json.*
import java.nio.file.Path
import kotlin.io.path.name
import kotlin.io.path.notExists
import kotlin.io.path.readText
import kotlin.io.path.writeText

open class Settings(private val path: Path) : ReadableJson() {
    constructor(dir: Path, name: String) : this(dir.resolve("$name-settings.json"))

    fun loadThis(): Settings {
        load()
        return this
    }

    override fun load() {
        saveInternally()
        json = path.readText()
        super.load()
    }

    open fun save() {
        saveInternally()
        path.writeText(Constants.JSON.encodeToString(JsonObject.serializer(), loaded!!))
    }

    private fun saveInternally() {
        if (path.notExists())
            ResourceUtil.saveResource(path.name, path)
    }

    open operator fun set(path: String, value: Any) {
        val parts = path.split(PATH_SPLITTER)
        val current: JsonObject = loaded!!

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

        val newValue = when (value) {
            is String -> JsonPrimitive(value)
            is Number -> JsonPrimitive(value)
            is Boolean -> JsonPrimitive(value)
            is Char -> JsonPrimitive(value.toString())
            is JsonElement -> value
            else -> throw IllegalArgumentException("Invalid value: $value")
        }

        loaded = setRecursive(current, parts, newValue)
    }
}