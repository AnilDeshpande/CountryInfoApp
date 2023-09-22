package com.codetutor.countryinfoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Fin(
    val common: String,
    val official: String
)