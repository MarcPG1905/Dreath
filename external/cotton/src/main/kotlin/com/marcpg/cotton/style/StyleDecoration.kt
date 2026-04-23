package com.marcpg.cotton.style

import com.marcpg.libpg.color.Ansi

/**
 * Represents a decoration used for styles in cotton.
 */
enum class StyleDecoration(val ansi: Ansi, val md: String) {
    /** Makes the text bold/thick. */
    BOLD(Ansi.BOLD, "**"),

    /** Makes the text italic. */
    ITALIC(Ansi.ITALIC, "*"),

    /** Makes the text underlined. */
    UNDERLINED(Ansi.UNDERLINE, "__"),

    /** Makes the text overlined (rarely supported). */
    OVERLINED(Ansi.OVERLINED, "^^"),

    /** Makes the text blink (rarely supported). */
    BLINKING(Ansi.BLINK, "!!"),

    /** Makes the text strikethrough / crossed out. */
    STRIKETHROUGH(Ansi.STRIKETHROUGH, "~~"),
}
