package com.codetutor.countryinfoapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.codetutor.countryinfoapp.MainDispatcherRule
import com.codetutor.countryinfoapp.TestDataFactory
import com.codetutor.countryinfoapp.data.Country
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CountryUIViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: CountryUIViewModel
    private lateinit var testCountry: Country

    @Before
    fun setup() {
        viewModel = CountryUIViewModel()
        testCountry = TestDataFactory.createCountry(id = 1, name = "Test Country")
    }

    @Test
    fun `initial state has correct default values`() {
        // Assert initial state
        assertFalse(viewModel.showDeleteAlertDialog.value)
        assertNull(viewModel.selectedCountryForDeletion.value)
        assertNull(viewModel.selectedFilter.value)
        assertEquals("", viewModel.filterByKey.value)
        assertFalse(viewModel.showUpdateCapitalDialog.value)
        assertNull(viewModel.updateCountryInfo.value)
    }

    @Test
    fun `delete dialog state management works correctly`() = runTest {
        // Initially dialog should be hidden
        assertFalse(viewModel.showDeleteAlertDialog.value)
        assertNull(viewModel.selectedCountryForDeletion.value)

        // Act - Show delete dialog
        viewModel.showDeleteAlertDialog.value = true
        viewModel.selectedCountryForDeletion.value = testCountry

        // Assert - Dialog is shown with selected country
        assertTrue(viewModel.showDeleteAlertDialog.value)
        assertEquals(testCountry, viewModel.selectedCountryForDeletion.value)

        // Act - Hide delete dialog
        viewModel.showDeleteAlertDialog.value = false
        viewModel.selectedCountryForDeletion.value = null

        // Assert - Dialog is hidden and country is cleared
        assertFalse(viewModel.showDeleteAlertDialog.value)
        assertNull(viewModel.selectedCountryForDeletion.value)
    }

    @Test
    fun `update capital dialog state management works correctly`() = runTest {
        // Initially dialog should be hidden
        assertFalse(viewModel.showUpdateCapitalDialog.value)
        assertNull(viewModel.updateCountryInfo.value)

        // Act - Show update dialog
        viewModel.showUpdateCapitalDialog.value = true
        viewModel.updateCountryInfo.value = testCountry

        // Assert - Dialog is shown with selected country
        assertTrue(viewModel.showUpdateCapitalDialog.value)
        assertEquals(testCountry, viewModel.updateCountryInfo.value)

        // Act - Hide update dialog
        viewModel.showUpdateCapitalDialog.value = false
        viewModel.updateCountryInfo.value = null

        // Assert - Dialog is hidden and country is cleared
        assertFalse(viewModel.showUpdateCapitalDialog.value)
        assertNull(viewModel.updateCountryInfo.value)
    }

    @Test
    fun `filter state management works correctly`() = runTest {
        // Initially filter should be empty
        assertNull(viewModel.selectedFilter.value)
        assertEquals("", viewModel.filterByKey.value)

        // Act - Set filter values
        viewModel.selectedFilter.value = "continent"
        viewModel.filterByKey.value = "Europe"

        // Assert - Filter values are updated
        assertEquals("continent", viewModel.selectedFilter.value)
        assertEquals("Europe", viewModel.filterByKey.value)

        // Act - Clear filter values
        viewModel.selectedFilter.value = null
        viewModel.filterByKey.value = ""

        // Assert - Filter values are cleared
        assertNull(viewModel.selectedFilter.value)
        assertEquals("", viewModel.filterByKey.value)
    }

    @Test
    fun `selected country for deletion state updates correctly`() = runTest {
        val country1 = TestDataFactory.createCountry(id = 1, name = "Country 1")
        val country2 = TestDataFactory.createCountry(id = 2, name = "Country 2")

        // Act - Select first country for deletion
        viewModel.selectedCountryForDeletion.value = country1

        // Assert
        assertEquals(country1, viewModel.selectedCountryForDeletion.value)
        assertEquals("Country 1", viewModel.selectedCountryForDeletion.value?.name?.common)

        // Act - Select second country for deletion
        viewModel.selectedCountryForDeletion.value = country2

        // Assert
        assertEquals(country2, viewModel.selectedCountryForDeletion.value)
        assertEquals("Country 2", viewModel.selectedCountryForDeletion.value?.name?.common)
    }

    @Test
    fun `update country info state updates correctly`() = runTest {
        val country1 = TestDataFactory.createCountry(id = 1, name = "Country 1", capital = listOf("Capital 1"))
        val country2 = TestDataFactory.createCountry(id = 2, name = "Country 2", capital = listOf("Capital 2"))

        // Act - Select first country for update
        viewModel.updateCountryInfo.value = country1

        // Assert
        assertEquals(country1, viewModel.updateCountryInfo.value)
        assertEquals("Capital 1", viewModel.updateCountryInfo.value?.capital?.first())

        // Act - Select second country for update
        viewModel.updateCountryInfo.value = country2

        // Assert
        assertEquals(country2, viewModel.updateCountryInfo.value)
        assertEquals("Capital 2", viewModel.updateCountryInfo.value?.capital?.first())
    }

    @Test
    fun `multiple filter types can be set`() = runTest {
        // Test continent filter
        viewModel.selectedFilter.value = "continent"
        viewModel.filterByKey.value = "Europe"
        assertEquals("continent", viewModel.selectedFilter.value)
        assertEquals("Europe", viewModel.filterByKey.value)

        // Test language filter
        viewModel.selectedFilter.value = "language"
        viewModel.filterByKey.value = "English"
        assertEquals("language", viewModel.selectedFilter.value)
        assertEquals("English", viewModel.filterByKey.value)

        // Test drive side filter
        viewModel.selectedFilter.value = "driveside"
        viewModel.filterByKey.value = "left"
        assertEquals("driveside", viewModel.selectedFilter.value)
        assertEquals("left", viewModel.filterByKey.value)
    }

    @Test
    fun `dialog states are independent`() = runTest {
        // Act - Show both dialogs simultaneously
        viewModel.showDeleteAlertDialog.value = true
        viewModel.selectedCountryForDeletion.value = testCountry
        viewModel.showUpdateCapitalDialog.value = true
        viewModel.updateCountryInfo.value = testCountry

        // Assert - Both dialogs can be shown independently
        assertTrue(viewModel.showDeleteAlertDialog.value)
        assertTrue(viewModel.showUpdateCapitalDialog.value)
        assertEquals(testCountry, viewModel.selectedCountryForDeletion.value)
        assertEquals(testCountry, viewModel.updateCountryInfo.value)

        // Act - Hide delete dialog but keep update dialog
        viewModel.showDeleteAlertDialog.value = false
        viewModel.selectedCountryForDeletion.value = null

        // Assert - Only update dialog remains
        assertFalse(viewModel.showDeleteAlertDialog.value)
        assertTrue(viewModel.showUpdateCapitalDialog.value)
        assertNull(viewModel.selectedCountryForDeletion.value)
        assertEquals(testCountry, viewModel.updateCountryInfo.value)
    }

    @Test
    fun `filter key can handle empty and whitespace values`() = runTest {
        // Test empty string
        viewModel.filterByKey.value = ""
        assertEquals("", viewModel.filterByKey.value)

        // Test whitespace
        viewModel.filterByKey.value = "   "
        assertEquals("   ", viewModel.filterByKey.value)

        // Test normal value
        viewModel.filterByKey.value = "Europe"
        assertEquals("Europe", viewModel.filterByKey.value)

        // Test back to empty
        viewModel.filterByKey.value = ""
        assertEquals("", viewModel.filterByKey.value)
    }

    @Test
    fun `country selection states handle null values gracefully`() = runTest {
        // Set countries first
        viewModel.selectedCountryForDeletion.value = testCountry
        viewModel.updateCountryInfo.value = testCountry

        // Verify they are set
        assertNotNull(viewModel.selectedCountryForDeletion.value)
        assertNotNull(viewModel.updateCountryInfo.value)

        // Act - Set to null
        viewModel.selectedCountryForDeletion.value = null
        viewModel.updateCountryInfo.value = null

        // Assert - Should handle null gracefully
        assertNull(viewModel.selectedCountryForDeletion.value)
        assertNull(viewModel.updateCountryInfo.value)
    }
}