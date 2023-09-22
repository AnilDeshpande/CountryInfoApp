package com.codetutor.countryinfoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Idd(
    val root: String,
    val suffixes: List<String>
)