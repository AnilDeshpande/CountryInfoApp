# Unit Test Plan for Country Info App

## 📋 Overview

This document outlines a comprehensive, phase-wise unit testing strategy for the Country Info App. The plan follows a **bottom-up approach** through the MVVM architecture layers, starting from the database layer and progressing upward to ViewModels.

### Testing Strategy
- **Approach**: Bottom-up testing (Database → Repository → ViewModel)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Target**: Achieve high code coverage (aim for 80%+ overall)
- **Exclusions**: Data classes, UI Composables, and Theme files
- **Framework**: JUnit 4, MockK, Coroutines Test, Room Testing

---

## 🎯 Testing Phases

### Phase 1: Database Layer Testing
**Priority**: HIGH | **Estimated Coverage Target**: 85%+

#### 1.1 Type Converters Testing (`Converters.kt`)
**Test File**: `app/src/test/java/com/codetutor/countryinfoapp/database/converters/ConvertersTest.kt`

**Test Cases**:
1. **String List Conversion**
   - ✓ Test conversion of non-empty string list to JSON
   - ✓ Test conversion of empty string list to JSON
   - ✓ Test conversion of null string list to JSON
   - ✓ Test JSON string to string list conversion
   - ✓ Test null JSON to null string list
   - ✓ Test invalid JSON string handling

2. **CapitalInfo Conversion**
   - ✓ Test CapitalInfo object to JSON conversion
   - ✓ Test null CapitalInfo to JSON
   - ✓ Test JSON to CapitalInfo object conversion
   - ✓ Test null JSON to null CapitalInfo
   - ✓ Test invalid JSON to CapitalInfo handling

3. **Car Object Conversion**
   - ✓ Test Car object to JSON conversion
   - ✓ Test null Car to JSON
   - ✓ Test JSON to Car object conversion
   - ✓ Test malformed JSON handling

4. **Currency Conversion**
   - ✓ Test Currency map to JSON conversion
   - ✓ Test null Currency to JSON
   - ✓ Test JSON to Currency map conversion
   - ✓ Test empty currency map handling

5. **Flags, Maps, Name, Languages Conversions**
   - ✓ Test each complex type to JSON conversion
   - ✓ Test null handling for each type
   - ✓ Test JSON to object conversion for each type
   - ✓ Test edge cases (empty objects, missing fields)

6. **Double List Conversion**
   - ✓ Test double list to JSON conversion
   - ✓ Test empty double list
   - ✓ Test null double list
   - ✓ Test JSON to double list conversion

7. **IDD Conversion**
   - ✓ Test IDD object to JSON
   - ✓ Test null IDD handling
   - ✓ Test JSON to IDD object

**Dependencies**: kotlinx.serialization, JUnit 4

**Success Criteria**: All type converters properly serialize/deserialize objects with proper null handling

---

#### 1.2 DAO Testing (`CountryDao.kt`)
**Test File**: `app/src/test/java/com/codetutor/countryinfoapp/database/dao/CountryDaoTest.kt`

**Test Cases**:
1. **Insert Operations**
   - ✓ Test inserting single country
   - ✓ Test inserting multiple countries (bulk insert)
   - ✓ Test inserting duplicate countries (REPLACE strategy)
   - ✓ Test inserting empty list
   - ✓ Verify inserted data integrity

2. **Read Operations**
   - ✓ Test `getAllCountries()` with empty database
   - ✓ Test `getAllCountries()` with populated database
   - ✓ Test `getCountriesByContinent()` with valid continent
   - ✓ Test `getCountriesByContinent()` with non-existent continent
   - ✓ Test `getCountriesByContinent()` with null/empty continent
   - ✓ Test case-insensitive continent search

3. **Update Operations**
   - ✓ Test `updateCapital()` with valid country ID
   - ✓ Test `updateCapital()` with non-existent country ID
   - ✓ Test `updateCapital()` with null capital
   - ✓ Test `updateCapital()` with multiple capitals
   - ✓ Test `updateCountry()` with complete country object
   - ✓ Test `updateCountry()` with modified fields
   - ✓ Verify update return values

