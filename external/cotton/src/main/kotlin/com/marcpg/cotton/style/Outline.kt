package com.marcpg.cotton.style

import com.marcpg.cotton.FullyDeserializable
import com.marcpg.cotton.FullySerializable
import com.marcpg.cotton.receiver.Receiver
import com.marcpg.cotton.util.Color
import kotlinx.serialization.json.*

/**
 * Represents an outline which can be used for formatting in cotton.
 *
 * @param color This outline's color.
 * @param width This outline's width, which is 1.0 by default.
 * @param height This outline's height, which is 1.0 by default.
 * @param offset This outline's offset, which is 0.0 by default.
 *
 * @author MarcPG
 * @since 0.1.0
 */
data class Outline(
    var color: Color? = null,
    var width: Double = 1.0,
    var height: Double = 1.0,
    var offset: Double = 0.0
) : FullySerializable<Outline> {
    companion object : FullyDeserializable<Outline> {
        override fun deserializeJson(data: JsonElement): Outline = Outline(
            if (data.jsonObject["color"] != null) Color.deserializeJson(data.jsonObject["color"]!!) else null,
            data.jsonObject["width"]?.jsonPrimitive?.doubleOrNull ?: 1.0,
            data.jsonObject["height"]?.jsonPrimitive?.doubleOrNull ?: 1.0,
            data.jsonObject["offset"]?.jsonPrimitive?.doubleOrNull ?: 0.0
        )

        override fun deserializeMarkdown(data: String): Outline = Outline()
    }

    override fun renderString(receiver: Receiver): String = ""

    override fun serializeJson(): JsonElement {
        return buildJsonObject {
            if (color != null)
                put("color", color!!.serializeJson())
            put("width", JsonPrimitive(width))
            put("height", JsonPrimitive(height))
            put("offset", JsonPrimitive(offset))
        }
    }
    override fun renderJson(receiver: Receiver): JsonElement {
        return buildJsonObject {
            if (color != null)
                put("color", color!!.renderJson(receiver))
            put("width", JsonPrimitive(width))
            put("height", JsonPrimitive(height))
            put("offset", JsonPrimitive(offset))
        }
    }

    override fun renderAnsi(receiver: Receiver): String = "" // No outlines in Ansi.

    override fun renderMarkdown(receiver: Receiver): String = "" // No outlines in Markdown.
}
