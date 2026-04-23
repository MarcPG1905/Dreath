package com.marcpg.cotton.boll

import com.marcpg.cotton.FullyDeserializable
import com.marcpg.cotton.FullySerializable
import com.marcpg.cotton.boll.text.TextBoll
import com.marcpg.cotton.style.Outline
import com.marcpg.cotton.style.Style
import com.marcpg.cotton.style.StyleDecoration
import com.marcpg.cotton.util.Color
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Represents a boll which can be rendered, serialized, and modified.
 *
 * @author MarcPG
 * @since 0.1.0
 */
interface Boll : FullySerializable<Boll> {
    companion object : FullyDeserializable<Boll> {
        override fun deserializeJson(data: JsonElement): Boll {
            require(data is JsonObject) { "Json must be object" }

            when (data["impl"]?.jsonPrimitive?.content) {
                "text" -> return TextBoll.deserializeJson(data)
                else -> throw IllegalArgumentException("Unknown impl: ${data["impl"]?.jsonPrimitive?.content}")
            }
        }

        override fun deserializeMarkdown(data: String): Boll {
            TODO("Not yet implemented")
        }
    }

    /** Appends the following boll. */
    infix operator fun plus(boll: Boll): Boll = append(boll)

    /**
     * Appends a new boll to this boll.
     * @return The result of the operation.
     */
    fun append(boll: Boll): Boll

    /**
     * Gets this boll's full style.
     * @return This boll's full style.
     */
    fun fullStyle(): Style

    /**
     * Sets this boll's style to the provided style.
     * @return The result of the operation.
     */
    fun fullStyle(style: Style): Boll = this

    /**
     * Resets this boll's style to no styling at all.
     * @return The result of the operation.
     * @see fullStyle
     */
    fun resetStyle(): Boll = fullStyle(Style())

    /**
     * Gets this boll's color.
     * @return This boll's color.
     */
    fun color(): Color?

    /**
     * Sets this boll's color to the provided color.
     * @return The result of the operation.
     */
    fun color(color: Color?): Boll = this

    /**
     * Sets this boll's color to the provided color, if no color is set yet.
     * @return The result of the operation.
     */
    fun colorIfAbsent(color: Color?): Boll {
        if (color() == null)
            color(color)
        return this
    }

    /**
     * Gets the state of this decoration.
     * @return The decoration's state, or `null` if the decoration is not configured.
     */
    fun decorationState(decoration: StyleDecoration): Boolean?

    /**
     * Sets the state of this decoration.
     * @param state The state, which can be `true`, `false`, or `null` (=unset).
     * @return The result of the operation.
     */
    fun decoration(decoration: StyleDecoration, state: Boolean? = true): Boll

    /**
     * Sets the state of this decoration, if not configured at all yet.
     * @param state The state, which can be `true`, `false`, or `null` (=unset).
     * @return The result of the operation.
     */
    fun decorationIfAbsent(decoration: StyleDecoration, state: Boolean? = true): Boll

    /**
     * Sets the state of this decoration, if not set or set to `null`.
     * @param state The state, which can be `true`, `false`, or `null` (=unset).
     * @return The result of the operation.
     */
    fun decorationIfUnset(decoration: StyleDecoration, state: Boolean? = true): Boll {
        if (decorationState(decoration) == null)
            decoration(decoration, state)
        return this
    }

    /**
     * Gets this boll's font.
     * @return This boll's font.
     */
    fun font(): Any?

    /**
     * Sets this boll's font to the provided font.
     * @return The result of the operation.
     */
    fun font(font: Any?): Boll

    /**
     * Sets this boll's font to the provided font, if no font is set yet.
     * @return The result of the operation.
     */
    fun fontIfAbsent(font: Any?): Boll {
        if (font() == null)
            font(font)
        return this
    }

    /**
     * Gets this boll's background color.
     * @return This boll's background color.
     */
    fun backgroundColor(): Color?

    /**
     * Sets this boll's background color to the provided one.
     * @return The result of the operation.
     */
    fun backgroundColor(color: Color?): Boll

    /**
     * Sets this boll's background color to the provided one, if not set yet.
     * @return The result of the operation.
     */
    fun backgroundColorIfAbsent(color: Color?): Boll {
        if (backgroundColor() == null)
            backgroundColor(color)
        return this
    }

    /**
     * Gets this boll's outline.
     * @return This boll's outline.
     */
    fun outline(): Outline?

    /**
     * Sets this boll's outline to the provided outline.
     * @return The result of the operation.
     */
    fun outline(outline: Outline?): Boll

    /**
     * Sets this boll's outline to the provided outline, if no outline is set yet.
     * @return The result of the operation.
     */
    fun outlineIfAbsent(outline: Outline?): Boll {
        if (outline() == null)
            outline(outline)
        return this
    }
}
