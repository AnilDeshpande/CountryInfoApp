package com.codetutor.countryinfoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Srp(
    val common: String,
    val official: String
)