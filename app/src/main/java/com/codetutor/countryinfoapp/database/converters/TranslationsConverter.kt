package com.codetutor.countryinfoapp.database.converters

import androidx.room.TypeConverter
import com.codetutor.countryinfoapp.data.Translations
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TranslationsConverter {
    @TypeConverter
    fun fromTranslations(translations: Translations): String {
        return Json.encodeToString(translations)
    }

    @TypeConverter
    fun toTranslations(data: String): Translations {
        return Json.decodeFromString<Translations>(data)
    }
}