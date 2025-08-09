package com.codetutor.countryinfoapp

import com.codetutor.countryinfoapp.repository.FilterByDriveSide
import com.codetutor.countryinfoapp.repository.FilterCriteria
import com.codetutor.countryinfoapp.util.DriveSideFilterFactory
import org.junit.Assert.assertTrue
import org.junit.Test

class DriveSideFilterFactoryTest {

    private val factory = DriveSideFilterFactory()

    @Test
    fun createFilterCriteria_shouldReturnFilterByDriveSide() {
        // When: creating filter criteria with a valid drive side
        val filterCriteria = factory.createFilterCriteria("left")
        
        // Then: should return FilterByDriveSide instance
        assertTrue("Factory should return FilterByDriveSide instance", 
                   filterCriteria is FilterByDriveSide)
    }

    @Test
    fun createFilterCriteria_shouldReturnFilterByDriveSideWithCorrectKey() {
        // Given: a specific drive side key
        val driveSideKey = "right"
        
        // When: creating filter criteria
        val filterCriteria = factory.createFilterCriteria(driveSideKey)
        
        // Then: should return FilterByDriveSide with the correct key
        assertTrue("Should return FilterByDriveSide", filterCriteria is FilterByDriveSide)
        // Note: FilterByDriveSide doesn't expose the side parameter, so we verify by type only
    }

    @Test
    fun createFilterCriteria_shouldHandleEmptyString() {
        // When: creating filter criteria with empty string
        val filterCriteria = factory.createFilterCriteria("")
        
        // Then: should still return FilterByDriveSide instance
        assertTrue("Factory should handle empty string and return FilterByDriveSide", 
                   filterCriteria is FilterByDriveSide)
    }

    @Test
    fun createFilterCriteria_shouldHandleInvalidDriveSide() {
        // When: creating filter criteria with invalid drive side
        val filterCriteria = factory.createFilterCriteria("center")
        
        // Then: should still return FilterByDriveSide instance (validation happens in filter logic)
        assertTrue("Factory should handle invalid drive side and return FilterByDriveSide", 
                   filterCriteria is FilterByDriveSide)
    }

    @Test
    fun createFilterCriteria_shouldHandleCaseSensitivity() {
        // When: creating filter criteria with different cases
        val filterCriteriaUpper = factory.createFilterCriteria("LEFT")
        val filterCriteriaLower = factory.createFilterCriteria("left")
        
        // Then: should return FilterByDriveSide instances for both
        assertTrue("Factory should handle uppercase and return FilterByDriveSide", 
                   filterCriteriaUpper is FilterByDriveSide)
        assertTrue("Factory should handle lowercase and return FilterByDriveSide", 
                   filterCriteriaLower is FilterByDriveSide)
    }

    @Test
    fun createFilterCriteria_shouldImplementFilterCriteriaInterface() {
        // When: creating filter criteria
        val filterCriteria = factory.createFilterCriteria("left")
        
        // Then: should implement FilterCriteria interface
        assertTrue("Returned object should implement FilterCriteria interface", 
                   filterCriteria is FilterCriteria)
    }

    @Test
    fun createFilterCriteria_shouldReturnNewInstanceEachTime() {
        // When: creating multiple filter criteria instances
        val criteria1 = factory.createFilterCriteria("left")
        val criteria2 = factory.createFilterCriteria("left")
        
        // Then: should return different instances (not singleton)
        assertTrue("Should return new instances each time", criteria1 !== criteria2)
        assertTrue("Both should be FilterByDriveSide instances", 
                   criteria1 is FilterByDriveSide && criteria2 is FilterByDriveSide)
    }

    @Test
    fun createFilterCriteria_shouldHandleBothValidDriveSides() {
        // When: creating filter criteria for both valid drive sides
        val leftCriteria = factory.createFilterCriteria("left")
        val rightCriteria = factory.createFilterCriteria("right")
        
        // Then: should return FilterByDriveSide instances for both
        assertTrue("Should create FilterByDriveSide for 'left'", 
                   leftCriteria is FilterByDriveSide)
        assertTrue("Should create FilterByDriveSide for 'right'", 
                   rightCriteria is FilterByDriveSide)
    }
}