package com.marcpg.cotton.lang

import java.util.Locale

data class CustomLang(
    override val code: String,
    override val locale: Locale,
) : ILang
