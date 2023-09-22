package com.codetutor.countryinfoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Est(
    val common: String,
    val official: String
)