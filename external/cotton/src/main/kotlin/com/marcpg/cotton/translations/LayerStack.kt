package com.marcpg.cotton.translations

import com.marcpg.cotton.lang.ILang

internal class LayerStack {
    private val layers = mutableListOf<TranslationFile>()

    fun addAll(files: List<TranslationFile>) {
        layers.addAll(files)
    }

    fun removeAll(modId: String) {
        layers.removeAll { it.modId == modId }
    }

    fun resolve(key: String, lang: ILang): String? {
        for (file in layers.asReversed()) {
            if (file.lang == lang)
                return file.keys[key] ?: continue
        }
        return null
    }
}
