# CountryInfoApp

This is a sample Android app that lists all the countries of the world.

## 🔧 Build Requirements

### Prerequisites

- **JDK 11**: Required for building the project
  - The project uses Gradle Toolchains API which will automatically provision JDK 11 if not present
  - To verify your JDK version: `java -version`
  
- **Android SDK**: API Level 34 (compileSdk)
  - Minimum SDK: API 25
  - Target SDK: API 33

- **Gradle**: 8.0 (via wrapper)
  - Use the included wrapper: `./gradlew` (Unix/Mac) or `gradlew.bat` (Windows)
  - Do not use a global Gradle installation

### First Time Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd CountryInfoApp
   ```

2. **Generate dependency lock files**
   ```bash
   ./gradlew dependencies --write-locks
   ```
   
   This creates lock files that ensure reproducible builds across all environments.

3. **Build the project**
   ```bash
   ./gradlew clean assembleDebug
   ```

4. **Run tests**
   ```bash
   ./gradlew testDebugUnitTest
   ```

---

## 🔒 Locked Configuration

This project uses **locked Gradle configurations** to ensure reproducible builds across different machines and CI/CD environments.

### What This Means

- ✅ All dependency versions (including transitive dependencies) are locked
- ✅ Builds are guaranteed to be reproducible
- ✅ JDK 11 is enforced via Gradle Toolchains API
- ✅ Both debug and release variants are locked

### Key Files

- **`gradle/libs.versions.toml`**: Single source of truth for all dependency versions
- **`settings-gradle.lockfile`**: Root project/buildscript dependency locks
- **`app/gradle.lockfile`**: App module dependency locks

### When Working with Dependencies

If you add or update dependencies, you must regenerate lock files:

```bash
# Update all locks
./gradlew dependencies --write-locks

# Or selectively update locks for one library
./gradlew dependencies --update-locks group:artifact:*
```

For detailed instructions, see [DEPENDENCY_LOCK_MANAGEMENT.md](DEPENDENCY_LOCK_MANAGEMENT.md).

---

## 📦 Dependencies

The project uses a **Version Catalog** (`gradle/libs.versions.toml`) to manage dependencies centrally.

### Major Libraries

- **Kotlin**: 1.9.0
- **Compose**: BOM 2022.10.00
- **Lifecycle**: 2.7.0
- **Room**: 2.6.1
- **Retrofit**: 2.9.0
- **Coil**: 2.4.0

### Code Quality Tools

- **ktlint**: 12.1.1 - [Setup Guide](KTLINT_SETUP.md)
- **Detekt**: 1.23.7 - [Setup Guide](DETEKT_SETUP.md), [Status](DETEKT_STATUS.md)
- **JaCoCo**: 0.8.8 - Code coverage

---

## 🛠️ Common Tasks

### Building

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Clean build
./gradlew clean build
```

### Testing

```bash
# Run unit tests
./gradlew testDebugUnitTest

# Run unit tests with coverage
./gradlew testDebugUnitTest jacocoTestReport

# Run instrumented tests
./gradlew connectedAndroidTest
```

### Code Quality

```bash
# Run ktlint check
./gradlew ktlintCheck

# Run ktlint format (auto-fix)
./gradlew ktlintFormat

# Run Detekt
./gradlew detekt
```

### Dependency Management

```bash
# View dependency tree
./gradlew :app:dependencies

# Update lock files
./gradlew dependencies --write-locks

# Verify JDK toolchain
./gradlew -q javaToolchains
```

---

## 📚 Documentation

- **[DEPENDENCY_LOCK_MANAGEMENT.md](DEPENDENCY_LOCK_MANAGEMENT.md)**: Complete guide for managing locked dependencies
- **[GRADLE_CONFIGURATION_LOCKING.md](GRADLE_CONFIGURATION_LOCKING.md)**: Summary of Gradle locking implementation
- **[KTLINT_SETUP.md](KTLINT_SETUP.md)**: ktlint configuration and usage
- **[DETEKT_SETUP.md](DETEKT_SETUP.md)**: Detekt configuration and usage
- **[DETEKT_STATUS.md](DETEKT_STATUS.md)**: Current Detekt status
- **[unit_test_todo.md](unit_test_todo.md)**: Unit testing TODO list

---

## 🚀 CI/CD Integration (Planned)

Automated CI/CD integration for dependency lock validation is planned but not yet implemented. See [DEPENDENCY_LOCK_MANAGEMENT.md - CI/CD Integration](DEPENDENCY_LOCK_MANAGEMENT.md#cicd-integration-future) for the implementation plan.

---

## 📄 License

See [LICENSE](LICENSE) file for details.

---

## 🤝 Contributing

When contributing:

1. Follow the existing code style (enforced by ktlint and Detekt)
2. If adding/updating dependencies:
   - Update `gradle/libs.versions.toml`
   - Run `./gradlew dependencies --write-locks`
   - Follow the [PR checklist](DEPENDENCY_LOCK_MANAGEMENT.md#pr-checklist-for-dependency-changes)
3. Ensure all tests pass
4. Update documentation as needed

---

**Build System:** Gradle 8.0 with locked configurations  
**Language:** Kotlin 1.9.0  
**Min SDK:** 25 | **Target SDK:** 33 | **Compile SDK:** 34  
**JDK:** 11 (enforced via Gradle Toolchains)
