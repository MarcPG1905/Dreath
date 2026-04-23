package com.marcpg.cotton.util

import com.marcpg.cotton.FullyDeserializable
import com.marcpg.cotton.FullySerializable
import com.marcpg.cotton.receiver.Receiver
import com.marcpg.libpg.color.Ansi
import kotlinx.serialization.json.*
import kotlin.math.abs

/**
 * Represents a color which can be used for formatting in cotton.
 *
 * @param red The red component of this color, as an unsigned byte from `0x00u` to `0xFFu`.
 * @param green The green component of this color, as an unsigned byte from `0x00u` to `0xFFu`.
 * @param blue The blue component of this color, as an unsigned byte from `0x00u` to `0xFFu`.
 * @param alpha The alpha (opacity) component of this color, as an unsigned byte, where `0x00u` is fully transparent, and `0xFFu` is fully opaque.
 * @param background If this color should be displayed as a background color instead of a foreground color.
 *
 * @author MarcPG
 * @since 0.1.0
 */
data class Color(
    var red: UByte = 0x00u,
    var green: UByte = 0x00u,
    var blue: UByte = 0x00u,
    var alpha: UByte = 0xFFu,
    var background: Boolean = false,
) : FullySerializable<Color> {
    companion object : FullyDeserializable<Color> {
        /** Black (#000000) */ val BLACK = Color(0x00u, 0x00u, 0x00u)
        /** White (#FFFFFF) */ val WHITE = Color(0xFFu, 0xFFu, 0xFFu)
        /** Red (#FF0000) */ val RED = Color(0xFFu, 0x00u, 0x00u)
        /** Yellow (#FFFF00) */ val YELLOW = Color(0xFFu, 0xFFu, 0x00u)
        /** Green (#00FF00) */ val GREEN = Color(0x00u, 0xFFu, 0x00u)
        /** Cyan (#00FFFF) */ val CYAN = Color(0x00u, 0xFFu, 0xFFu)
        /** Blue (#0000FF) */ val BLUE = Color(0x00u, 0x00u, 0xFFu)
        /** Pink (#FF00FF) */ val PINK = Color(0xFFu, 0x00u, 0xFFu)

        /**
         * Gets a color from a hex code.
         * @param hex The hexadecimal color code that optionally starts with a `#` and can be `#RGB`, `#ARGB`, `#RRGGBB`, or `#AARRGGBB`.
         * @return The color if it was a valid hex code.
         */
        fun fromHex(hex: String): Color {
            val cleanHex = hex.replaceFirst("^#", "").replace("[^a-fA-F0-9]".toRegex(), "")
            return when (cleanHex.length) {
                3 -> parse3DigitHex(cleanHex)
                4 -> parse4DigitHex(cleanHex)
                6 -> parse6DigitHex(cleanHex)
                8 -> parse8DigitHex(cleanHex)
                else -> throw IllegalArgumentException("Invalid hex color: $hex")
            }
        }

        /**
         * Gets a color from an ARGB value.
         * @param argb The ARGB color value.
         * @return The color if it was a valid value.
         */
        fun fromARGB(argb: Int): Color = Color(
            (argb shr 16) and 0xFF,
            (argb shr 8) and 0xFF,
            argb and 0xFF,
            (argb shr 24) and 0xFF
        )

        /**
         * Gets a color from an RGBA value.
         * @param rgba The RGBA color value.
         * @return The color if it was a valid value.
         */
        fun fromRGBA(rgba: Int): Color = Color(
            (rgba shr 24) and 0xFF,
            (rgba shr 16) and 0xFF,
            (rgba shr 8) and 0xFF,
            rgba and 0xFF,
        )

        /**
         * Gets a color from an RGB value.
         * @param rgb The RGB color value.
         * @return The color if it was a valid value.
         */
        fun fromRGB(rgb: Int): Color = Color(
            (rgb shr 16) and 0xFF,
            (rgb shr 8) and 0xFF,
            rgb and 0xFF
        )

        /**
         * Gets a color from an HSL value.
         * @param h The hue value.
         * @param s The saturation value.
         * @param l The lightness value.
         * @param alpha The alpha (opacity) component of this color, as an unsigned byte, where `0x00u` is fully transparent, and `0xFFu` is fully opaque.
         * @return The color if it was a valid value.
         */
        fun fromHsl(h: Float, s: Float, l: Float, alpha: UByte = 0xFFu): Color {
            val hue = h.rem(360f).let { if (it < 0) it + 360f else it }
            val saturation = s.coerceIn(0f, 1f)
            val lightness = l.coerceIn(0f, 1f)

            val c = (1 - abs(2 * lightness - 1)) * saturation
            val x = c * (1 - abs((hue / 60f).rem(2f) - 1))
            val m = lightness - c / 2f

            val (r1, g1, b1) = when {
                hue < 60f -> Triple(c, x, 0f)
                hue < 120f -> Triple(x, c, 0f)
                hue < 180f -> Triple(0f, c, x)
                hue < 240f -> Triple(0f, x, c)
                hue < 300f -> Triple(x, 0f, c)
                else -> Triple(c, 0f, x)
            }

            return Color(
                ((r1 + m) * 0xFF).toInt().toUByte(),
                ((g1 + m) * 0xFF).toInt().toUByte(),
                ((b1 + m) * 0xFF).toInt().toUByte(),
                alpha
            )
        }

        /**
         * Gets a color from an HSL value.
         * @param hsl The HSL (hue, saturation, lightness) value.
         * @param alpha The alpha (opacity) component of this color, as an unsigned byte, where `0x00u` is fully transparent, and `0xFFu` is fully opaque.
         * @return The color if it was a valid value.
         */
        fun fromHsl(hsl: HSL, alpha: UByte = 0xFFu): Color = fromHsl(hsl.hue, hsl.saturation, hsl.lightness, alpha)

        private fun parse3DigitHex(hex: String) = Color(
            hex[0].repeat(2).toInt(16),
            hex[1].repeat(2).toInt(16),
            hex[2].repeat(2).toInt(16),
        )

        private fun parse4DigitHex(hex: String) = Color(
            hex[1].repeat(2).toInt(16),
            hex[2].repeat(2).toInt(16),
            hex[3].repeat(2).toInt(16),
            hex[0].repeat(2).toInt(16),
        )

        private fun parse6DigitHex(hex: String) = Color(
            hex.take(2).toInt(16),
            hex.substring(2, 4).toInt(16),
            hex.substring(4, 6).toInt(16),
        )

        private fun parse8DigitHex(hex: String) = Color(
            hex.substring(2, 4).toInt(16),
            hex.substring(4, 6).toInt(16),
            hex.substring(6, 8).toInt(16),
            hex.take(2).toInt(16),
        )

        override fun deserializeJson(data: JsonElement): Color {
            return when (data) {
                is JsonPrimitive -> {
                    fromRGBA(data.intOrNull ?: 0x000000FF)
                }
                is JsonObject -> {
                    val result = fromRGBA(data["rgba"]?.jsonPrimitive?.intOrNull ?: 0x000000FF)
                    result.background = data["background"]?.jsonPrimitive?.booleanOrNull ?: false
                    return result
                }
                else -> BLACK
            }
        }

        override fun deserializeMarkdown(data: String): Color = BLACK

        private fun Char.repeat(n: Int) = toString().repeat(n)
    }

    constructor(red: Float = 0.0f, green: Float = 0.0f, blue: Float = 0.0f, alpha: Float = 1.0f) : this(
        (red * 255.0f).toUInt().toUByte(),
        (green * 255.0f).toUInt().toUByte(),
        (blue * 255.0f).toUInt().toUByte(),
        (alpha * 255.0f).toUInt().toUByte()
    )

    constructor(red: Int = 0, green: Int = 0, blue: Int = 0, alpha: Int = 255) : this(
        red.toUByte(),
        green.toUByte(),
        blue.toUByte(),
        alpha.toUByte()
    )

    /**
     * Converts this color to a hexadecimal color code in the format `#RRGGBB` or `#RRGGBBAA`.
     * @param includeAlpha If the output should include the alpha value of this color.
     * @return The hex color code as a string.
     */
    fun toHex(includeAlpha: Boolean = false): String {
        return if (includeAlpha) {
            "#%02X%02X%02X%02X".format(red, green, blue, alpha)
        } else {
            "#%02X%02X%02X".format(red, green, blue)
        }
    }

    /**
     * Converts this color to an ARGB value.
     * @return The ARGB value integer.
     */
    fun toARGB(): Int {
        return (alpha.toInt() shl 24) or
                (red.toInt() shl 16) or
                (green.toInt() shl 8) or
                blue.toInt()
    }

    /**
     * Converts this color to an RGBA value.
     * @return The RGBA value integer.
     */
    fun toRGBA(): Int {
        return (red.toInt() shl 24) or
                (green.toInt() shl 16) or
                (blue.toInt() shl 8) or
                alpha.toInt()
    }

    /**
     * Converts this color to an RGB value.
     * @return The RGB value integer.
     */
    fun toRGB(): Int {
        return (red.toInt() shl 16) or
                (green.toInt() shl 8) or
                blue.toInt()
    }

    /** Represents an HSL color value with hue, saturation, and lightness, as floats. */
    data class HSL(val hue: Float, val saturation: Float, val lightness: Float)

    /**
     * Converts this color to an HSL value.
     * @return The HSL object.
     */
    fun toHSL(): HSL {
        val r = red.toFloat() / 255.0f
        val g = green.toFloat() / 255.0f
        val b = blue.toFloat() / 255.0f

        val max = maxOf(r, g, b)
        val min = minOf(r, g, b)
        val delta = max - min

        val lightness = (max + min) / 2.0f

        val saturation = when {
            delta == 0.0f -> 0.0f
            else -> delta / (1.0f - abs(2 * lightness - 1))
        }.coerceIn(0.0f, 1.0f)

        val hue = when {
            delta == 0.0f -> 0.0f
            max == r -> ((g - b) / delta).rem(6.0f) * 60.0f
            max == g -> ((b - r) / delta + 2.0f) * 60.0f
            else -> ((r - g) / delta + 4.0f) * 60.0f
        }.let { if (it < 0) it + 360f else it }

        return HSL(hue, saturation, lightness)
    }

    /** Creates a copy of this color with the set red value. */
    fun withRed(red: UByte): Color = Color(red, green, blue, alpha)

    /** Creates a copy of this color with the set green value. */
    fun withGreen(green: UByte): Color = Color(red, green, blue, alpha)

    /** Creates a copy of this color with the set blue value. */
    fun withBlue(blue: UByte): Color = Color(red, green, blue, alpha)

    /** Creates a copy of this color with the set alpha value. */
    fun withAlpha(alpha: UByte): Color = Color(red, green, blue, alpha)

    override fun renderString(receiver: Receiver): String = ""

    override fun serializeJson(): JsonElement = if (background) {
        buildJsonObject {
            put("rgba", toRGBA())
            put("background", JsonPrimitive(true))
        }
    } else {
        JsonPrimitive(toRGBA())
    }
    override fun renderJson(receiver: Receiver): JsonElement = serializeJson()

    override fun renderAnsi(receiver: Receiver): String = if (background) serializeAnsiRaw().background else serializeAnsiRaw().get()

    /** Serializes this color in ANSI, returning an [Ansi] object instead of a string. */
    fun serializeAnsiRaw(): Ansi = Ansi.fromRGB(red.toInt(), green.toInt(), blue.toInt())

    override fun renderMarkdown(receiver: Receiver): String = "" // No colors in Markdown.
}
