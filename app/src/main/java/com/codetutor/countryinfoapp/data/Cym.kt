package com.codetutor.countryinfoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Cym(
    val common: String? = null,
    val official: String? = null
)