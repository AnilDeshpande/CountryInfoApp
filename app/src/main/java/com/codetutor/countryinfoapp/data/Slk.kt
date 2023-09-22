package com.codetutor.countryinfoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Slk(
    val common: String,
    val official: String
)