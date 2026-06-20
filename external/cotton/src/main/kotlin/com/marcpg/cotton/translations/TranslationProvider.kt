package com.marcpg.cotton.translations

interface TranslationProvider {
    val modId: String

    fun load(): List<TranslationFile>
}
