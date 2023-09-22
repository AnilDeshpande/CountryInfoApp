package com.codetutor.countryinfoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Maps(
    val googleMaps: String,
    val openStreetMaps: String
)