4. **Delete Operations**
   - ✓ Test deleting existing country
   - ✓ Test deleting non-existent country
   - ✓ Verify database state after deletion
   - ✓ Test deleting last country in database

5. **Integration Tests**
   - ✓ Test insert, read, update, delete sequence
   - ✓ Test concurrent operations (if applicable)
   - ✓ Test transaction rollback scenarios

**Dependencies**: Room Testing Library, JUnit 4, Coroutines Test, MockK

**Setup Requirements**:
- Use in-memory database for testing
- Create test database instance before each test
- Close database after each test
- Create helper functions for country test data

**Success Criteria**: All DAO operations work correctly with proper data persistence and retrieval

---

#### 1.3 Database Provider Testing (`DatabaseProvider.kt`)
**Test File**: `app/src/test/java/com/codetutor/countryinfoapp/database/appdb/DatabaseProviderTest.kt`

**Test Cases**:
1. **Database Instance Creation**
   - ✓ Test singleton pattern implementation
   - ✓ Test database instance is not null
   - ✓ Test same instance returned on multiple calls
   - ✓ Test thread safety (if applicable)

2. **Database Configuration**
   - ✓ Test database version
   - ✓ Test type converters are registered
   - ✓ Test DAO is accessible

**Dependencies**: Robolectric (for Context), JUnit 4

**Success Criteria**: Database provider correctly creates and manages database instances

---

### Phase 2: Repository Layer Testing
**Priority**: HIGH | **Estimated Coverage Target**: 85%+

#### 2.1 Filter Strategy Testing

##### 2.1.1 `FilterByContinent.kt`
**Test File**: `app/src/test/java/com/codetutor/countryinfoapp/repository/FilterByContinentTest.kt`

**Test Cases**:
1. ✓ Test filtering with valid continent name
2. ✓ Test filtering with null continent
3. ✓ Test filtering with empty continent string
4. ✓ Test case-insensitive continent matching
5. ✓ Test filtering with non-existent continent
6. ✓ Test filtering empty country list
7. ✓ Test filtering returns all countries when continent is null
8. ✓ Test multiple countries from same continent
9. ✓ Test countries from different continents

**Dependencies**: JUnit 4, Coroutines Test, MockK

---

##### 2.1.2 `FilterByDriveSide.kt`
**Test File**: `app/src/test/java/com/codetutor/countryinfoapp/repository/FilterByDriveSideTest.kt` *(Already exists)*

**Status**: Review and enhance existing tests

**Additional Test Cases to Add**:
1. ✓ Test filtering with "left" drive side
2. ✓ Test filtering with "right" drive side
3. ✓ Test filtering with null drive side
4. ✓ Test filtering with empty drive side string
5. ✓ Test case-insensitive drive side matching
6. ✓ Test filtering with invalid drive side value
7. ✓ Test filtering empty country list
8. ✓ Test countries with null car information

**Action**: Review existing test and add missing cases

---

##### 2.1.3 `FilterByLanguage.kt`
**Test File**: `app/src/test/java/com/codetutor/countryinfoapp/repository/FilterByLanguageTest.kt`

**Test Cases**:
1. ✓ Test filtering with valid language name
2. ✓ Test filtering with null language
3. ✓ Test filtering with empty language string
4. ✓ Test case-sensitive language matching
5. ✓ Test filtering with non-existent language
6. ✓ Test filtering empty country list
7. ✓ Test countries with multiple languages
8. ✓ Test countries with null languages object
9. ✓ Test countries with empty languages map
10. ✓ Test filtering returns all when language is null

**Dependencies**: JUnit 4, Coroutines Test, MockK

---

#### 2.2 Service Layer Testing

##### 2.2.1 `CountryListServiceProviderImpl.kt`
**Test File**: `app/src/test/java/com/codetutor/countryinfoapp/repository/service/CountryListServiceProviderImplTest.kt`

