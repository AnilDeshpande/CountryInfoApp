package com.codetutor.countryinfoapp.data

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
@Serializable
data class Languages(
    // Using a Map to handle any language code dynamically
    val languages: Map<String, String>? = null,
)
