# Country Info App

A modern Android application built with Kotlin that displays comprehensive information about countries worldwide. The app follows MVVM (Model-View-ViewModel) architecture using Android Jetpack Compose and modern Android development practices.

## 📱 Features

- **Country Listing**: Display all countries with detailed information
- **Search & Filter**: Filter countries by continent, language, and driving side
- **Offline Support**: Local database with Room persistence
- **Update Operations**: Edit country capitals
- **Delete Operations**: Remove countries from the list
- **Modern UI**: Built with Jetpack Compose
- **Network Integration**: Fetch data from REST API using Retrofit

## 🏗️ Architecture

This project implements the **MVVM (Model-View-ViewModel)** architecture pattern with the following components:

- **Model**: Data layer with Room database and network services
- **View**: UI layer built with Jetpack Compose
- **ViewModel**: Business logic and state management
- **Repository**: Data abstraction layer between ViewModel and data sources

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture Pattern**: MVVM
- **Database**: Room (SQLite)
- **Networking**: Retrofit + OkHttp
- **Serialization**: Kotlinx Serialization
- **Image Loading**: Coil
- **Testing**: JUnit, MockK, MockWebServer
- **Code Coverage**: JaCoCo

## 📦 Dependencies

### Core Dependencies
- `androidx.core:core-ktx:1.8.0`
- `androidx.lifecycle:lifecycle-runtime-ktx:2.3.1`
- `androidx.activity:activity-compose:1.5.1`

### Jetpack Compose
- `androidx.compose.ui:ui`
- `androidx.compose.material3:material3`
- `androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0`

### Database
- `androidx.room:room-ktx:2.6.1`
- `androidx.room:room-compiler:2.6.1` (kapt)

### Networking
- `com.squareup.retrofit2:retrofit:2.9.0`
- `com.squareup.retrofit2:converter-gson:2.9.0`
- `com.squareup.okhttp3:logging-interceptor:4.12.0`

### Testing
- `junit:junit:4.13.2`
- `io.mockk:mockk:1.13.8`
- `com.squareup.okhttp3:mockwebserver:4.12.0`
- `androidx.test:core:1.5.0`

## 📁 Project Structure

