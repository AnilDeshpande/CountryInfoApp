# CountryInfoApp – **Unit‑Test Implementation Roadmap**

> **Agentic‑AI workflow instructions**
>
> 1. Work **phase‑by‑phase** in the order below.
> 2. Inside each phase, tackle tasks **top→bottom**.
> 3. When a task is done, replace the checkbox `[ ]` with `[x]`.
> 4. Commit tests to `app/src/test/...` (unit) or `app/src/androidTest/...` (instrumentation) **with the naming convention** `ClassNameTest.kt`.
> 5. Keep PRs small: **one phase per PR**.
> 6. After pushing, run `./gradlew testDebugUnitTest jacocoTestReport` and update the coverage badge at the end of this doc.

---

## Phase 0 – Baseline & Tools ☑️ *(one‑time setup)*

| Task                                                                                                                  | Details |
| --------------------------------------------------------------------------------------------------------------------- | ------- |
| [ ] Add **JaCoCo** to `build.gradle` (`plugins { id "jacoco" }`) and `testCoverageEnabled true` in *debug* buildType. |         |
| [ ] Append these test dependencies to `build.gradle` (Module: app):                                                  |         |

```groovy
testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3"
testImplementation "io.mockk:mockk:1.13.8"  
testImplementation "com.squareup.okhttp3:mockwebserver:4.9.3"
testImplementation "androidx.test:core:1.5.0"
testImplementation "androidx.room:room-testing:2.5.0"
testImplementation "androidx.arch.core:core-testing:2.2.0"

// Additional dependencies for Compose testing
androidTestImplementation "androidx.compose.ui:ui-test-junit4:$compose_version"
androidTestImplementation "androidx.test.ext:junit:1.1.5"
androidTestImplementation "androidx.test.espresso:espresso-core:3.5.1"
debugImplementation "androidx.compose.ui:ui-test-manifest:$compose_version"
```

| [ ] Create `TestCoroutineRule.kt` with `MainDispatcherRule` pattern to set `Dispatchers.setMain(UnconfinedTestDispatcher())`. |         |
| [ ] Create helper `TestDataFactory.kt` with builders for minimal `Country`, `Car`, `CapitalInfo`, `Name`, etc.              |         |  
| [ ] Prepare **in‑memory Room** provider util (`InMemoryDbHelper.kt`).                                                       |         |

> Commit tag: `test/infra-setup`

---

## Phase 1 – Filtering Strategies

| Task | Class | Scenarios |
|------|-------|-----------|
| [x] `FilterByContinentTest` | `FilterByContinent` | match list, no match, empty input, null/blank key |
| [x] `FilterByLanguageTest` | `FilterByLanguage` | Same 4 scenarios |
| [x] `FilterByDriveSideTest` (already exists) | `FilterByDriveSide` | Verify existing test coverage |

> Expected files: `FilterByContinentTest.kt`, `FilterByLanguageTest.kt`  
> Use `runBlocking { … }` with `UnconfinedTestDispatcher()`.

---

## Phase 2 – Factory Layer

| Task | Goal |
|------|------|
| [ ] `ContinentFilterFactoryTest` ensures `createFilterCriteria()` returns `FilterByContinent` | Verify factory pattern |
| [ ] `DriveSideFilterFactoryTest` ensures correct return type | Verify factory pattern |  
| [ ] `FilterCriteriaFactoryProviderTest` for unknown key returns `null` | Test error handling |

---

## Phase 3 – Room TypeConverters

Write **round‑trip** (`obj → json → obj`) **and null** tests for each converter in `Converters.kt`:

- [ ] `CapitalInfo` converter
- [ ] `Car` converter
- [ ] `Currency` converter
- [ ] `Flags` converter
- [ ] `Idd` converter
- [ ] `Languages` converter
- [ ] `Maps` converter
- [ ] `Name` converter
- [ ] `NameTranslation` converter

> Tips: reuse a singleton `Json { ignoreUnknownKeys = true }` in tests.
> Create `ConvertersTest.kt` (note: `ConverterCriticalTests.kt` and `ConvertersUnitTest.kt` already exist)

---

## Phase 4 – DAO Integration (In‑Memory Room)

| Task | Query | Assertion |
|------|-------|-----------|
| [ ] Insert & getAll | `insertAll()` + `getAllCountries()` | size equality |
| [ ] getCountriesByContinent | `getCountriesByContinent()` | all continents match key |
| [ ] updateCapital | `updateCountryCapital()` | returns 1; capital updated |
| [ ] delete | `deleteCountry()` | size decreases by 1 |

