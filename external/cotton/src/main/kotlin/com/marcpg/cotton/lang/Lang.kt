package com.marcpg.cotton.lang

import java.util.*

enum class Lang(lang: String? = null, variant: String? = null) : ILang {
    AR, // Arabic
    BN, // Bengali
    CZ, // Chinese TODO: (Traditional/Chinese)
    DE, // German
    EN, // English
    ES, // Spanish
    HI, // Hindi
    ID, // Indonesian
    IT, // Italian
    JA, // Japanese
    LA, // Latin
    PT, // Portuguese
    RU, // Russian
    UR; // Urdu
    // TODO: Add remaining languages from ISO 639 (https://en.wikipedia.org/wiki/List_of_ISO_639_language_codes)

    companion object {
        val DEFAULT = EN
    }

    override val code: String = name
    override val locale: Locale = if (variant == null) Locale.of(lang ?: name) else Locale.of(lang ?: name, variant)
}
