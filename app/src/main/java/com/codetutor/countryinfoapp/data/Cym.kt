package com.codetutor.countryinfoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Cym(
    val common: String,
    val official: String
)