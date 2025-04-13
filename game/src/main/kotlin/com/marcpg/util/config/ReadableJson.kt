package com.marcpg.util.config

import com.marcpg.util.Constants
import kotlinx.serialization.json.*

open class ReadableJson(protected var json: String = "{}") {
    companion object {
        const val PATH_SPLITTER = "."
    }

    protected var loaded: JsonObject? = null

    open fun load() {
        loaded = Constants.JSON.parseToJsonElement(json).jsonObject
    }

    open operator fun get(path: String) : JsonElement? {
        val parts = path.split(PATH_SPLITTER)

        var current: JsonElement? = loaded
        for (part in parts) {
            current = (current as? JsonObject)?.get(part) ?: return null
        }
        return current
    }

    fun getArray(path: String, default: List<JsonElement> = emptyList()) : List<JsonElement> {
        return (get(path) as? JsonPrimitive)?.jsonArray ?: default
    }

    fun getInt(path: String, default: Int = 0) : Int {
        return (get(path) as? JsonPrimitive)?.intOrNull ?: default
    }

    fun getLong(path: String, default: Long = 0L) : Long {
        return (get(path) as? JsonPrimitive)?.longOrNull ?: default
    }

    fun getDouble(path: String, default: Double = 0.0) : Double {
        return (get(path) as? JsonPrimitive)?.doubleOrNull ?: default
    }

    fun getFloat(path: String, default: Float = 0.0f) : Float {
        return (get(path) as? JsonPrimitive)?.floatOrNull ?: default
    }

    fun getBoolean(path: String, default: Boolean = false) : Boolean {
        return (get(path) as? JsonPrimitive)?.booleanOrNull ?: default
    }

    fun getString(path: String, default: String = "") : String {
        return (get(path) as? JsonPrimitive)?.contentOrNull ?: default
    }
}