```
app/
├── build.gradle                          # App-level build configuration
├── src/
│   ├── main/
│   │   ├── AndroidManifest.xml           # App manifest
│   │   ├── java/com/codetutor/countryinfoapp/
│   │   │   ├── MainActivity.kt           # Entry point activity
│   │   │   │
│   │   │   ├── components/               # Reusable UI components
│   │   │   │   ├── CircularText.kt
│   │   │   │   ├── CountryCard.kt
│   │   │   │   ├── CountryCardWithConstraintLayout.kt
│   │   │   │   ├── CountryInfoAppScaffold.kt
│   │   │   │   ├── FilterCountryChips.kt
│   │   │   │   ├── ObserveFilterKeyChanges.kt
│   │   │   │   └── ObserveIsLoadingChanges.kt
│   │   │   │
│   │   │   ├── data/                     # Data models and DTOs
│   │   │   │   ├── Country.kt            # Main country entity
│   │   │   │   ├── CountryInfo.kt
│   │   │   │   ├── Name.kt
│   │   │   │   ├── Languages.kt
│   │   │   │   ├── Currency.kt
│   │   │   │   ├── Flags.kt
│   │   │   │   ├── Maps.kt
│   │   │   │   ├── Car.kt
│   │   │   │   ├── CapitalInfo.kt
│   │   │   │   ├── CoatOfArms.kt
│   │   │   │   ├── Demonyms.kt
│   │   │   │   ├── Idd.kt
│   │   │   │   ├── NativeName.kt
│   │   │   │   ├── Translations.kt
│   │   │   │   ├── Helper.kt
│   │   │   │   └── [Language-specific models]
│   │   │   │       ├── Ara.kt, Bre.kt, Ces.kt, Cym.kt
│   │   │   │       ├── Deu.kt, Eng.kt, Est.kt, Fin.kt
│   │   │   │       ├── Fra.kt, Hrv.kt, Hun.kt, Ita.kt
│   │   │   │       ├── Jpn.kt, Kor.kt, Nld.kt, NOK.kt
│   │   │   │       ├── Nor.kt, Per.kt, Pol.kt, Por.kt
│   │   │   │       ├── Rus.kt, Slk.kt, Spa.kt, Srp.kt
│   │   │   │       ├── Swe.kt, Tur.kt, Urd.kt, Zho.kt
│   │   │   │
│   │   │   ├── database/                 # Local database layer
│   │   │   │   ├── appdb/               # Database configuration
│   │   │   │   │   ├── AppDataBase.kt   # Room database class
│   │   │   │   │   └── DatabaseProvider.kt
│   │   │   │   ├── converters/          # Type converters for Room
│   │   │   │   │   └── Converters.kt
│   │   │   │   └── dao/                 # Data Access Objects
│   │   │   │       ├── ICountryDao.kt   # DAO interface
│   │   │   │       └── CountryDao.kt    # DAO implementation
│   │   │   │
│   │   │   ├── dialogs/                 # Dialog components
│   │   │   │   ├── DialogDeleteCountry.kt
│   │   │   │   └── DialogUpdateCountry.kt
│   │   │   │
│   │   │   ├── repository/              # Repository layer
│   │   │   │   ├── ICountryRepository.kt # Repository interface
│   │   │   │   ├── CountryRepository.kt  # Repository implementation
│   │   │   │   ├── FilterCriteria.kt    # Filter interface
│   │   │   │   ├── FilterByContinent.kt # Continent filter
│   │   │   │   ├── FilterByDriveSide.kt # Drive side filter
│   │   │   │   ├── FilterByLanguage.kt  # Language filter
│   │   │   │   └── service/             # Network services
│   │   │   │       ├── CountryListServiceProvider.kt
│   │   │   │       ├── CountryListServiceProviderImpl.kt
│   │   │   │       ├── CountryListProviderViaNetwork.kt
│   │   │   │       └── network/         # Network layer
│   │   │   │           ├── ApiService.kt
│   │   │   │           └── RetrofitInstance.kt
│   │   │   │
│   │   │   ├── screens/                 # Screen composables
│   │   │   │   └── MainScreen.kt
│   │   │   │
│   │   │   ├── ui/                      # UI theme and styling
│   │   │   │   └── theme/
│   │   │   │       ├── Color.kt
│   │   │   │       ├── Shape.kt
│   │   │   │       ├── Theme.kt
│   │   │   │       └── Type.kt
│   │   │   │
│   │   │   ├── util/                    # Utility classes
│   │   │   │   ├── Helper.kt
│   │   │   │   ├── FilterCriteriaFactory.kt
│   │   │   │   ├── FilterCriteriaFactoryProvider.kt
│   │   │   │   ├── ContinentFilterFactory.kt
│   │   │   │   └── DriveSideFilterFactory.kt
│   │   │   │
│   │   │   └── viewmodel/               # ViewModels
│   │   │       ├── ICountryOperationViewModel.kt
│   │   │       ├── CountryOperationViewModel.kt
│   │   │       ├── CountryUIViewModel.kt
│   │   │       └── CountryViewModelFactory.kt
│   │   │
│   │   └── res/                         # Android resources
│   │       ├── drawable/
│   │       ├── mipmap/
│   │       ├── values/
│   │       └── xml/
│   │
│   └── test/                            # Unit tests
│       ├── assets/                      # Test assets
│       └── java/com/codetutor/countryinfoapp/
│           └── repository/
│               └── FilterByDriveSideTest.kt
│
├── build.gradle                         # Project-level build configuration
├── gradle.properties                    # Gradle properties
├── settings.gradle                      # Gradle settings
└── local.properties                     # Local configuration
```

## 🏛️ MVVM Architecture Implementation

### 📊 Model Layer

#### Data Models
- **Country.kt**: Main entity with Room annotations for local database storage
- **Supporting Models**: Name, Languages, Currency, Flags, Maps, etc.

#### Database Layer
- **AppDataBase.kt**: Room database configuration with type converters
- **CountryDao.kt**: Data Access Object with CRUD operations
- **Converters.kt**: Type converters for complex data types