**Test Cases**:
1. **Data Loading**
   - ✓ Test loading country list from raw resource
   - ✓ Test JSON parsing succeeds
   - ✓ Test delay is applied (2 seconds)
   - ✓ Test returned list is not empty
   - ✓ Test country objects are properly deserialized

2. **Error Handling**
   - ✓ Test handling of missing raw resource
   - ✓ Test handling of malformed JSON
   - ✓ Test handling of empty JSON array

3. **Data Integrity**
   - ✓ Test all required fields are populated
   - ✓ Test unknown fields are ignored (ignoreUnknownKeys)

**Dependencies**: Robolectric, JUnit 4, Coroutines Test, MockK

**Mocking Strategy**: Mock Context and Resources

---

##### 2.2.2 `CountryListProviderViaNetwork.kt`
**Test File**: `app/src/test/java/com/codetutor/countryinfoapp/repository/service/CountryListProviderViaNetworkTest.kt`

**Test Cases**:
1. **Successful Network Call**
   - ✓ Test API call returns country list
   - ✓ Test data deserialization
   - ✓ Test network response handling

2. **Error Scenarios**
   - ✓ Test network timeout handling
   - ✓ Test HTTP error responses (4xx, 5xx)
   - ✓ Test JSON parsing errors
   - ✓ Test empty response handling
   - ✓ Test network unavailability

3. **Integration**
   - ✓ Test with MockWebServer
   - ✓ Test retry logic (if implemented)

**Dependencies**: MockWebServer, JUnit 4, Coroutines Test, Retrofit

**Setup**: Use MockWebServer to simulate API responses

---

#### 2.3 Repository Testing (`CountryRepository.kt`)
**Test File**: `app/src/test/java/com/codetutor/countryinfoapp/repository/CountryRepositoryTest.kt`

**Test Cases**:

1. **Fetch and Insert Operations**
   - ✓ Test `fetchAndInsertAll()` when database is empty
   - ✓ Test `fetchAndInsertAll()` when database already has data (should not fetch again)
   - ✓ Test successful data insertion from service
   - ✓ Test handling service failures
   - ✓ Test coroutine dispatcher usage

2. **Get All Countries**
   - ✓ Test `getAllCountries()` returns cached data on subsequent calls
   - ✓ Test `getAllCountries()` fetches from DAO on first call
   - ✓ Test empty country list handling
   - ✓ Test cache invalidation after delete/update

3. **Delete Country**
   - ✓ Test `deleteCountry()` removes country from database
   - ✓ Test cache is refreshed after deletion
   - ✓ Test DAO delete is called with correct country
   - ✓ Test deleting non-existent country

4. **Update Capital**
   - ✓ Test `updateCapital()` updates country in database
   - ✓ Test cache is refreshed after update
   - ✓ Test country object is correctly copied with new capital
   - ✓ Test DAO update is called with correct data
   - ✓ Test updating null country

5. **Filter Countries**
   - ✓ Test `filterCountries()` with FilterByContinent
   - ✓ Test `filterCountries()` with FilterByDriveSide
   - ✓ Test `filterCountries()` with FilterByLanguage
   - ✓ Test filtering on cached data
   - ✓ Test filtering empty list
   - ✓ Test multiple filter criteria (if chaining supported)

6. **Coroutine and Dispatcher Testing**
   - ✓ Test all suspend functions use correct dispatcher
   - ✓ Test concurrent operation handling
   - ✓ Test cancellation scenarios

**Dependencies**: JUnit 4, MockK, Coroutines Test

**Mocking Strategy**:
- Mock ICountryDao
- Mock CountryListServiceProvider
- Use TestCoroutineDispatcher for dispatcher

**Success Criteria**: Repository correctly coordinates between DAO and service, manages cache, and handles all CRUD operations

---

