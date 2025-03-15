package com.codetutor.countryinfoapp.util

import com.codetutor.countryinfoapp.repository.FilterByContinent
import com.codetutor.countryinfoapp.repository.FilterByDriveSide
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class FilterCriteriaFactoryProviderTest {
    @Test
    fun testContinentFilterFactory() {
        // When: retrieving the factory for continent filtering
        val factory = FilterCriteriaFactoryProvider.getFactory("Continent")
        // Then: factory should not be null and create a FilterByContinent instance.
        val criteria = factory?.createFilterCriteria("Asia")
        assertTrue(criteria is FilterByContinent)
    }

    @Test
    fun testDriveSideFilterFactory() {
        // When: retrieving the factory for drive side filtering
        val factory = FilterCriteriaFactoryProvider.getFactory("Drive Side")
        // Then: factory should not be null and create a FilterByDriveSide instance.
        val criteria = factory?.createFilterCriteria("left")
        assertTrue(criteria is FilterByDriveSide)
    }
}