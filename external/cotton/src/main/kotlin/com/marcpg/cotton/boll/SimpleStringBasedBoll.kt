package com.marcpg.cotton.boll

import com.marcpg.cotton.receiver.Receiver
import com.marcpg.cotton.style.Style
import kotlinx.serialization.json.*

abstract class SimpleStringBasedBoll(content: String, style: Style = Style(), followingBolls: MutableList<Boll> = mutableListOf()) : BollImpl<String>(content, style, followingBolls) {
    abstract fun content(receiver: Receiver): String
    abstract fun JsonObjectBuilder.serializationFields()

    override fun renderString(receiver: Receiver): String {
        return content(receiver) + followingBolls.joinToString("") { it.renderString(receiver) }
    }

    override fun serializeJson(): JsonElement {
        return buildJsonObject {
            put("impl", JsonPrimitive("translatable"))

            this@buildJsonObject.serializationFields()

            val style = style.serializeJson()
            if (style.jsonObject.isNotEmpty())
                put("style", style)
            if (followingBolls.isNotEmpty())
                put("following", JsonArray(followingBolls.map { it.serializeJson() }))
        }
    }

    override fun renderJson(receiver: Receiver): JsonElement {
        return buildJsonObject {
            put("impl", JsonPrimitive("translatable"))
            put("content", JsonPrimitive(content(receiver)))
            val style = style.renderJson(receiver)
            if (style.jsonObject.isNotEmpty())
                put("style", style)
            if (followingBolls.isNotEmpty())
                put("following", JsonArray(followingBolls.map { it.renderJson(receiver) }))
        }
    }

    override fun renderAnsi(receiver: Receiver): String {
        val content = StringBuilder()
        content.append(style.renderAnsi(receiver))
        content.append(content(receiver))
        for (following in followingBolls) {
            content.append(following.renderAnsi(receiver))
        }
        return content.toString()
    }

    override fun renderMarkdown(receiver: Receiver): String = ""
}
