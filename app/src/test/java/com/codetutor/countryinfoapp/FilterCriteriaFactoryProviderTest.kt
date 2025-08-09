package com.codetutor.countryinfoapp

import com.codetutor.countryinfoapp.repository.FilterByContinent
import com.codetutor.countryinfoapp.repository.FilterByDriveSide
import com.codetutor.countryinfoapp.util.FilterCriteriaFactoryProvider
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FilterCriteriaFactoryProviderTest {
    @Test
    fun testContinentFilterFactory() {
        // When: retrieving the factory for continent filtering
        val factory = FilterCriteriaFactoryProvider.getFactory("Continent")
        // Then: factory should not be null and create a FilterByContinent instance.
        assertNotNull("Factory should not be null for 'Continent'", factory)
        val criteria = factory?.createFilterCriteria("Asia")
        assertTrue("Should create FilterByContinent instance", criteria is FilterByContinent)
    }

    @Test
    fun testDriveSideFilterFactory() {
        // When: retrieving the factory for drive side filtering
        val factory = FilterCriteriaFactoryProvider.getFactory("Drive Side")
        // Then: factory should not be null and create a FilterByDriveSide instance.
        assertNotNull("Factory should not be null for 'Drive Side'", factory)
        val criteria = factory?.createFilterCriteria("left")
        assertTrue("Should create FilterByDriveSide instance", criteria is FilterByDriveSide)
    }

    @Test
    fun testUnknownKeyReturnsNull() {
        // When: retrieving the factory for an unknown filter type
        val factory = FilterCriteriaFactoryProvider.getFactory("Unknown Filter")
        
        // Then: factory should be null for unknown keys
        assertNull("Factory should return null for unknown filter types", factory)
    }

    @Test
    fun testEmptyKeyReturnsNull() {
        // When: retrieving the factory for empty string
        val factory = FilterCriteriaFactoryProvider.getFactory("")
        
        // Then: factory should be null for empty key
        assertNull("Factory should return null for empty string", factory)
    }

    @Test
    fun testCaseSensitiveKeys() {
        // When: retrieving factories with different cases
        val continentFactory = FilterCriteriaFactoryProvider.getFactory("continent") // lowercase
        val driveFactory = FilterCriteriaFactoryProvider.getFactory("DRIVE SIDE") // uppercase
        
        // Then: should return null for case-sensitive mismatches
        assertNull("Factory should be case-sensitive for 'continent'", continentFactory)
        assertNull("Factory should be case-sensitive for 'DRIVE SIDE'", driveFactory)
    }

    @Test
    fun testAllRegisteredFactories() {
        // When: retrieving all known factory types
        val continentFactory = FilterCriteriaFactoryProvider.getFactory("Continent")
        val driveSideFactory = FilterCriteriaFactoryProvider.getFactory("Drive Side")
        
        // Then: all registered factories should be available
        assertNotNull("Continent factory should be registered", continentFactory)
        assertNotNull("Drive Side factory should be registered", driveSideFactory)
        
        // And: they should create the correct filter types
        assertTrue("Continent factory should create FilterByContinent", 
                   continentFactory?.createFilterCriteria("test") is FilterByContinent)
        assertTrue("Drive Side factory should create FilterByDriveSide", 
                   driveSideFactory?.createFilterCriteria("test") is FilterByDriveSide)
    }
}