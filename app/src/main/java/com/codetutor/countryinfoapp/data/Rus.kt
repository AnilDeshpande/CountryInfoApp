package com.codetutor.countryinfoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Rus(
    val common: String,
    val official: String
)