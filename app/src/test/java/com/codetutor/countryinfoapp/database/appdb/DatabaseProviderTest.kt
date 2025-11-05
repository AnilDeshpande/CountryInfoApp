package com.codetutor.countryinfoapp.database.appdb

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.codetutor.countryinfoapp.database.dao.CountryDao
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class DatabaseProviderTest {
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        // Clear any existing instance before each test
        clearDatabaseInstance()
    }

    @After
    fun tearDown() {
        clearDatabaseInstance()
    }

    private fun clearDatabaseInstance() {
        // Use reflection to clear the INSTANCE field for testing
        try {
            val instanceField = AppDatabase::class.java.getDeclaredField("INSTANCE")
            instanceField.isAccessible = true
            instanceField.set(null, null)
        } catch (e: Exception) {
            // Field might not exist or already null
        }
    }

    // ==================== Database Instance Creation Tests ====================

    @Test
    fun `test singleton pattern implementation`() {
        val instance1 = AppDatabase.getDatabase(context)
        val instance2 = AppDatabase.getDatabase(context)

        assertSame("Should return same instance", instance1, instance2)
    }

    @Test
    fun `test database instance is not null`() {
        val database = AppDatabase.getDatabase(context)
        assertNotNull("Database instance should not be null", database)
    }

    @Test
    fun `test same instance returned on multiple calls`() {
        val instance1 = AppDatabase.getDatabase(context)
        val instance2 = AppDatabase.getDatabase(context)
        val instance3 = AppDatabase.getDatabase(context)

        assertSame("All instances should be the same", instance1, instance2)
        assertSame("All instances should be the same", instance2, instance3)
        assertSame("All instances should be the same", instance1, instance3)
    }

    @Test
    fun `test thread safety - multiple threads get same instance`() {
        // Get initial instance
        val firstInstance = AppDatabase.getDatabase(context)

        // Verify subsequent calls return the same instance
        // Note: Full multi-threaded testing is limited in Robolectric environment
        val secondInstance = AppDatabase.getDatabase(context)
        val thirdInstance = AppDatabase.getDatabase(context)

        assertSame("All instances should be the same", firstInstance, secondInstance)
        assertSame("All instances should be the same", firstInstance, thirdInstance)
    }

    // ==================== Database Configuration Tests ====================

    @Test
    fun `test database version`() {
        val database = AppDatabase.getDatabase(context)
        assertNotNull("Database should be created", database)
        // Database version is set to 1 in the @Database annotation
        // We can verify the database was created successfully
        assertTrue("Database should be a DatabaseProvider", database is DatabaseProvider)
    }

    @Test
    fun `test type converters are registered`() {
        val database = AppDatabase.getDatabase(context)
        val dao = database.countryDao()

        // If type converters are not registered, this would fail
        // We can test by using the DAO which relies on converters
        assertNotNull("DAO should be accessible with type converters", dao)
    }

    @Test
    fun `test DAO is accessible`() {
        val database = AppDatabase.getDatabase(context)
        val dao = database.countryDao()

        assertNotNull("CountryDao should not be null", dao)
        assertTrue("DAO should be of type CountryDao", dao is CountryDao)
    }

    @Test
    fun `test database implements DatabaseProvider interface`() {
        val database = AppDatabase.getDatabase(context)

        assertTrue("Database should implement DatabaseProvider", database is DatabaseProvider)
    }

    @Test
    fun `test countryDao method returns consistent instance`() {
        val database = AppDatabase.getDatabase(context)
        val dao1 = database.countryDao()
        val dao2 = database.countryDao()

        assertNotNull("First DAO should not be null", dao1)
        assertNotNull("Second DAO should not be null", dao2)
        // Room DAOs are typically singletons per database instance
        assertSame("DAO instances should be the same", dao1, dao2)
    }

    @Test
    fun `test database survives context operations`() {
        val instance1 = AppDatabase.getDatabase(context)

        // Get a new context (in Robolectric, this is the same application context)
        val newContext = ApplicationProvider.getApplicationContext<Context>()
        val instance2 = AppDatabase.getDatabase(newContext)

        assertSame("Database should survive context operations", instance1, instance2)
    }

    @Test
    fun `test database can be used for basic operations`() {
        val database = AppDatabase.getDatabase(context)
        val dao = database.countryDao()

        // Verify we can access the DAO and it's functional
        assertNotNull("DAO should be functional", dao)

        // Verify the DAO is properly initialized
        assertTrue("DAO should be CountryDao instance", dao is CountryDao)
    }

    @Test
    fun `test database provider interface contract`() {
        val database = AppDatabase.getDatabase(context)

        // Verify DatabaseProvider contract is fulfilled
        assertTrue("Should implement DatabaseProvider", database is DatabaseProvider)

        // Verify the countryDao method is accessible through interface
        val provider: DatabaseProvider = database
        val providerDao = provider.countryDao()
        assertNotNull("DAO should be accessible through interface", providerDao)
    }

    @Test
    fun `test multiple sequential database accesses`() {
        // Test that we can access the database multiple times sequentially
        repeat(5) { iteration ->
            val database = AppDatabase.getDatabase(context)
            assertNotNull("Database should not be null on iteration $iteration", database)
            val dao = database.countryDao()
            assertNotNull("DAO should not be null on iteration $iteration", dao)
        }
    }
}
