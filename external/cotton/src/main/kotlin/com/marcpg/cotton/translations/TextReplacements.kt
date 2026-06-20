package com.marcpg.cotton.translations

import com.marcpg.cotton.boll.Boll
import com.marcpg.cotton.receiver.Receiver
import com.marcpg.cotton.util.boll
import kotlinx.serialization.json.*
import kotlin.reflect.KClass

abstract class TextReplacement {
    companion object {
        fun ofJson(json: JsonObject): TextReplacement {
            val clazz = Class.forName(json["type"]!!.jsonPrimitive.content)
            val method = clazz.getMethod("ofJson", JsonElement::class.java)

            return method.invoke(null, json["value"]!!) as TextReplacement
        }
    }

    abstract fun string(receiver: Receiver): String
    abstract val boll: Boll

    protected abstract fun jsonValue(): JsonElement
    protected abstract fun jsonClass(): KClass<out TextReplacement>

    fun serializeJson(): JsonElement = buildJsonObject {
        put("type", JsonPrimitive(jsonClass().java.name))
        put("value", jsonValue())
    }
}

interface TextReplacementCompanion<T : TextReplacement> {
    fun ofJson(json: JsonElement): T
}

class StaticStringReplacement(val value: String) : TextReplacement() {
    companion object : TextReplacementCompanion<StaticStringReplacement> {
        override fun ofJson(json: JsonElement): StaticStringReplacement = StaticStringReplacement(json.jsonPrimitive.content)
    }

    override fun string(receiver: Receiver): String = value

    override val boll: Boll by lazy { boll(value) }

    override fun jsonValue(): JsonElement = JsonPrimitive(value)
    override fun jsonClass(): KClass<out TextReplacement> = StaticStringReplacement::class
}

class LazyStringReplacement(val value: () -> String) : TextReplacement() {
    override fun string(receiver: Receiver): String = value()

    override val boll: Boll
        get() = boll(value())

    override fun jsonValue(): JsonElement = JsonPrimitive(value())
    override fun jsonClass(): KClass<out TextReplacement> = StaticStringReplacement::class
}

class StaticBollReplacement(val value: Boll) : TextReplacement() {
    companion object : TextReplacementCompanion<StaticBollReplacement> {
        override fun ofJson(json: JsonElement): StaticBollReplacement = StaticBollReplacement(Boll.deserializeJson(json))
    }

    override fun string(receiver: Receiver): String = value.renderString(receiver)

    override val boll: Boll by lazy { value }

    override fun jsonValue(): JsonElement = value.serializeJson()
    override fun jsonClass(): KClass<out TextReplacement> = StaticBollReplacement::class
}

class LazyBollReplacement(val value: () -> Boll) : TextReplacement() {
    override fun string(receiver: Receiver): String = value().renderString(receiver)

    override val boll: Boll
        get() = value()

    override fun jsonValue(): JsonElement = value().serializeJson()
    override fun jsonClass(): KClass<out TextReplacement> = StaticBollReplacement::class
}
