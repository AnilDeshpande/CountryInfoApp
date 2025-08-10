package com.codetutor.countryinfoapp.database.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.codetutor.countryinfoapp.TestDataFactory
import com.codetutor.countryinfoapp.repository.doubles.FakeCountryDao
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for CountryDao operations using a fake implementation
 * Tests all DAO operations: insert, query, update, delete
 * 
 * Note: These are unit tests using a fake DAO. For true integration tests
 * with Room database, see androidTest folder.
 */
class CountryDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var countryDao: FakeCountryDao

    @Before
    fun setUp() {
        countryDao = FakeCountryDao()
    }

    // Test: Insert & getAll
    @Test
    fun test_insertAll_and_getAllCountries() = runTest {
        // Arrange
        val testCountries = TestDataFactory.createCountryList(3)
        
        // Act
        countryDao.insertAll(testCountries)
        val retrievedCountries = countryDao.getAllCountries()
        
        // Assert
        assertEquals(testCountries.size, retrievedCountries.size)
        assertEquals(testCountries[0].name?.common, retrievedCountries[0].name?.common)
        assertEquals(testCountries[1].name?.common, retrievedCountries[1].name?.common)
        assertEquals(testCountries[2].name?.common, retrievedCountries[2].name?.common)
    }

    @Test
    fun test_insertAll_emptyList() = runTest {
        // Arrange
        val emptyList = emptyList<com.codetutor.countryinfoapp.data.Country>()
        
        // Act
        countryDao.insertAll(emptyList)
        val retrievedCountries = countryDao.getAllCountries()
        
        // Assert
        assertEquals(0, retrievedCountries.size)
    }

    @Test
    fun test_insertAll_replaceOnConflict() = runTest {
        // Arrange
        val country1 = TestDataFactory.createCountry(id = 1, name = "Original Country")
        val country1Updated = TestDataFactory.createCountry(id = 1, name = "Updated Country")
        
        // Act
        countryDao.insertAll(listOf(country1))
        countryDao.insertAll(listOf(country1Updated)) // FakeDao clears and adds new list
        val retrievedCountries = countryDao.getAllCountries()
        
        // Assert
        assertEquals(1, retrievedCountries.size)
        assertEquals("Updated Country", retrievedCountries[0].name?.common)
    }

    // Test: getCountriesByContinent
    @Test
    fun test_getCountriesByContinent_matchingCountries() = runTest {
        // Arrange - Note: FakeDao checks continents field, not region
        val countries = listOf(
            TestDataFactory.createCountry(id = 1, name = "Germany", region = "Europe").copy(continents = listOf("Europe")),
            TestDataFactory.createCountry(id = 2, name = "France", region = "Europe").copy(continents = listOf("Europe")),
            TestDataFactory.createCountry(id = 3, name = "Italy", region = "Europe").copy(continents = listOf("Europe")),
            TestDataFactory.createCountry(id = 4, name = "Japan", region = "Asia").copy(continents = listOf("Asia"))
        )
        
        // Act
        countryDao.insertAll(countries)
        val retrievedEuropeanCountries = countryDao.getCountriesByContinent("Europe")
        
        // Assert
        assertEquals(3, retrievedEuropeanCountries.size)
        retrievedEuropeanCountries.forEach { country ->
            assertTrue(country.continents?.contains("Europe") == true)
        }
    }

    @Test
    fun test_getCountriesByContinent_noMatches() = runTest {
        // Arrange
        val countries = listOf(
            TestDataFactory.createCountry(id = 1, name = "Germany").copy(continents = listOf("Europe")),
            TestDataFactory.createCountry(id = 2, name = "France").copy(continents = listOf("Europe"))
        )
        
        // Act
        countryDao.insertAll(countries)
        val retrievedAfricanCountries = countryDao.getCountriesByContinent("Africa")
        
        // Assert
        assertEquals(0, retrievedAfricanCountries.size)
    }

    @Test
    fun test_getCountriesByContinent_partialMatch() = runTest {
        // Arrange - Note: FakeDao checks continents field exactly, not with LIKE
        val countries = listOf(
            TestDataFactory.createCountry(id = 1, name = "USA").copy(continents = listOf("North America")),
            TestDataFactory.createCountry(id = 2, name = "Brazil").copy(continents = listOf("South America")),
            TestDataFactory.createCountry(id = 3, name = "Germany").copy(continents = listOf("Europe"))
        )
        
        // Act
        countryDao.insertAll(countries)
        val northAmericanCountries = countryDao.getCountriesByContinent("North America")
        
        // Assert
        assertEquals(1, northAmericanCountries.size)
        assertTrue(northAmericanCountries[0].continents?.contains("North America") == true)
    }

    // Test: updateCapital
    @Test
    fun test_updateCapital_existingCountry() = runTest {
        // Arrange
        val country = TestDataFactory.createCountry(
            id = 1, 
            name = "Test Country",
            capital = listOf("Old Capital")
        )
        val newCapital = listOf("New Capital", "Second Capital")
        
        // Act
        countryDao.insertAll(listOf(country))
        val updateResult = countryDao.updateCapital(newCapital, 1)
        val updatedCountry = countryDao.getAllCountries().first { it.id == 1 }
        
        // Assert
        assertEquals(1, updateResult) // Should return 1 for successful update
        assertEquals(newCapital, updatedCountry.capital)
    }

    @Test
    fun test_updateCapital_nonExistentCountry() = runTest {
        // Arrange
        val newCapital = listOf("New Capital")
        
        // Act
        val updateResult = countryDao.updateCapital(newCapital, 999) // Non-existent ID
        
        // Assert
        assertEquals(0, updateResult) // Should return 0 for no rows updated
    }

    @Test
    fun test_updateCapital_multipleCapitals() = runTest {
        // Arrange
        val country = TestDataFactory.createCountry(
            id = 1,
            capital = listOf("Single Capital")
        )
        val multipleCapitals = listOf("Capital One", "Capital Two", "Capital Three")
        
        // Act
        countryDao.insertAll(listOf(country))
        val updateResult = countryDao.updateCapital(multipleCapitals, 1)
        val updatedCountry = countryDao.getAllCountries().first { it.id == 1 }
        
        // Assert
        assertEquals(1, updateResult)
        assertEquals(3, updatedCountry.capital?.size)
        assertEquals(multipleCapitals, updatedCountry.capital)
    }

    // Test: delete
    @Test
    fun test_delete_existingCountry() = runTest {
        // Arrange
        val countries = TestDataFactory.createCountryList(3)
        val countryToDelete = countries[1]
        
        // Act
        countryDao.insertAll(countries)
        val initialSize = countryDao.getAllCountries().size
        countryDao.delete(countryToDelete)
        val finalSize = countryDao.getAllCountries().size
        
        // Assert
        assertEquals(3, initialSize)
        assertEquals(2, finalSize) // Size should decrease by 1
        
        // Verify the specific country was deleted
        val remainingCountries = countryDao.getAllCountries()
        assertFalse(remainingCountries.any { it.id == countryToDelete.id })
    }

    @Test
    fun test_delete_nonExistentCountry() = runTest {
        // Arrange
        val countries = TestDataFactory.createCountryList(2)
        val nonExistentCountry = TestDataFactory.createCountry(id = 999, name = "Non-existent")
        
        // Act
        countryDao.insertAll(countries)
        val initialSize = countryDao.getAllCountries().size
        countryDao.delete(nonExistentCountry) // This should not crash
        val finalSize = countryDao.getAllCountries().size
        
        // Assert
        assertEquals(initialSize, finalSize) // Size should remain the same
    }

    @Test
    fun test_delete_allCountries() = runTest {
        // Arrange
        val countries = TestDataFactory.createCountryList(3)
        
        // Act
        countryDao.insertAll(countries)
        assertEquals(3, countryDao.getAllCountries().size)
        
        // Delete all countries one by one
        countries.forEach { country ->
            countryDao.delete(country)
        }
        
        // Assert
        assertEquals(0, countryDao.getAllCountries().size)
    }

    // Test: updateCountry
    @Test
    fun test_updateCountry_existingCountry() = runTest {
        // Arrange - Note: FakeDao updates based on name matching, not ID
        val originalCountry = TestDataFactory.createCountry(
            id = 1,
            name = "Original Name",
            population = 1000000
        )
        val updatedCountry = originalCountry.copy(
            population = 2000000 // Keep same name for FakeDao to match
        )
        
        // Act
        countryDao.insertAll(listOf(originalCountry))
        val updateResult = countryDao.updateCountry(updatedCountry)
        val retrievedCountry = countryDao.getAllCountries().first { it.id == 1 }
        
        // Assert
        assertEquals(1, updateResult) // Should return 1 for successful update
        assertEquals("Original Name", retrievedCountry.name?.common)
        assertEquals(2000000, retrievedCountry.population)
    }

    @Test
    fun test_updateCountry_nonExistentCountry() = runTest {
        // Arrange
        val nonExistentCountry = TestDataFactory.createCountry(id = 999, name = "Non-existent")
        
        // Act
        val updateResult = countryDao.updateCountry(nonExistentCountry)
        
        // Assert
        assertEquals(0, updateResult) // Should return 0 for no rows updated
    }

    // Integration test: Complex workflow
    @Test
    fun test_complexWorkflow_insertUpdateDeleteQuery() = runTest {
        // Arrange - Set up countries with continents for proper FakeDao behavior
        val initialCountries = listOf(
            TestDataFactory.createCountry(id = 1, name = "Germany").copy(continents = listOf("Europe")),
            TestDataFactory.createCountry(id = 2, name = "France").copy(continents = listOf("Europe")),
            TestDataFactory.createCountry(id = 3, name = "Japan").copy(continents = listOf("Asia"))
        )
        
        // Act & Assert - Step by step workflow
        
        // 1. Insert countries
        countryDao.insertAll(initialCountries)
        assertEquals(3, countryDao.getAllCountries().size)
        
        // 2. Update a country's capital
        val updateResult = countryDao.updateCapital(listOf("New Capital"), 1)
        assertEquals(1, updateResult)
        
        // 3. Query by continent
        val europeanCountries = countryDao.getCountriesByContinent("Europe")
        assertEquals(2, europeanCountries.size) // Should find Germany and France
        
        // 4. Delete a country - Get the current state to ensure proper object reference
        val currentCountries = countryDao.getAllCountries()
        val countryToDelete = currentCountries.first { it.id == 1 } // Get Germany from current state
        countryDao.delete(countryToDelete)
        assertEquals(2, countryDao.getAllCountries().size)
        
        // 5. Verify final state
        val finalCountries = countryDao.getAllCountries()
        assertEquals(2, finalCountries.size)
        assertFalse(finalCountries.any { it.id == 1 }) // Verify Germany (id=1) was deleted
    }

    // Test edge cases with null values
    @Test
    fun test_insertCountry_withNullValues() = runTest {
        // Arrange
        val countryWithNulls = TestDataFactory.createCountry(
            id = 1,
            name = "Country With Nulls"
        ).copy(
            capital = null,
            population = null,
            area = null,
            region = null
        )
        
        // Act
        countryDao.insertAll(listOf(countryWithNulls))
        val retrievedCountries = countryDao.getAllCountries()
        
        // Assert
        assertEquals(1, retrievedCountries.size)
        val retrievedCountry = retrievedCountries[0]
        assertEquals("Country With Nulls", retrievedCountry.name?.common)
        assertNull(retrievedCountry.capital)
        assertNull(retrievedCountry.population)
        assertNull(retrievedCountry.area)
        assertNull(retrievedCountry.region)
    }
}