package com.codetutor.countryinfoapp.database

import androidx.room.TypeConverter
import com.codetutor.countryinfoapp.data.CapitalInfo
import com.codetutor.countryinfoapp.data.Car
import com.codetutor.countryinfoapp.data.Currency
import com.codetutor.countryinfoapp.data.Flags
import com.codetutor.countryinfoapp.data.Idd
import com.codetutor.countryinfoapp.data.Languages
import com.codetutor.countryinfoapp.data.Maps
import com.codetutor.countryinfoapp.data.Name
import com.codetutor.countryinfoapp.data.NativeName
import com.codetutor.countryinfoapp.data.Translations
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.serializer
import org.intellij.lang.annotations.Language

class Converters {
    private val jsonFormat = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromStringListToJson(value: List<String>?): String? {
        return value?.let { jsonFormat.encodeToString(it) }
    }

    @TypeConverter
    fun fromJsonToStringList(value: String?): List<String>? {
        return value?.let { jsonFormat.decodeFromString(it) }
    }

    @TypeConverter
    fun fromCapitalInfoToJson(capitalInfo: CapitalInfo?): String? {
        return capitalInfo?.let { jsonFormat.encodeToString(it) }
    }

    @TypeConverter
    fun fromJsonToCapitalInfo(json: String?): CapitalInfo? {
        return json?.let { jsonFormat.decodeFromString(it) }
    }

    // Example converters for other complex types like List<Double>, Map<String, Currency>, etc.
    @TypeConverter
    fun fromDoubleListToJson(value: List<Double>?): String? {
        return value?.let { jsonFormat.encodeToString(it) }
    }

    @TypeConverter
    fun fromCarToJson(car: Car?): String? {
        return car?.let { jsonFormat.encodeToString(it) }
    }

    @TypeConverter
    fun fromJsonToCar(json: String?): Car? {
        return json?.let { jsonFormat.decodeFromString(it) }
    }

    @TypeConverter
    fun fromFlagsToJson(flags: Flags?): String? {
        return flags?.let { jsonFormat.encodeToString(it) }
    }

    @TypeConverter
    fun fromJsonToFlags(json: String?): Flags? {
        return json?.let { jsonFormat.decodeFromString(it) }
    }

    @TypeConverter
    fun fromMapsToJson(maps: Maps?): String? {
        return maps?.let { jsonFormat.encodeToString(it) }
    }

    @TypeConverter
    fun fromJsonToMaps(json: String?): Maps? {
        return json?.let { jsonFormat.decodeFromString(it) }
    }

    @TypeConverter
    fun fromLanguagesToJson(languages: Languages?): String? {
        return languages?.let { jsonFormat.encodeToString(it) }
    }

    @TypeConverter
    fun fromJsonToLanguages(json: String?): Languages? {
        return json?.let { jsonFormat.decodeFromString(it) }
    }

    @TypeConverter
    fun fromIddToJson(idd: Idd?): String? {
        return idd?.let { jsonFormat.encodeToString(it) }
    }

    @TypeConverter
    fun fromJsonToIdd(json: String?): Idd? {
        return json?.let { jsonFormat.decodeFromString(it) }
    }

    @TypeConverter
    fun fromNameToJson(name: Name?): String? {
        return name?.let { jsonFormat.encodeToString(it) }
    }

    @TypeConverter
    fun fromJsonToName(json: String?): Name? {
        return json?.let { jsonFormat.decodeFromString(it) }
    }

    @TypeConverter
    fun fromNativeNameToJson(nativeName: NativeName?): String? {
        return nativeName?.let { jsonFormat.encodeToString(it) }
    }

    @TypeConverter
    fun fromJsonToNativeName(json: String?): NativeName? {
        return json?.let { jsonFormat.decodeFromString(it) }
    }

    @TypeConverter
    fun fromJsonToDoubleList(value: String?): List<Double>? {
        return value?.let { jsonFormat.decodeFromString(it) }
    }

    // Add similar converters for Map and other custom objects
    @TypeConverter
    fun fromCurrencyMapToJson(value: Map<String, Currency>?): String? {
        return value?.let { jsonFormat.encodeToString(it) }
    }

    @TypeConverter
    fun fromJsonToCurrencyMap(value: String?): Map<String, Currency>? {
        return value?.let { jsonFormat.decodeFromString(it) }
    }
}
