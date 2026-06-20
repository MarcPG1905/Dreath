package com.marcpg.cotton.translations

import com.marcpg.cotton.lang.ILang

data class TranslationFile(
    val modId: String,
    val lang: ILang,
    val keys: Map<String, String>,
)
