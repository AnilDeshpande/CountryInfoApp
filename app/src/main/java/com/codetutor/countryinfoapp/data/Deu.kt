package com.codetutor.countryinfoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Deu(
    val common: String,
    val official: String
)