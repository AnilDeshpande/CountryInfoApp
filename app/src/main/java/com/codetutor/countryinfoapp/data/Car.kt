package com.codetutor.countryinfoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Car(
    val side: String? = null,
    val signs: List<String>? = null
)