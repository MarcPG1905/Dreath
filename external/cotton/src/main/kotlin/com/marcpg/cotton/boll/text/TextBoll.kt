package com.marcpg.cotton.boll.text

import com.marcpg.cotton.FullyDeserializable
import com.marcpg.cotton.boll.Boll
import com.marcpg.cotton.boll.SimpleStringBasedBoll
import com.marcpg.cotton.receiver.Receiver
import com.marcpg.cotton.style.Style
import kotlinx.serialization.json.*

class TextBoll(content: String = "", style: Style = Style(), followingBolls: MutableList<Boll> = mutableListOf()) : SimpleStringBasedBoll(content, style, followingBolls) {
    companion object : FullyDeserializable<TextBoll> {
        override fun deserializeJson(data: JsonElement): TextBoll {
            return TextBoll(
                data.jsonObject["content"]?.jsonPrimitive?.contentOrNull ?: "",
                Style.deserializeJson(data.jsonObject["style"]!!),
                data.jsonObject["following"]?.jsonArray?.map { Boll.deserializeJson(it) }?.toMutableList() ?: mutableListOf()
            )
        }

        override fun deserializeMarkdown(data: String): TextBoll {
            TODO("Not yet implemented")
        }
    }

    override fun content(receiver: Receiver): String = content

    override fun JsonObjectBuilder.serializationFields() {
        put("content", JsonPrimitive(content))
    }
}
