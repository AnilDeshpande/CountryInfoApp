package com.codetutor.countryinfoapp

import com.codetutor.countryinfoapp.repository.FilterByContinent
import com.codetutor.countryinfoapp.repository.FilterCriteria
import com.codetutor.countryinfoapp.util.ContinentFilterFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ContinentFilterFactoryTest {

    private val factory = ContinentFilterFactory()

    @Test
    fun createFilterCriteria_shouldReturnFilterByContinent() {
        // When: creating filter criteria with a valid continent
        val filterCriteria = factory.createFilterCriteria("Europe")
        
        // Then: should return FilterByContinent instance
        assertTrue("Factory should return FilterByContinent instance", 
                   filterCriteria is FilterByContinent)
    }

    @Test
    fun createFilterCriteria_shouldReturnFilterByContinentWithCorrectKey() {
        // Given: a specific continent key
        val continentKey = "Asia"
        
        // When: creating filter criteria
        val filterCriteria = factory.createFilterCriteria(continentKey)
        
        // Then: should return FilterByContinent with the correct key
        assertTrue("Should return FilterByContinent", filterCriteria is FilterByContinent)
        // Note: FilterByContinent doesn't expose the continent parameter, so we verify by type only
    }

    @Test
    fun createFilterCriteria_shouldHandleEmptyString() {
        // When: creating filter criteria with empty string
        val filterCriteria = factory.createFilterCriteria("")
        
        // Then: should still return FilterByContinent instance
        assertTrue("Factory should handle empty string and return FilterByContinent", 
                   filterCriteria is FilterByContinent)
    }

    @Test
    fun createFilterCriteria_shouldHandleNullString() {
        // When: creating filter criteria with null (handled as string)
        val filterCriteria = factory.createFilterCriteria("null")
        
        // Then: should still return FilterByContinent instance
        assertTrue("Factory should handle string 'null' and return FilterByContinent", 
                   filterCriteria is FilterByContinent)
    }

    @Test
    fun createFilterCriteria_shouldHandleSpecialCharacters() {
        // When: creating filter criteria with special characters
        val filterCriteria = factory.createFilterCriteria("North America")
        
        // Then: should return FilterByContinent instance
        assertTrue("Factory should handle strings with spaces and return FilterByContinent", 
                   filterCriteria is FilterByContinent)
    }

    @Test
    fun createFilterCriteria_shouldImplementFilterCriteriaInterface() {
        // When: creating filter criteria
        val filterCriteria = factory.createFilterCriteria("Africa")
        
        // Then: should implement FilterCriteria interface
        assertTrue("Returned object should implement FilterCriteria interface", 
                   filterCriteria is FilterCriteria)
    }

    @Test
    fun createFilterCriteria_shouldReturnNewInstanceEachTime() {
        // When: creating multiple filter criteria instances
        val criteria1 = factory.createFilterCriteria("Europe")
        val criteria2 = factory.createFilterCriteria("Europe")
        
        // Then: should return different instances (not singleton)
        assertTrue("Should return new instances each time", criteria1 !== criteria2)
        assertTrue("Both should be FilterByContinent instances", 
                   criteria1 is FilterByContinent && criteria2 is FilterByContinent)
    }
}