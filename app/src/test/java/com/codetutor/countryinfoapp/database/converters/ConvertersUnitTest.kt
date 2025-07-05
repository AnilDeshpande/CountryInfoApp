package com.codetutor.countryinfoapp.database.converters

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ConvertersUnitTest {
    lateinit var converters: Converters

    @Before
    fun setUp() {
        converters = Converters()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun test_fromStringListToJson() {
        val mockList = listOf("one", "two", "three")
        val expectedJsonString =  "[\"one\",\"two\",\"three\"]"
        val result = converters.fromStringListToJson(mockList)

        Assert.assertEquals(expectedJsonString, result)
    }

    @Test
    fun test_fromJsonToStringList() {
        val expectedList = listOf("one", "two", "three")
        val jsonString = "[\"one\",\"two\",\"three\"]"
        val result = converters.fromJsonToStringList(jsonString)

        Assert.assertEquals(expectedList, result)
    }
}