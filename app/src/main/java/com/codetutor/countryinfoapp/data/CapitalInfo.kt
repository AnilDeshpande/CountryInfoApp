package com.codetutor.countryinfoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class CapitalInfo(
    val latlng: List<Double>? = null
)