package com.codetutor.countryinfoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Tur(
    val common: String,
    val official: String
)