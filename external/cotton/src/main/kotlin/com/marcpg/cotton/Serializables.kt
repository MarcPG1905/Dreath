package com.marcpg.cotton

import com.marcpg.cotton.receiver.Receiver
import kotlinx.serialization.json.JsonElement

/**
 * Something that can be rendered as a plain string.
 *
 * @author MarcPG
 * @since 0.1.0
 */
interface StringSerializable<T> {
    /** Renders this as a plain string using the receiver's settings. */
    fun renderString(receiver: Receiver): String
}

/**
 * Something that can be rendered in the ANSI format.
 *
 * @author MarcPG
 * @since 0.1.0
 */
interface AnsiSerializable<T> {
    /** Renders this as a string formatted with ANSI using the receiver's settings. */
    fun renderAnsi(receiver: Receiver): String
}

/**
 * Something serializable in the JSON format.
 *
 * @author MarcPG
 * @since 0.1.0
 */
interface JsonSerializable<T> {
    /**
     * Serialize this in JSON.
     *
     * Includes the data required for reconstructing and then rendering to another player.
     */
    fun serializeJson(): JsonElement

    /**
     * Render this in JSON using the receiver's settings.
     *
     * Already rendered, meaning that translations are already translated for example.
     */
    fun renderJson(receiver: Receiver): JsonElement
}

/**
 * Something deserializable from the JSON format.
 *
 * @author MarcPG
 * @since 0.1.0
 */
interface JsonDeserializable<T> {
    /** Deserializes the JSON input to the type T. */
    fun deserializeJson(data: JsonElement): T
}

/**
 * Something that can be rendered in the markdown format.
 *
 * @author MarcPG
 * @since 0.1.0
 */
interface MarkdownSerializable<T> {
    /**
     * Render this in markdown using the receiver's settings.
     *
     * Already rendered, meaning that translations are already translated for example.
     */
    fun renderMarkdown(receiver: Receiver): String
}

/**
 * Something deserializable (un-renderable) in the JSON format.
 *
 * Keep in mind, due to markdown's nature, translations or similar can not be reconstructed.
 *
 * @author MarcPG
 * @since 0.1.0
 */
interface MarkdownDeserializable<T> {
    /** Deserializes the markdown input to the type T. */
    fun deserializeMarkdown(data: String): T
}


/**
 * Something serializable in all the formats (currently string, JSON, ANSI, and markdown).
 *
 * ### Serialize
 * Includes the data required for rendering to another player.
 *
 * ### Render
 * Already rendered, meaning that translations are already translated for example.
 *
 * @author MarcPG
 * @since 0.1.0
 *
 * @see StringSerializable
 * @see JsonSerializable
 * @see AnsiSerializable
 * @see MarkdownSerializable
 */
interface FullySerializable<T> : StringSerializable<T>, JsonSerializable<T>, AnsiSerializable<T>, MarkdownSerializable<T>

/**
 * Something deserializable from the JSON and markdown formats.
 *
 * @author MarcPG
 * @since 0.1.0
 *
 * @see JsonDeserializable
 * @see MarkdownDeserializable
 */
interface FullyDeserializable<T> : JsonDeserializable<T>, MarkdownDeserializable<T>
