package com.codetutor.countryinfoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Name(
    val common: String? = null,
    val nativeName: NativeName? = null,
    val official: String? = null
)