### Phase 3: Utility Classes Testing
**Priority**: MEDIUM | **Estimated Coverage Target**: 90%+

#### 3.1 Filter Factory Testing

##### 3.1.1 `FilterCriteriaFactoryProvider.kt`
**Test File**: `app/src/test/java/com/codetutor/countryinfoapp/util/FilterCriteriaFactoryProviderTest.kt`

**Test Cases**:
1. ✓ Test `getFactory("Continent")` returns ContinentFilterFactory
2. ✓ Test `getFactory("Drive Side")` returns DriveSideFilterFactory
3. ✓ Test `getFactory()` with non-existent key returns null
4. ✓ Test `getFactory()` with null key returns null
5. ✓ Test `getFactory()` with empty string returns null
6. ✓ Test case-sensitive key matching
7. ✓ Test factory map contains correct mappings

**Dependencies**: JUnit 4

---

##### 3.1.2 `ContinentFilterFactory.kt`
**Test File**: `app/src/test/java/com/codetutor/countryinfoapp/util/ContinentFilterFactoryTest.kt`

**Test Cases**:
1. ✓ Test `create()` returns FilterByContinent instance
2. ✓ Test created filter with valid continent value
3. ✓ Test created filter with null continent value
4. ✓ Test created filter with empty continent value
5. ✓ Test filter options list is correct
6. ✓ Test filter options contains expected continents

**Dependencies**: JUnit 4, Coroutines Test

---

##### 3.1.3 `DriveSideFilterFactory.kt`
**Test File**: `app/src/test/java/com/codetutor/countryinfoapp/util/DriveSideFilterFactoryTest.kt`

**Test Cases**:
1. ✓ Test `create()` returns FilterByDriveSide instance
2. ✓ Test created filter with "left" value
3. ✓ Test created filter with "right" value
4. ✓ Test created filter with null value
5. ✓ Test created filter with empty value
6. ✓ Test filter options list contains "left" and "right"

**Dependencies**: JUnit 4, Coroutines Test

---

#### 3.2 Helper Utilities Testing

##### 3.2.1 `Helper.kt` (util package)
**Test File**: `app/src/test/java/com/codetutor/countryinfoapp/util/HelperTest.kt`

**Test Cases**:
- Review the Helper.kt file and create appropriate test cases based on utility functions present
- Test all public utility methods
- Test edge cases and null handling

**Dependencies**: JUnit 4

---

### Phase 4: ViewModel Layer Testing
**Priority**: HIGH | **Estimated Coverage Target**: 85%+

#### 4.1 `CountryOperationViewModel.kt`
**Test File**: `app/src/test/java/com/codetutor/countryinfoapp/viewmodel/CountryOperationViewModelTest.kt`

**Test Cases**:

1. **Initialization**
   - ✓ Test ViewModel initialization triggers `fetchAndInsertAll()`
   - ✓ Test `allCountries` state is initialized as empty list
   - ✓ Test data is loaded after initialization completes

2. **Get All Countries**
   - ✓ Test `getAllCountries()` fetches data from repository
   - ✓ Test `allCountries` state is updated with fetched data
   - ✓ Test empty list handling
   - ✓ Test error handling during fetch

3. **Delete Country**
   - ✓ Test `deleteCountry()` calls repository delete
   - ✓ Test `allCountries` state is refreshed after delete
   - ✓ Test deleting single country
   - ✓ Test UI state updates after deletion
   - ✓ Test error handling during deletion

4. **Update Capital**
   - ✓ Test `updateCapital()` calls repository update
   - ✓ Test `allCountries` state is refreshed after update
   - ✓ Test capital update with valid data
   - ✓ Test UI state updates after update
   - ✓ Test error handling during update

5. **Filter Operations**
   - ✓ Test applying continent filter
   - ✓ Test applying drive side filter
   - ✓ Test applying language filter
   - ✓ Test clearing filters
   - ✓ Test filter state management
   - ✓ Test filtered results update UI state

