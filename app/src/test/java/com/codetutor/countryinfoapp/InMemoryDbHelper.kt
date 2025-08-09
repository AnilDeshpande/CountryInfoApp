package com.codetutor.countryinfoapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.codetutor.countryinfoapp.database.appdb.AppDatabase
import com.codetutor.countryinfoapp.database.dao.CountryDao

/**
 * Helper class for creating in-memory Room database instances for testing
 */
object InMemoryDbHelper {

    /**
     * Creates an in-memory Room database for testing
     * The database is created fresh for each test and destroyed when the test completes
     */
    fun createInMemoryDatabase(): AppDatabase {
        return Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
        .allowMainThreadQueries() // Allow queries on main thread for testing
        .build()
    }

    /**
     * Creates an in-memory database and returns the CountryDao
     * Convenience method for tests that only need the DAO
     */
    fun createInMemoryCountryDao(): CountryDao {
        return createInMemoryDatabase().countryDao()
    }

    /**
     * Creates a database with test data pre-populated
     */
    suspend fun createDatabaseWithTestData(): Pair<AppDatabase, CountryDao> {
        val database = createInMemoryDatabase()
        val dao = database.countryDao()
        
        // Insert some test data
        val testCountries = TestDataFactory.createCountryList(5)
        dao.insertAll(testCountries)
        
        return Pair(database, dao)
    }

    /**
     * Closes the database and cleans up resources
     */
    fun closeDatabase(database: AppDatabase) {
        database.close()
    }
}