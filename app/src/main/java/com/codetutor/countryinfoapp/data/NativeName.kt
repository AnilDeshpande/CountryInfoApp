package com.codetutor.countryinfoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class NativeName(
    // Using a Map to handle any language code (fra, nor, etc.) dynamically
    val nativeNames: Map<String, NameTranslation>? = null,
)

@Serializable
data class NameTranslation(
    val official: String? = null,
    val common: String? = null,
)