#### Network Layer
- **ApiService.kt**: Retrofit interface for REST API calls
- **RetrofitInstance.kt**: Retrofit configuration and setup

### 🏪 Repository Layer

#### Repository Pattern
- **ICountryRepository.kt**: Repository interface defining data operations
- **CountryRepository.kt**: Implementation handling both local and remote data sources

#### Filter Strategy Pattern
- **FilterCriteria.kt**: Base interface for filtering strategies
- **FilterByContinent.kt**: Filter countries by continent
- **FilterByDriveSide.kt**: Filter countries by driving side
- **FilterByLanguage.kt**: Filter countries by language

#### Service Layer
- **CountryListServiceProvider.kt**: Service interface for data retrieval
- **CountryListServiceProviderImpl.kt**: Service implementation
- **CountryListProviderViaNetwork.kt**: Network-specific data provider

### 🎯 ViewModel Layer

#### ViewModels
- **ICountryOperationViewModel.kt**: ViewModel interface
- **CountryOperationViewModel.kt**: Main ViewModel managing country operations
- **CountryUIViewModel.kt**: UI state management ViewModel
- **CountryViewModelFactory.kt**: Factory for ViewModel creation

#### State Management
- Uses `MutableState` and `MutableStateFlow` for reactive state management
- Handles coroutines for asynchronous operations
- Manages UI state and business logic separation

### 🎨 View Layer (Jetpack Compose)

#### Screens
- **MainScreen.kt**: Primary screen composable

#### Components
- **CountryCard.kt**: Individual country display component
- **CountryCardWithConstraintLayout.kt**: Alternative card layout
- **FilterCountryChips.kt**: Filter selection UI
- **CountryInfoAppScaffold.kt**: Main app structure

#### Dialogs
- **DialogDeleteCountry.kt**: Confirmation dialog for deletion
- **DialogUpdateCountry.kt**: Dialog for editing country information

#### Theme
- **Theme.kt**: Material Design theme configuration
- **Color.kt**: Color palette definition
- **Type.kt**: Typography definitions
- **Shape.kt**: Shape definitions

## 🔧 Key Features Implementation

### Data Flow
1. **Data Fetching**: Repository fetches data from network API using Retrofit
2. **Local Storage**: Data is stored locally using Room database
3. **State Management**: ViewModels manage UI state and business logic
4. **UI Updates**: Compose UI observes ViewModel state changes

### Filter System
The app implements a Strategy Pattern for filtering:
- **Factory Pattern**: `FilterCriteriaFactoryProvider` creates appropriate filters
- **Strategy Pattern**: Different filter implementations for various criteria
- **Chain of Responsibility**: Filters can be combined and applied

### CRUD Operations
- **Create**: Insert countries from API
- **Read**: Display countries in list format
- **Update**: Edit country capitals
- **Delete**: Remove countries from database

## 🧪 Testing Strategy

### Unit Tests
- **Repository Tests**: Testing filter implementations
- **ViewModel Tests**: Business logic validation
- **Network Tests**: API response handling

### Test Tools
- **JUnit**: Core testing framework
- **MockK**: Mocking framework for Kotlin
- **MockWebServer**: HTTP client testing
- **Coroutines Test**: Testing suspend functions

### Code Coverage
- **JaCoCo**: Code coverage analysis
- **Configurable Reports**: HTML, XML, and CSV formats

## 📱 Build Configuration

### Build Types
- **Debug**: Development build with testing coverage disabled
- **Release**: Production build with ProGuard optimization

### Compilation
- **Source/Target Compatibility**: Java 11
- **Kotlin JVM Target**: JVM 11
- **Compose Compiler**: Version 1.5.1

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Kotlin 1.8.0 or later
- Android SDK 25 or higher
- Gradle 7.0 or later

### Installation
1. Clone the repository
2. Open project in Android Studio
3. Sync Gradle files
4. Run the application

### Building
```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Run tests
./gradlew test

# Generate test coverage report
./gradlew jacocoTestReport
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## 📞 Contact

For questions or support, please open an issue in the repository.

---

**Note**: This project demonstrates modern Android development practices including MVVM architecture, Jetpack Compose, Room database, Retrofit networking, and comprehensive testing strategies.