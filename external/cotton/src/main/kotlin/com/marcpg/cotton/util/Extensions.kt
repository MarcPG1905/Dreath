package com.marcpg.cotton.util

import com.marcpg.cotton.boll.Boll
import com.marcpg.cotton.boll.text.TextBoll
import com.marcpg.cotton.boll.text.TranslatableBoll
import com.marcpg.cotton.style.Outline
import com.marcpg.cotton.style.Style
import com.marcpg.cotton.style.StyleDecoration

/**
 * Creates a new text-based boll based on the raw text input and a style.
 */
fun boll(text: String, style: Style) = TextBoll(text, style)

/**
 * Creates a new text-based boll based on the raw text input and optional style options.
 */
fun boll(text: String, color: Color? = null, decorations: Map<StyleDecoration, Boolean?> = mapOf(), font: Any? = null, backgroundColor: Color? = null, outline: Outline? = null) =
    boll(text, Style(color, decorations.toMutableMap(), font, backgroundColor, outline))

/**
 * Creates a new translatable boll based on the translation key, a style, and optionally some placeholders.
 */
fun translatable(key: String, style: Style, placeholders: List<String> = listOf()) = TranslatableBoll(key, placeholders, style)

/**
 * Creates a new translatable boll based on the translation key and optionally some style options or placeholders.
 */
fun translatable(key: String, color: Color? = null, decorations: Map<StyleDecoration, Boolean?> = mapOf(), font: Any? = null, backgroundColor: Color? = null, outline: Outline? = null, placeholders: List<String> = listOf()) =
    translatable(key, Style(color, decorations.toMutableMap(), font, backgroundColor, outline), placeholders)

/** Appends a single whitespace to this boll. */
fun Boll.appendSpace(): Boll = append(boll(" "))

/** Appends four whitespaces to this boll. */
fun Boll.append4Spaces(): Boll = append(boll("    "))

/** Appends a new line / linebreak to this boll. This is essentially just `\n`. */
fun Boll.appendNewLine(): Boll = append(boll("\n"))
