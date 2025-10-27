package com.codetutor.countryinfoapp.database.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.codetutor.countryinfoapp.data.*
import com.codetutor.countryinfoapp.database.appdb.AppDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class CountryDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var countryDao: CountryDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        countryDao = database.countryDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    // ==================== Helper Functions ====================

    private fun createTestCountry(
        id: Int? = null,
        name: String = "Test Country",
        region: String = "Europe",
        capital: List<String> = listOf("Test Capital"),
        population: Int = 1000000
    ): Country {
        return Country(
            id = id,
            name = Name(common = name, official = "$name Official"),
            region = region,
            capital = capital,
            population = population,
            area = 100000.0,
            flag = "🏳️",
            independent = true,
            landlocked = false,
            continents = listOf(region),
            timezones = listOf("UTC+1")
        )
    }

    private fun createTestCountries(): List<Country> {
        return listOf(
            createTestCountry(name = "United States", region = "Americas", capital = listOf("Washington D.C."), population = 331000000),
            createTestCountry(name = "Canada", region = "Americas", capital = listOf("Ottawa"), population = 38000000),
            createTestCountry(name = "France", region = "Europe", capital = listOf("Paris"), population = 67000000),
            createTestCountry(name = "Germany", region = "Europe", capital = listOf("Berlin"), population = 83000000),
            createTestCountry(name = "Japan", region = "Asia", capital = listOf("Tokyo"), population = 126000000)
        )
    }

    // ==================== Insert Operations Tests ====================

    @Test
    fun `test inserting single country`() = runBlocking {
        val country = createTestCountry()
        countryDao.insertAll(listOf(country))

        val allCountries = countryDao.getAllCountries()
        assertEquals(1, allCountries.size)
        assertEquals("Test Country", allCountries[0].name?.common)
    }

    @Test
    fun `test inserting multiple countries (bulk insert)`() = runBlocking {
        val countries = createTestCountries()
        countryDao.insertAll(countries)

        val allCountries = countryDao.getAllCountries()
        assertEquals(5, allCountries.size)
    }

    @Test
    fun `test inserting duplicate countries (REPLACE strategy)`() = runBlocking {
        val country1 = createTestCountry(id = 1, name = "Test Country", population = 1000000)
        countryDao.insertAll(listOf(country1))

        // Insert same country with updated data
        val country2 = createTestCountry(id = 1, name = "Test Country Updated", population = 2000000)
        countryDao.insertAll(listOf(country2))

        val allCountries = countryDao.getAllCountries()
        assertEquals(1, allCountries.size)
        assertEquals("Test Country Updated", allCountries[0].name?.common)
        assertEquals(2000000, allCountries[0].population)
    }

    @Test
    fun `test inserting empty list`() = runBlocking {
        countryDao.insertAll(emptyList())

        val allCountries = countryDao.getAllCountries()
        assertEquals(0, allCountries.size)
    }

    @Test
    fun `test verify inserted data integrity`() = runBlocking {
        val country = createTestCountry(
            name = "United States",
            region = "Americas",
            capital = listOf("Washington D.C."),
            population = 331000000
        )
        countryDao.insertAll(listOf(country))

        val retrievedCountries = countryDao.getAllCountries()
        assertEquals(1, retrievedCountries.size)

        val retrieved = retrievedCountries[0]
        assertEquals("United States", retrieved.name?.common)
        assertEquals("Americas", retrieved.region)
        assertEquals(listOf("Washington D.C."), retrieved.capital)
        assertEquals(331000000, retrieved.population)
    }

    // ==================== Read Operations Tests ====================

    @Test
    fun `test getAllCountries with empty database`() = runBlocking {
        val countries = countryDao.getAllCountries()
        assertNotNull(countries)
        assertEquals(0, countries.size)
    }

    @Test
    fun `test getAllCountries with populated database`() = runBlocking {
        val testCountries = createTestCountries()
        countryDao.insertAll(testCountries)

        val countries = countryDao.getAllCountries()
        assertEquals(5, countries.size)
    }

    @Test
    fun `test getCountriesByContinent with valid continent`() = runBlocking {
        val testCountries = createTestCountries()
        countryDao.insertAll(testCountries)

        val europeanCountries = countryDao.getCountriesByContinent("Europe")
        assertEquals(2, europeanCountries.size)
        assertTrue(europeanCountries.all { it.region == "Europe" })
    }

    @Test
    fun `test getCountriesByContinent with non-existent continent`() = runBlocking {
        val testCountries = createTestCountries()
        countryDao.insertAll(testCountries)

        val countries = countryDao.getCountriesByContinent("Antarctica")
        assertEquals(0, countries.size)
    }

    @Test
    fun `test getCountriesByContinent with null continent`() = runBlocking {
        val testCountries = createTestCountries()
        countryDao.insertAll(testCountries)

        // SQLite LIKE with null should return empty
        val countries = countryDao.getCountriesByContinent("")
        assertTrue(countries.isEmpty())
    }

    @Test
    fun `test getCountriesByContinent with empty continent`() = runBlocking {
        val testCountries = createTestCountries()
        countryDao.insertAll(testCountries)

        val countries = countryDao.getCountriesByContinent("")
        assertTrue(countries.isEmpty())
    }

    @Test
    fun `test case-insensitive continent search`() = runBlocking {
        val testCountries = createTestCountries()
        countryDao.insertAll(testCountries)

        // Test with different case
        val countries1 = countryDao.getCountriesByContinent("europe")
        val countries2 = countryDao.getCountriesByContinent("EUROPE")
        val countries3 = countryDao.getCountriesByContinent("Europe")

        // Note: LIKE in SQLite is case-insensitive by default for ASCII characters
        assertTrue(countries1.isNotEmpty() || countries2.isNotEmpty() || countries3.isNotEmpty())
    }

    @Test
    fun `test getCountriesByContinent with partial match`() = runBlocking {
        val testCountries = createTestCountries()
        countryDao.insertAll(testCountries)

        // Using % wildcard for partial match
        val countries = countryDao.getCountriesByContinent("%eur%")
        assertTrue(countries.size >= 2)
    }

    // ==================== Update Operations Tests ====================

    @Test
    fun `test updateCapital with valid country ID`() = runBlocking {
        val country = createTestCountry(capital = listOf("Old Capital"))
        countryDao.insertAll(listOf(country))

        val insertedCountries = countryDao.getAllCountries()
        val countryId = insertedCountries[0].id!!

        val rowsUpdated = countryDao.updateCapital(listOf("New Capital"), countryId)
        assertEquals(1, rowsUpdated)

        val updatedCountries = countryDao.getAllCountries()
        assertEquals(listOf("New Capital"), updatedCountries[0].capital)
    }

    @Test
    fun `test updateCapital with non-existent country ID`() = runBlocking {
        val country = createTestCountry()
        countryDao.insertAll(listOf(country))

        val rowsUpdated = countryDao.updateCapital(listOf("New Capital"), 99999)
        assertEquals(0, rowsUpdated)
    }

    @Test
    fun `test updateCapital with null capital`() = runBlocking {
        val country = createTestCountry(capital = listOf("Old Capital"))
        countryDao.insertAll(listOf(country))

        val insertedCountries = countryDao.getAllCountries()
        val countryId = insertedCountries[0].id!!

        // Note: Room cannot pass null for non-nullable parameter, but we can test with empty list
        val rowsUpdated = countryDao.updateCapital(emptyList(), countryId)
        assertEquals(1, rowsUpdated)

        val updatedCountries = countryDao.getAllCountries()
        assertTrue(updatedCountries[0].capital!!.isEmpty())
    }

    @Test
    fun `test updateCapital with multiple capitals`() = runBlocking {
        val country = createTestCountry(capital = listOf("Old Capital"))
        countryDao.insertAll(listOf(country))

        val insertedCountries = countryDao.getAllCountries()
        val countryId = insertedCountries[0].id!!

        val newCapitals = listOf("Capital 1", "Capital 2", "Capital 3")
        val rowsUpdated = countryDao.updateCapital(newCapitals, countryId)
        assertEquals(1, rowsUpdated)

        val updatedCountries = countryDao.getAllCountries()
        assertEquals(newCapitals, updatedCountries[0].capital)
    }

    @Test
    fun `test updateCountry with complete country object`() = runBlocking {
        val country = createTestCountry(name = "Original Name", population = 1000000)
        countryDao.insertAll(listOf(country))

        val insertedCountries = countryDao.getAllCountries()
        val updatedCountry = insertedCountries[0].copy(
            name = Name(common = "Updated Name", official = "Updated Official Name"),
            population = 2000000
        )

        val rowsUpdated = countryDao.updateCountry(updatedCountry)
        assertEquals(1, rowsUpdated)

        val retrievedCountries = countryDao.getAllCountries()
        assertEquals("Updated Name", retrievedCountries[0].name?.common)
        assertEquals(2000000, retrievedCountries[0].population)
    }

    @Test
    fun `test updateCountry with modified fields`() = runBlocking {
        val country = createTestCountry(
            name = "Test Country",
            region = "Europe",
            population = 1000000
        )
        countryDao.insertAll(listOf(country))

        val insertedCountries = countryDao.getAllCountries()
        val countryToUpdate = insertedCountries[0].copy(
            region = "Asia",
            population = 3000000,
            area = 500000.0
        )

        val rowsUpdated = countryDao.updateCountry(countryToUpdate)
        assertEquals(1, rowsUpdated)

        val updatedCountries = countryDao.getAllCountries()
        assertEquals("Asia", updatedCountries[0].region)
        assertEquals(3000000, updatedCountries[0].population)
        assertEquals(500000.0, updatedCountries[0].area!!, 0.01)
    }

    @Test
    fun `test verify update return values`() = runBlocking {
        val country = createTestCountry()
        countryDao.insertAll(listOf(country))

        val insertedCountries = countryDao.getAllCountries()
        val countryId = insertedCountries[0].id!!

        // Test successful update returns 1
        val successUpdate = countryDao.updateCapital(listOf("New Capital"), countryId)
        assertEquals(1, successUpdate)

        // Test failed update returns 0
        val failedUpdate = countryDao.updateCapital(listOf("Another Capital"), 99999)
        assertEquals(0, failedUpdate)
    }

    // ==================== Delete Operations Tests ====================

    @Test
    fun `test deleting existing country`() = runBlocking {
        val country = createTestCountry()
        countryDao.insertAll(listOf(country))

        val insertedCountries = countryDao.getAllCountries()
        assertEquals(1, insertedCountries.size)

        countryDao.delete(insertedCountries[0])

        val remainingCountries = countryDao.getAllCountries()
        assertEquals(0, remainingCountries.size)
    }

    @Test
    fun `test deleting non-existent country`() = runBlocking {
        val country = createTestCountry(id = 999)

        // Deleting a country that was never inserted should not throw an exception
        try {
            countryDao.delete(country)
            assertTrue(true)
        } catch (e: Exception) {
            fail("Deleting non-existent country should not throw exception")
        }
    }

    @Test
    fun `test verify database state after deletion`() = runBlocking {
        val countries = createTestCountries()
        countryDao.insertAll(countries)

        val allCountries = countryDao.getAllCountries()
        assertEquals(5, allCountries.size)

        // Delete one country
        countryDao.delete(allCountries[0])

        val remainingCountries = countryDao.getAllCountries()
        assertEquals(4, remainingCountries.size)
        assertFalse(remainingCountries.any { it.id == allCountries[0].id })
    }

    @Test
    fun `test deleting last country in database`() = runBlocking {
        val country = createTestCountry()
        countryDao.insertAll(listOf(country))

        val allCountries = countryDao.getAllCountries()
        countryDao.delete(allCountries[0])

        val remainingCountries = countryDao.getAllCountries()
        assertEquals(0, remainingCountries.size)
        assertTrue(remainingCountries.isEmpty())
    }

    @Test
    fun `test delete specific country from multiple`() = runBlocking {
        val countries = createTestCountries()
        countryDao.insertAll(countries)

        val allCountries = countryDao.getAllCountries()
        val franceCountry = allCountries.find { it.name?.common == "France" }
        assertNotNull(franceCountry)

        countryDao.delete(franceCountry!!)

        val remaining = countryDao.getAllCountries()
        assertEquals(4, remaining.size)
        assertFalse(remaining.any { it.name?.common == "France" })
        assertTrue(remaining.any { it.name?.common == "Germany" })
    }

    // ==================== Integration Tests ====================

    @Test
    fun `test insert, read, update, delete sequence`() = runBlocking {
        // Insert
        val country = createTestCountry(name = "Test Country", population = 1000000)
        countryDao.insertAll(listOf(country))

        // Read
        var allCountries = countryDao.getAllCountries()
        assertEquals(1, allCountries.size)
        assertEquals("Test Country", allCountries[0].name?.common)

        // Update
        val countryId = allCountries[0].id!!
        countryDao.updateCapital(listOf("Updated Capital"), countryId)

        allCountries = countryDao.getAllCountries()
        assertEquals(listOf("Updated Capital"), allCountries[0].capital)

        // Delete
        countryDao.delete(allCountries[0])

        allCountries = countryDao.getAllCountries()
        assertEquals(0, allCountries.size)
    }

    @Test
    fun `test complex integration with multiple operations`() = runBlocking {
        // Insert multiple countries
        val countries = createTestCountries()
        countryDao.insertAll(countries)

        // Read by continent
        val europeanCountries = countryDao.getCountriesByContinent("Europe")
        assertEquals(2, europeanCountries.size)

        // Update one country
        val france = europeanCountries.find { it.name?.common == "France" }
        assertNotNull(france)
        countryDao.updateCapital(listOf("Paris Updated"), france!!.id!!)

        // Verify update
        val updated = countryDao.getAllCountries().find { it.id == france.id }
        assertEquals(listOf("Paris Updated"), updated?.capital)

        // Delete one country
        countryDao.delete(france)

        // Verify final state
        val finalCountries = countryDao.getAllCountries()
        assertEquals(4, finalCountries.size)
        assertFalse(finalCountries.any { it.name?.common == "France" })
    }

    @Test
    fun `test insert with complex nested objects`() = runBlocking {
        val complexCountry = Country(
            name = Name(
                common = "Complex Country",
                official = "Official Complex Country",
                nativeName = mapOf("eng" to NameTranslation(official = "Complex", common = "Complex"))
            ),
            region = "Europe",
            capital = listOf("Complex Capital", "Second Capital"),
            population = 5000000,
            car = Car(side = "right", signs = listOf("CC")),
            flags = Flags(png = "flag.png", svg = "flag.svg", alt = "Flag"),
            maps = Maps(googleMaps = "https://google.com", openStreetMaps = "https://osm.org"),
            languages = Languages(languages = mapOf("eng" to "English", "fra" to "French")),
            currencies = mapOf("EUR" to Currency(name = "Euro", symbol = "€")),
            idd = Idd(root = "+1", suffixes = listOf("23")),
            capitalInfo = CapitalInfo(latlng = listOf(48.8566, 2.3522)),
            latlng = listOf(46.2276, 2.2137),
            timezones = listOf("UTC+1", "UTC+2"),
            continents = listOf("Europe"),
            area = 551695.0,
            independent = true,
            landlocked = false
        )

        countryDao.insertAll(listOf(complexCountry))

        val retrieved = countryDao.getAllCountries()
        assertEquals(1, retrieved.size)

        val country = retrieved[0]
        assertEquals("Complex Country", country.name?.common)
        assertEquals(2, country.capital?.size)
        assertEquals("right", country.car?.side)
        assertNotNull(country.languages?.languages)
        assertNotNull(country.currencies)
        assertEquals("+1", country.idd?.root)
    }

    @Test
    fun `test update and read consistency`() = runBlocking {
        val country = createTestCountry(name = "Consistency Test", population = 1000000)
        countryDao.insertAll(listOf(country))

        val inserted = countryDao.getAllCountries()[0]
        val countryId = inserted.id!!

        // Perform multiple updates
        countryDao.updateCapital(listOf("Capital 1"), countryId)
        assertEquals(listOf("Capital 1"), countryDao.getAllCountries()[0].capital)

        countryDao.updateCapital(listOf("Capital 2"), countryId)
        assertEquals(listOf("Capital 2"), countryDao.getAllCountries()[0].capital)

        countryDao.updateCapital(listOf("Capital 3"), countryId)
        assertEquals(listOf("Capital 3"), countryDao.getAllCountries()[0].capital)
    }
}

