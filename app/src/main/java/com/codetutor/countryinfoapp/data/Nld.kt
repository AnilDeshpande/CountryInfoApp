package com.codetutor.countryinfoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Nld(
    val common: String? = null,
    val official: String? = null
)