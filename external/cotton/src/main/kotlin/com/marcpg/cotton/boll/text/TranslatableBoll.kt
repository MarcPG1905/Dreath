package com.marcpg.cotton.boll.text

import com.marcpg.cotton.FullyDeserializable
import com.marcpg.cotton.boll.Boll
import com.marcpg.cotton.boll.SimpleStringBasedBoll
import com.marcpg.cotton.receiver.Receiver
import com.marcpg.cotton.style.Style
import com.marcpg.cotton.translations.TextReplacement
import com.marcpg.cotton.translations.translate
import kotlinx.serialization.json.*

class TranslatableBoll(val key: String = "", val replacements: Map<String, TextReplacement> = mapOf(), style: Style = Style(), followingBolls: MutableList<Boll> = mutableListOf()) : SimpleStringBasedBoll(key, style, followingBolls) {
    companion object : FullyDeserializable<TranslatableBoll> {
        override fun deserializeJson(data: JsonElement): TranslatableBoll {
            return TranslatableBoll(
                data.jsonObject["key"]?.jsonPrimitive?.contentOrNull ?: "",
                data.jsonObject["replacements"]?.jsonObject!!.mapValues { TextReplacement.ofJson(it.value.jsonObject) },
                Style.deserializeJson(data.jsonObject["style"]!!),
                data.jsonObject["following"]?.jsonArray?.map { Boll.deserializeJson(it) }?.toMutableList() ?: mutableListOf()
            )
        }

        override fun deserializeMarkdown(data: String): TranslatableBoll {
            TODO("Not yet implemented")
        }
    }

    override fun content(receiver: Receiver): String = receiver.translate(key, replacements)

    override fun implementationId(): String = "translatable"

    override fun JsonObjectBuilder.serializationFields() {
        put("key", JsonPrimitive(key))

        if (replacements.isEmpty()) return
        putJsonObject("replacements") {
            replacements.forEach { (key, value) -> put(key, value.serializeJson()) }
        }
    }
}
