package com.marcpg.cotton.translations

import com.marcpg.cotton.receiver.Receiver

fun Receiver.translateOrNull(key: String, replace: Map<String, TextReplacement>? = null) = TranslationRegistry.getStringOrNull(key, this, replace)
fun Receiver.translate(key: String, replace: Map<String, TextReplacement>? = null) = TranslationRegistry.getString(key, this, replace)
