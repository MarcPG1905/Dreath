package com.marcpg.cotton.boll.text

import com.marcpg.cotton.FullyDeserializable
import com.marcpg.cotton.boll.Boll
import com.marcpg.cotton.boll.SimpleStringBasedBoll
import com.marcpg.cotton.receiver.Receiver
import com.marcpg.cotton.style.Style
import com.marcpg.libpg.lang.string
import kotlinx.serialization.json.*

class TranslatableBoll(val key: String = "", val placeholders: List<String> = listOf(), style: Style = Style(), followingBolls: MutableList<Boll> = mutableListOf()) : SimpleStringBasedBoll(key, style, followingBolls) {
    companion object : FullyDeserializable<TranslatableBoll> {
        override fun deserializeJson(data: JsonElement): TranslatableBoll {
            return TranslatableBoll(
                data.jsonObject["key"]?.jsonPrimitive?.contentOrNull ?: "",
                data.jsonObject["placeholders"]?.jsonArray?.map { it.toString() } ?: listOf(),
                Style.deserializeJson(data.jsonObject["style"]!!),
                data.jsonObject["following"]?.jsonArray?.map { Boll.deserializeJson(it) }?.toMutableList() ?: mutableListOf()
            )
        }

        override fun deserializeMarkdown(data: String): TranslatableBoll {
            TODO("Not yet implemented")
        }
    }

    override fun content(receiver: Receiver): String = receiver.locale.string(key)

    override fun JsonObjectBuilder.serializationFields() {
        put("key", JsonPrimitive(key))
        if (placeholders.isNotEmpty())
            put("placeholders", JsonArray(placeholders.map { JsonPrimitive(it) }))
    }
}
