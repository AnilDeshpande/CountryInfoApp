package com.codetutor.countryinfoapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.codetutor.countryinfoapp.MainDispatcherRule
import com.codetutor.countryinfoapp.TestDataFactory
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.repository.FilterByContinent
import com.codetutor.countryinfoapp.repository.FilterCriteria
import com.codetutor.countryinfoapp.repository.ICountryRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CountryOperationViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: CountryOperationViewModel
    private lateinit var mockRepository: ICountryRepository
    private lateinit var testCountries: List<Country>

    @Before
    fun setup() {
        mockRepository = mockk()
        testCountries = TestDataFactory.createCountryList(3)
    }

    @Test
    fun `init triggers repository fetch once`() = runTest {
        // Arrange
        coEvery { mockRepository.fetchAndInsertAll() } just Runs
        coEvery { mockRepository.getAllCountries() } returns testCountries

        // Act
        viewModel = CountryOperationViewModel(mockRepository)

        // Assert - Verify repository fetch was called once during init
        coVerify(exactly = 1) { mockRepository.fetchAndInsertAll() }
        coVerify(exactly = 1) { mockRepository.getAllCountries() }
        assertEquals(testCountries, viewModel.allCountries.value)
    }

    @Test
    fun `deleteCountry reduces allCountries size`() = runTest {
        // Arrange
        val countryToDelete = testCountries[0]
        val remainingCountries = testCountries.drop(1)
        
        coEvery { mockRepository.fetchAndInsertAll() } just Runs
        coEvery { mockRepository.getAllCountries() } returnsMany listOf(testCountries, remainingCountries)
        coEvery { mockRepository.deleteCountry(countryToDelete) } just Runs
        
        viewModel = CountryOperationViewModel(mockRepository)
        
        // Verify initial state
        assertEquals(3, viewModel.allCountries.value.size)

        // Act
        viewModel.deleteCountry(countryToDelete)

        // Assert
        coVerify(exactly = 1) { mockRepository.deleteCountry(countryToDelete) }
        coVerify(exactly = 2) { mockRepository.getAllCountries() } // Once in init, once after delete
        assertEquals(2, viewModel.allCountries.value.size)
        assertEquals(remainingCountries, viewModel.allCountries.value)
    }

    @Test
    fun `updateCountryCapital mutates capital data`() = runTest {
        // Arrange
        val countryToUpdate = testCountries[0]
        val newCapital = "New Capital City"
        val updatedCountry = countryToUpdate.copy(capital = listOf(newCapital))
        val updatedCountries = listOf(updatedCountry) + testCountries.drop(1)
        
        coEvery { mockRepository.fetchAndInsertAll() } just Runs
        coEvery { mockRepository.getAllCountries() } returnsMany listOf(testCountries, updatedCountries)
        coEvery { mockRepository.updateCapital(countryToUpdate, newCapital) } just Runs
        
        viewModel = CountryOperationViewModel(mockRepository)

        // Act
        viewModel.updateCapital(countryToUpdate, newCapital)

        // Assert
        coVerify(exactly = 1) { mockRepository.updateCapital(countryToUpdate, newCapital) }
        coVerify(exactly = 2) { mockRepository.getAllCountries() } // Once in init, once after update
        assertEquals(updatedCountries, viewModel.allCountries.value)
        assertEquals(newCapital, viewModel.allCountries.value[0].capital?.first())
    }

    @Test
    fun `applyFilter delegates to repo and updates state`() = runTest {
        // Arrange
        val filterCriteria = FilterByContinent("Europe")
        val europeanCountries = TestDataFactory.createEuropeanCountries()
        
        coEvery { mockRepository.fetchAndInsertAll() } just Runs
        coEvery { mockRepository.getAllCountries() } returns testCountries
        coEvery { mockRepository.filterCountries(filterCriteria) } returns europeanCountries
        
        viewModel = CountryOperationViewModel(mockRepository)

        // Act
        viewModel.filterCountries(filterCriteria)

        // Assert
        coVerify(exactly = 1) { mockRepository.filterCountries(filterCriteria) }
        assertEquals(europeanCountries, viewModel.allCountries.value)
    }

    @Test
    fun `loadCountries updates loading state correctly`() = runTest {
        // Arrange
        val updatedCountries = TestDataFactory.createAsianCountries()
        
        coEvery { mockRepository.fetchAndInsertAll() } just Runs
        coEvery { mockRepository.getAllCountries() } returnsMany listOf(testCountries, updatedCountries)
        
        viewModel = CountryOperationViewModel(mockRepository)
        
        // Verify initial state
        assertEquals(testCountries, viewModel.allCountries.value)

        // Act
        viewModel.getAllCountries()

        // Assert
        coVerify(exactly = 2) { mockRepository.getAllCountries() } // Once in init, once manually
        assertEquals(updatedCountries, viewModel.allCountries.value)
    }

    @Test
    fun `filterCountries applies valid criteria correctly`() = runTest {
        // Arrange
        val filterCriteria = FilterByContinent("Europe")
        val europeanCountries = TestDataFactory.createEuropeanCountries()
        
        coEvery { mockRepository.fetchAndInsertAll() } just Runs
        coEvery { mockRepository.getAllCountries() } returns testCountries
        coEvery { mockRepository.filterCountries(filterCriteria) } returns europeanCountries
        
        viewModel = CountryOperationViewModel(mockRepository)

        // Act
        viewModel.filterCountries(filterCriteria)

        // Assert
        coVerify(exactly = 1) { mockRepository.filterCountries(filterCriteria) }
        assertEquals(europeanCountries, viewModel.allCountries.value)
    }

    @Test
    fun `viewModel handles empty repository gracefully`() = runTest {
        // Arrange
        coEvery { mockRepository.fetchAndInsertAll() } just Runs
        coEvery { mockRepository.getAllCountries() } returns emptyList()

        // Act
        viewModel = CountryOperationViewModel(mockRepository)

        // Assert - Should handle empty data gracefully
        coVerify(exactly = 1) { mockRepository.fetchAndInsertAll() }
        coVerify(exactly = 1) { mockRepository.getAllCountries() }
        assertEquals(emptyList<Country>(), viewModel.allCountries.value)
    }

    @Test
    fun `multiple filter operations work correctly`() = runTest {
        // Arrange
        val continentFilter = FilterByContinent("Europe")
        val asiaFilter = FilterByContinent("Asia")
        val europeanCountries = TestDataFactory.createEuropeanCountries()
        val asianCountries = TestDataFactory.createAsianCountries()
        
        coEvery { mockRepository.fetchAndInsertAll() } just Runs
        coEvery { mockRepository.getAllCountries() } returns testCountries
        coEvery { mockRepository.filterCountries(continentFilter) } returns europeanCountries
        coEvery { mockRepository.filterCountries(asiaFilter) } returns asianCountries
        
        viewModel = CountryOperationViewModel(mockRepository)

        // Act - Apply first filter
        viewModel.filterCountries(continentFilter)
        assertEquals(europeanCountries, viewModel.allCountries.value)

        // Act - Apply second filter
        viewModel.filterCountries(asiaFilter)

        // Assert
        coVerify(exactly = 1) { mockRepository.filterCountries(continentFilter) }
        coVerify(exactly = 1) { mockRepository.filterCountries(asiaFilter) }
        assertEquals(asianCountries, viewModel.allCountries.value)
    }
}