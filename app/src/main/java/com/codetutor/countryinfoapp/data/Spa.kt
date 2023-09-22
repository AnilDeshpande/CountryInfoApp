package com.codetutor.countryinfoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Spa(
    val common: String,
    val official: String
)