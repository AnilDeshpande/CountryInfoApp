package com.codetutor.countryinfoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Bre(
    val common: String,
    val official: String
)