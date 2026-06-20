package com.marcpg.cotton.translations

import com.marcpg.cotton.lang.CustomLang
import com.marcpg.cotton.lang.ILang
import com.marcpg.cotton.lang.Lang
import com.marcpg.cotton.receiver.Receiver

object TranslationRegistry {
    private val stack = LayerStack()
    private val providers = mutableMapOf<String, MutableList<TranslationProvider>>()
    private val languages = mutableMapOf<String, Pair<CustomLang, String>>()

    fun registerProvider(provider: TranslationProvider) {
        val list = providers.getOrPut(provider.modId) { mutableListOf() }
        list.add(provider)

        val files = runCatching { provider.load() }.getOrElse { e ->
            System.err.println("[TranslationRegistry] Failed to load provider for ${provider.modId}: ${e.message}")
            emptyList()
        }
        stack.addAll(files)
    }

    fun registerLang(lang: CustomLang, registrarId: String) {
        val existing = languages[lang.code]
        require(existing == null || existing.second == registrarId) { "Another mod already owns this language tag." }

        languages[lang.code] = lang to registrarId
    }

    fun unload(modId: String) {
        stack.removeAll(modId)
        providers.remove(modId)
        languages.entries.removeIf { (_, p) -> p.second == modId }
    }

    fun reload(modId: String) {
        val modProviders = providers[modId] ?: return
        stack.removeAll(modId)

        modProviders.forEach { stack.addAll(it.load()) }
    }

    fun getStringOrNull(key: String, receiver: Receiver, replace: Map<String, TextReplacement>? = null): String? {
        var result = resolveRaw(key, receiver.lang) ?: return null
        if (replace.isNullOrEmpty())
            return result

        replace.forEach { (placeholder, replacement) ->
            result = result.replace(
                "{$placeholder}",
                replacement.string(receiver)
            )
        }

        return result
    }

    fun getString(key: String, receiver: Receiver, replace: Map<String, TextReplacement>? = null): String = getStringOrNull(key, receiver, replace) ?: key

    private fun resolveRaw(key: String, lang: ILang): String? {
        stack.resolve(key, lang)?.let { return it }
        stack.resolve(key, Lang.DEFAULT)?.let { return it }
        return null
    }

    fun availableLanguages(): List<ILang> = languages.values.map { it.first }

    fun resolveCustomLang(tag: String): CustomLang? = languages[tag]?.first
}
