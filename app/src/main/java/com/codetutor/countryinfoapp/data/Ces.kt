package com.codetutor.countryinfoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Ces(
    val common: String,
    val official: String
)