6. **State Management**
   - ✓ Test loading state during operations
   - ✓ Test error state handling
   - ✓ Test success state after operations
   - ✓ Test multiple concurrent operations

7. **Coroutine and Lifecycle**
   - ✓ Test operations use viewModelScope
   - ✓ Test cancellation when ViewModel is cleared
   - ✓ Test suspend function execution

**Dependencies**: JUnit 4, MockK, Coroutines Test, Turbine (for Flow testing)

**Mocking Strategy**:
- Mock ICountryRepository
- Use TestCoroutineDispatcher
- Use InstantTaskExecutorRule for LiveData/State

**Setup Requirements**:
```kotlin
@get:Rule
val instantTaskExecutorRule = InstantTaskExecutorRule()

@get:Rule
val testCoroutineRule = TestCoroutineRule()
```

---

#### 4.2 `CountryUIViewModel.kt`
**Test File**: `app/src/test/java/com/codetutor/countryinfoapp/viewmodel/CountryUIViewModelTest.kt`

**Test Cases**:

1. **UI State Management**
   - ✓ Test initial UI state
   - ✓ Test loading state updates
   - ✓ Test error state updates
   - ✓ Test success state updates

2. **Filter UI State**
   - ✓ Test filter chip selection state
   - ✓ Test filter option state updates
   - ✓ Test selected filter key state
   - ✓ Test clearing filter selection

3. **Dialog State**
   - ✓ Test delete dialog visibility state
   - ✓ Test update dialog visibility state
   - ✓ Test selected country for dialog

4. **State Observers**
   - ✓ Test state changes emit correct values
   - ✓ Test multiple observers work correctly
   - ✓ Test state persistence across configuration changes (if applicable)

**Dependencies**: JUnit 4, MockK, Coroutines Test, Turbine

---

#### 4.3 `CountryViewModelFactory.kt`
**Test File**: `app/src/test/java/com/codetutor/countryinfoapp/viewmodel/CountryViewModelFactoryTest.kt`

**Test Cases**:
1. ✓ Test factory creates CountryOperationViewModel instance
2. ✓ Test factory passes repository to ViewModel correctly
3. ✓ Test factory with null repository throws exception
4. ✓ Test factory creates new instance each time
5. ✓ Test factory with different ViewModel classes (if supported)
6. ✓ Test factory throws exception for unsupported ViewModel types

**Dependencies**: JUnit 4, MockK

---

## 📊 Coverage Targets by Phase

| Phase | Component | Target Coverage | Priority |
|-------|-----------|----------------|----------|
| 1.1 | Converters | 90% | HIGH |
| 1.2 | DAO | 85% | HIGH |
| 1.3 | Database Provider | 80% | MEDIUM |
| 2.1 | Filter Strategies | 95% | HIGH |
| 2.2 | Service Layer | 85% | HIGH |
| 2.3 | Repository | 90% | HIGH |
| 3.1 | Filter Factories | 95% | MEDIUM |
| 3.2 | Helper Utilities | 80% | MEDIUM |
| 4.1 | CountryOperationViewModel | 85% | HIGH |
| 4.2 | CountryUIViewModel | 85% | HIGH |
| 4.3 | ViewModelFactory | 90% | MEDIUM |

**Overall Target**: 85%+ code coverage

---

## 🛠️ Testing Tools and Dependencies

### Required Dependencies (build.gradle)
```gradle
dependencies {
    // Core Testing
    testImplementation 'junit:junit:4.13.2'
    testImplementation 'org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3'
    
    // Mocking
    testImplementation 'io.mockk:mockk:1.13.8'
    testImplementation 'io.mockk:mockk-android:1.13.8'
    
    // Room Testing
    testImplementation 'androidx.room:room-testing:2.6.1'
    
    // Architecture Components Testing
    testImplementation 'androidx.arch.core:core-testing:2.2.0'
    
    // Network Testing
    testImplementation 'com.squareup.okhttp3:mockwebserver:4.12.0'
    
    // Robolectric (for Android Context)
    testImplementation 'org.robolectric:robolectric:4.11'
    
    // Flow Testing
    testImplementation 'app.cash.turbine:turbine:1.0.0'
    
    // Truth Assertions (optional but recommended)
    testImplementation 'com.google.truth:truth:1.1.5'
}
```

