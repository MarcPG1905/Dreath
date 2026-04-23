package com.marcpg.cotton.style

import com.marcpg.cotton.FullyDeserializable
import com.marcpg.cotton.FullySerializable
import com.marcpg.cotton.receiver.Receiver
import com.marcpg.cotton.util.Color
import kotlinx.serialization.json.*

/**
 * Represents a style which can be used for formatting in cotton.
 *
 * All the values are optional and null or empty by default.
 *
 * @param color This style's color.
 * @param decorations This style's list of decorations.
 * @param font This style's font.
 * @param backgroundColor This style's background color.
 * @param outline This style's outline.
 *
 * @author MarcPG
 * @since 0.1.0
 */
data class Style(
    var color: Color? = null,
    var decorations: MutableMap<StyleDecoration, Boolean?> = mutableMapOf(),
    var font: Any? = null,
    var backgroundColor: Color? = null,
    var outline: Outline? = null,
) : FullySerializable<Style> {
    companion object : FullyDeserializable<Style> {
        override fun deserializeJson(data: JsonElement): Style = Style(
            if (data.jsonObject["color"] != null) Color.deserializeJson(data.jsonObject["color"]!!) else null,
            if (data.jsonObject["decorations"] != null) data.jsonObject["decorations"]!!.jsonObject.mapKeys { enumValueOf<StyleDecoration>(it.key) }.mapValues { it.value.jsonPrimitive.booleanOrNull }.toMutableMap() else mutableMapOf(),
            null, // TODO: Deserialize font!
            if (data.jsonObject["background-color"] != null) Color.deserializeJson(data.jsonObject["background-color"]!!) else null,
            if (data.jsonObject["outline"] != null) Outline.deserializeJson(data.jsonObject["outline"]!!) else null,
        )

        override fun deserializeMarkdown(data: String): Style {
            TODO("Not yet implemented")
        }
    }

    /**
     * Merges this style with a parent style, preferring this style's value.
     *
     * If this style does not set a value, the parent's value will be used as fallback.
     */
    fun mergeWith(parent: Style): Style = Style(
        color = color ?: parent.color,
        decorations = decorations.ifEmpty { parent.decorations },
        font = font ?: parent.font,
        backgroundColor = backgroundColor ?: parent.backgroundColor,
        outline = outline ?: parent.outline
    )

    override fun renderString(receiver: Receiver): String = ""

    override fun serializeJson(): JsonElement {
        return buildJsonObject {
            if (color != null)
                put("color", color!!.serializeJson())
            if (decorations.isNotEmpty())
                put("decorations", JsonObject(decorations.filter { it.value != null }.mapKeys { it.key.name }.mapValues { JsonPrimitive(it.value) }))
//          if (font != null) TODO: Serialize font!
//              put("font", font!!)
            if (backgroundColor != null)
                put("background-color", backgroundColor!!.serializeJson())
            if (outline != null)
                put("outline", outline!!.serializeJson())
        }
    }

    override fun renderJson(receiver: Receiver): JsonElement {
        return buildJsonObject {
            if (color != null)
                put("color", color!!.renderJson(receiver))
            if (decorations.isNotEmpty())
                put("decorations", JsonObject(decorations.filter { it.value != null }.mapKeys { it.key.name }.mapValues { JsonPrimitive(it.value) }))
//          if (font != null) TODO: Serialize font!
//              put("font", font!!)
            if (backgroundColor != null)
                put("background-color", backgroundColor!!.renderJson(receiver))
            if (outline != null)
                put("outline", outline!!.renderJson(receiver))
        }
    }

    override fun renderAnsi(receiver: Receiver): String {
        val content = StringBuilder()
        if (color != null)
            content.append(color!!.renderAnsi(receiver))
        decorations.filter { it.value == true }.keys.forEach { content.append(it.ansi.get()) }
//      if (font != null) TODO: Serialize font!
//          put("font", font!!)
        if (backgroundColor != null)
            content.append(backgroundColor!!.serializeAnsiRaw().background)
        return content.toString()
    }

    override fun renderMarkdown(receiver: Receiver): String {
        val content = StringBuilder()
        decorations.filter { it.value == true }.keys.forEach { content.append(it.md) }
        return content.toString()
    }
}
