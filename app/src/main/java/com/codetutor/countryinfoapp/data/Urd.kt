package com.codetutor.countryinfoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Urd(
    val common: String,
    val official: String
)