> Create `CountryDaoTest.kt` using in-memory Room database

---

## Phase 5 – Repository Edge Paths (MockK)

Test `CountryRepository` edge cases (note: `CountryRepositoryTest.kt` already exists):

| Case | Mock Setup | Expected |
|------|------------|----------|
| [ ] `fetchAndCacheCountries` when cache warm | Mock DAO returns non‑empty | Provider *not* invoked |
| [ ] Provider returns empty list | Mock provider empty | DAO insert *not* called |
| [ ] Provider throws `IOException` | Mock provider `throws` | verify exception propagated |
| [ ] `getFilteredCountries` on empty cache | `allCountries` empty | returns empty list |
| [ ] Network provider fallback | Mock local provider fails | Network provider called |

---

## Phase 6 – ViewModel Behaviour

- [ ] `CountryOperationViewModelTest`
    1. `init` triggers repository fetch once *(use MockK verify)*
    2. `deleteCountry` reduces `allCountries` size
    3. `updateCountryCapital` mutates capital data
    4. `applyFilter` delegates to repo and updates state
    5. `loadCountries` updates loading state correctly

- [ ] `CountryUIViewModelTest`
    1. Dialog state management
    2. Selected country state updates

---

## Phase 7 – Network Provider (MockWebServer)

Test `CountryListProviderViaNetwork` and `ApiService`:

| Task | Server Response | Expectation |
|------|-----------------|-------------|
| [ ] Happy path | 200 + valid JSON array | `getCountryList()` returns non-empty list |
| [ ] Error path | 500 server error | function throws appropriate exception |
| [ ] Malformed JSON | 200 + invalid JSON | parsing error handled gracefully |
| [ ] Network timeout | Delayed response | timeout exception handled |

> Create `CountryListProviderViaNetworkTest.kt` and `ApiServiceTest.kt`

---

## Phase 8 – Service Provider Tests

Test existing `CountryListServiceProviderImpl`:

| Task | Setup | Verification |
|------|-------|--------------|
| [ ] Valid JSON from resources | Mock context resources | Returns parsed country list |
| [ ] Invalid JSON resource | Mock invalid resource | Handles parsing errors |
| [ ] Missing resource | Mock missing resource | Handles resource not found |

> Note: `CountryListServiceProviderImplTest.kt` already exists - enhance with edge cases

---

## Phase 9 – Compose UI Components

Test UI components and screens:

| Component | Test Scenarios | Framework |
|-----------|----------------|-----------|
| [ ] `CountryCard` | Data display, click interactions | Compose Testing |
| [ ] `CountryCardWithConstraintLayout` | Layout and interactions | Compose Testing |
| [ ] `CircularText` | Text rendering with background | Compose Testing |
| [ ] `MainScreen` | List rendering, loading states | Compose Testing |
| [ ] `ObserveIsLoadingChanges` | Loading state observation | Compose Testing |

> Create instrumentation tests in `app/src/androidTest/...`
> Use `@get:Rule val composeTestRule = createComposeRule()`

---

## Phase 10 – Integration Tests

| Test Type | Scope | Verification |
|-----------|-------|--------------|
| [ ] Database Integration | Room + DAO + Converters | End-to-end data flow |
| [ ] Repository Integration | DAO + Service Providers | Data layer integration |
| [ ] ViewModel Integration | Repository + ViewModels | Business logic flow |

> Use real implementations where possible, mock external dependencies

---

## Coverage Badge

Add to **README** and update after each phase:

```markdown
![coverage](https://img.shields.io/badge/coverage-00%25-red)
```

---

## Definition of Done

- All checkboxes checked `☑️` or `[x]` across all phases.
- JaCoCo HTML report shows **≥ 85%** line coverage for `/app` source set.
- All unit tests pass: `./gradlew testDebugUnitTest`
- All instrumentation tests pass: `./gradlew connectedAndroidTest`
- README badge updated with current coverage percentage.
- Code follows Android testing best practices.
- Tests are maintainable and follow AAA pattern (Arrange, Act, Assert).

## Notes on Existing Tests

The following test files already exist and should be enhanced rather than recreated:

- `FilterByDriveSideTest.kt` ✓
- `FilterCriteriaFactoryProviderTest.kt` ✓
- `ConverterCriticalTests.kt` ✓
- `ConvertersUnitTest.kt` ✓
- `CountryRepositoryTest.kt` ✓
- `CountryListServiceProviderImplTest.kt` ✓
- `ExampleUnitTest.kt` ✓
- `ExampleInstrumentedTest.kt` ✓

Focus on adding missing test scenarios to existing files and creating new test files for untested classes.
