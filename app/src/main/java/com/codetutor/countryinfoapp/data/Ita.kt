package com.codetutor.countryinfoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Ita(
    val common: String,
    val official: String
)