---

## 📝 Test Implementation Guidelines

### 1. Test File Naming Convention
- Test files should mirror source files: `ClassName.kt` → `ClassNameTest.kt`
- Place in same package structure under `test/java/`

### 2. Test Method Naming Convention
Use descriptive names following pattern:
```kotlin
@Test
fun `methodName_stateUnderTest_expectedBehavior`() {
    // Test implementation
}
```

Example:
```kotlin
@Test
fun `filterCountries_withValidContinent_returnsFilteredList`() {
    // Arrange, Act, Assert
}
```

### 3. Test Structure (AAA Pattern)
```kotlin
@Test
fun testName() {
    // Arrange: Setup test data and mocks
    val testData = createTestData()
    every { mock.method() } returns expectedValue
    
    // Act: Execute the method under test
    val result = systemUnderTest.method()
    
    // Assert: Verify the results
    assertEquals(expectedValue, result)
    verify { mock.method() }
}
```

### 4. Coroutine Testing Setup
```kotlin
class RepositoryTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val testDispatcher = testCoroutineRule.testDispatcher
    
    @Test
    fun testSuspendFunction() = testCoroutineRule.runBlockingTest {
        // Test implementation
    }
}
```

### 5. Room Database Testing Setup
```kotlin
@Before
fun setup() {
    database = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        AppDataBase::class.java
    ).allowMainThreadQueries()
     .build()
    
    dao = database.countryDao()
}

@After
fun tearDown() {
    database.close()
}
```

### 6. MockK Setup Examples
```kotlin
// Relaxed mock (returns default values)
private val repository: ICountryRepository = mockk(relaxed = true)

// Strict mock (throws exception if not mocked)
private val dao: ICountryDao = mockk()

// Mocking suspend functions
coEvery { repository.getAllCountries() } returns listOf(country1, country2)

// Verifying suspend function calls
coVerify { repository.deleteCountry(any()) }
```

---

## 🔄 Phase Execution Workflow

### For Each Phase:

1. **Setup Phase**
   - Create test file structure
   - Add necessary test dependencies
   - Setup test utilities and helpers

2. **Implementation Phase**
   - Write tests following AAA pattern
   - Implement one test class at a time
   - Follow TDD principles where applicable

3. **Verification Phase**
   - Run tests: `./gradlew test`
   - Check code coverage: `./gradlew jacocoTestReport`
   - Review coverage report in `app/build/reports/jacoco/`

4. **Refinement Phase**
   - Add missing test cases to reach coverage target
   - Refactor tests for better maintainability
   - Document complex test scenarios

5. **Validation Phase**
   - Ensure all tests pass
   - Verify coverage meets phase target
   - Review test quality and completeness
   - Get code review if working in team

6. **Documentation Phase**
   - Update this plan with actual results
   - Document any deviations or issues
   - Note lessons learned

---

## 📈 Progress Tracking

### Phase 1: Database Layer
- [ ] 1.1 Converters Testing - Coverage: __%
- [ ] 1.2 DAO Testing - Coverage: __%
- [ ] 1.3 Database Provider Testing - Coverage: __%
- [ ] **Phase 1 Complete** - Overall Coverage: __%

### Phase 2: Repository Layer
- [x] 2.1.1 FilterByContinent Testing - Coverage: __%
- [x] 2.1.2 FilterByDriveSide Testing - Coverage: __(existing, enhance)
- [ ] 2.1.3 FilterByLanguage Testing - Coverage: __%
- [ ] 2.2.1 CountryListServiceProviderImpl Testing - Coverage: __%
- [ ] 2.2.2 CountryListProviderViaNetwork Testing - Coverage: __%
- [ ] 2.3 CountryRepository Testing - Coverage: __%
- [ ] **Phase 2 Complete** - Overall Coverage: __%

