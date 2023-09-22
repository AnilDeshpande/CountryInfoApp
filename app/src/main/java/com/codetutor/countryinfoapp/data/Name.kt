package com.codetutor.countryinfoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Name(
    val common: String,
    val nativeName: NativeName,
    val official: String
)