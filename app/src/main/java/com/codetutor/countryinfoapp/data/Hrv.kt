package com.codetutor.countryinfoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Hrv(
    val common: String,
    val official: String
)