### Phase 3: Utility Classes
- [ ] 3.1.1 FilterCriteriaFactoryProvider Testing - Coverage: __%
- [ ] 3.1.2 ContinentFilterFactory Testing - Coverage: __%
- [ ] 3.1.3 DriveSideFilterFactory Testing - Coverage: __%
- [ ] 3.2.1 Helper Testing - Coverage: __%
- [ ] **Phase 3 Complete** - Overall Coverage: __%

### Phase 4: ViewModel Layer
- [ ] 4.1 CountryOperationViewModel Testing - Coverage: __%
- [ ] 4.2 CountryUIViewModel Testing - Coverage: __%
- [ ] 4.3 CountryViewModelFactory Testing - Coverage: __%
- [ ] **Phase 4 Complete** - Overall Coverage: __%

---

## 🎯 Success Criteria

### Per Phase
- ✅ All planned tests implemented
- ✅ All tests passing
- ✅ Coverage target met
- ✅ No critical bugs found
- ✅ Code review completed

### Overall Project
- ✅ Overall code coverage ≥ 85%
- ✅ All critical paths tested
- ✅ All edge cases covered
- ✅ CI/CD integration (if applicable)
- ✅ Documentation updated

---

## 🚀 Commands for Execution

### Run All Tests
```bash
./gradlew test
```

### Run Tests for Specific Module
```bash
./gradlew :app:testDebugUnitTest
```

### Generate Coverage Report
```bash
./gradlew jacocoTestReport
```

### Run Tests with Coverage
```bash
./gradlew testDebugUnitTest jacocoTestReport
```

### View Coverage Report
```bash
open app/build/reports/jacoco/testDebugUnitTest/html/index.html
```

### Run Specific Test Class
```bash
./gradlew test --tests "com.codetutor.countryinfoapp.repository.CountryRepositoryTest"
```

### Run Tests with Debug Output
```bash
./gradlew test --info
```

---

## 📚 Additional Resources

### Testing Best Practices
1. Test one thing per test method
2. Use descriptive test names
3. Follow AAA pattern (Arrange, Act, Assert)
4. Keep tests independent and isolated
5. Mock external dependencies
6. Use test data builders for complex objects
7. Test edge cases and error scenarios
8. Avoid testing implementation details
9. Prefer composition over inheritance in tests
10. Keep tests maintainable and readable

### Common Pitfalls to Avoid
- ❌ Testing private methods directly
- ❌ Over-mocking (mock only external dependencies)
- ❌ Brittle tests that break with minor refactoring
- ❌ Tests that depend on execution order
- ❌ Tests that access real databases or networks
- ❌ Ignoring null and empty cases
- ❌ Not testing error conditions
- ❌ Copy-paste test code without understanding

### Useful MockK Patterns
```kotlin
// Return different values on consecutive calls
every { mock.method() } returnsMany listOf(value1, value2, value3)

// Throw exception
every { mock.method() } throws RuntimeException("Error")

// Capture arguments
val slot = slot<Country>()
every { mock.method(capture(slot)) } returns Unit
// Access captured value: slot.captured

// Answer with lambda
every { mock.method(any()) } answers { firstArg<Country>().copy() }
```

---

## 📋 Notes and Observations

### Phase-Specific Notes
- Add notes here as you progress through phases
- Document any blockers or issues
- Note any deviations from the plan

### Lessons Learned
- Document insights gained during testing
- Note patterns that worked well
- Identify areas for improvement

---

## 🔄 Plan Updates

| Date | Phase | Update | Reason |
|------|-------|--------|--------|
| - | - | - | - |

---

**Last Updated**: 2025-10-26  
**Version**: 1.0  
**Status**: Ready for Implementation  
**Next Phase**: Phase 1.1 - Converters Testing

