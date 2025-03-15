package com.codetutor.countryinfoapp.database.converters

import com.codetutor.countryinfoapp.database.converters.Converters
import com.codetutor.countryinfoapp.data.Car
import com.codetutor.countryinfoapp.data.CapitalInfo
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ConverterCriticalTests {
    private lateinit var converters: Converters

    @Before
    fun setUp() {
        converters = Converters()
    }

    // Test non-null conversion of a string list and its reversion.
    @Test
    fun test_stringListConversion() {
        val stringList = listOf("one", "two", "three")
        val json = converters.fromStringListToJson(stringList)
        val resultList = converters.fromJsonToStringList(json)

        Assert.assertEquals(stringList, resultList)
    }

    // Test that null string list returns null for both conversion functions.
    @Test
    fun test_stringListConversionWithNull() {
        Assert.assertNull(converters.fromStringListToJson(null))
        Assert.assertNull(converters.fromJsonToStringList(null))
    }

    // Test non-null conversion of a sample object (e.g. CapitalInfo) and its reversion.
    @Test
    fun test_capitalInfoConversion() {
        // Create a sample CapitalInfo instance based on your data model.
        val capitalInfo = CapitalInfo(/* set required properties */)
        val json = converters.fromCapitalInfoToJson(capitalInfo)
        val resultObject = converters.fromJsonToCapitalInfo(json)

        Assert.assertEquals(capitalInfo, resultObject)
    }

    // Test conversion for an object type (e.g. Car).
    @Test
    fun test_carConversion() {
        // Create a sample Car instance based on your data model.
        val car = Car(/* set required properties */)
        val json = converters.fromCarToJson(car)
        val resultObject = converters.fromJsonToCar(json)

        Assert.assertEquals(car, resultObject)
    }

    // Test that null object returns null conversion for Car.
    @Test
    fun test_carConversionWithNull() {
        Assert.assertNull(converters.fromCarToJson(null))
        Assert.assertNull(converters.fromJsonToCar(null))
    }
}