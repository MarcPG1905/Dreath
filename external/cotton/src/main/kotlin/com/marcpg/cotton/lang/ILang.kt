package com.marcpg.cotton.lang

import java.util.*

sealed interface ILang {
    val code: String
    val locale: Locale
}
