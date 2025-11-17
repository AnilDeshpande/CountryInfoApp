# Gradle Configuration Locking - Verification Steps

This document provides step-by-step verification instructions for the Gradle configuration locking implementation.

## Pre-Verification Setup

Current lock files should exist:
- `settings-gradle.lockfile` - Root buildscript dependencies
- `app/gradle.lockfile` - Application module dependencies

## Verification Test 1: Lock Files Prevent Unwanted Changes

### Step 1.1: Verify Current Build Works
```bash
./gradlew clean build --write-verification-metadata sha256
```

**Expected Result:** Build succeeds without warnings about lock file mismatches.

### Step 1.2: Attempt to Add New Dependency Without Updating Lock
1. Add a new dependency to `app/build.gradle`:
```gradle
dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.11.0")  // Add this line
}
```

2. Run build:
```bash
./gradlew clean build
```

**Expected Result:** Build should fail with error:
```
Could not resolve all dependencies for configuration ':app:debugRuntimeClasspath'.
> Dependency lock state for configuration ':app:debugRuntimeClasspath' is out of date
```

### Step 1.3: Update Lock Files After Adding Dependency
```bash
./gradlew dependencies --write-locks
```

**Expected Result:** Lock files updated successfully.

### Step 1.4: Verify Build Now Succeeds
```bash
./gradlew clean build
```

**Expected Result:** Build succeeds with new dependency.

### Step 1.5: Revert Test Changes
```bash
git checkout app/build.gradle app/gradle.lockfile
```

## Verification Test 2: Lock Files Detect Version Changes

### Step 2.1: Change Version in Version Catalog
Edit `gradle/libs.versions.toml`:
```toml
[versions]
retrofit = "2.10.0"  # Change from current version
```

### Step 2.2: Attempt Build
```bash
./gradlew clean build
```

**Expected Result:** Build fails with lock file mismatch error.

### Step 2.3: Update Lock Files
```bash
./gradlew dependencies --write-locks
```

### Step 2.4: Revert Changes
```bash
git checkout gradle/libs.versions.toml app/gradle.lockfile
```

## Verification Test 3: Lock Mode Verification

### Step 3.1: Test Strict Mode (Default)
Lock mode is configured as `LockMode.STRICT` in both `settings.gradle` and `app/build.gradle`.

Run build:
```bash
./gradlew clean build
```

**Expected Result:** Build succeeds, dependencies resolved from lock files.

### Step 3.2: View Locked Dependencies
```bash
./gradlew :app:dependencies --configuration debugRuntimeClasspath
```

**Expected Result:** Shows dependency tree with exact locked versions.

## Verification Test 4: Lock File Refresh

### Step 4.1: Delete Lock Files
```bash
rm app/gradle.lockfile settings-gradle.lockfile
```

### Step 4.2: Rebuild Lock Files
```bash
./gradlew dependencies --write-locks
```

**Expected Result:** Lock files regenerated with same content as before.

### Step 4.3: Verify No Changes
```bash
git diff app/gradle.lockfile settings-gradle.lockfile
```

**Expected Result:** No differences if no dependency changes occurred.

## Verification Test 5: CI/CD Validation

### Step 5.1: Test Read-Only Build (Simulating CI)
```bash
./gradlew clean build --no-daemon
```

**Expected Result:** Build succeeds using existing lock files.

### Step 5.2: Test with Corrupted Lock File
1. Edit `app/gradle.lockfile` and change a version number
2. Run build:
```bash
./gradlew clean build
```

**Expected Result:** Build fails with lock file validation error.

3. Restore lock file:
```bash
git checkout app/gradle.lockfile
```

## Verification Test 6: Dependency Resolution

### Step 6.1: View All Locked Configurations
```bash
./gradlew :app:dependencies
```

**Expected Result:** Shows all configurations (compile, runtime, test, etc.) with locked versions.

### Step 6.2: Check for Dependency Updates
```bash
./gradlew dependencyUpdates
```

**Note:** This requires the `gradle-versions-plugin`. It will show available updates without modifying lock files.

## Verification Test 7: Multi-Module Lock Files

### Step 7.1: Verify Root Project Locks
```bash
cat settings-gradle.lockfile
```

**Expected Result:** Shows locked buildscript dependencies (Android Gradle Plugin, Kotlin, etc.).

### Step 7.2: Verify App Module Locks
```bash
head -20 app/gradle.lockfile
```

**Expected Result:** Shows locked application dependencies.

## Troubleshooting Common Issues

### Issue 1: Lock File Out of Date
**Symptom:** Build fails with "lock state is out of date"

**Solution:**
```bash
./gradlew dependencies --write-locks
git add **/*.lockfile
git commit -m "Update dependency lock files"
```

### Issue 2: Transitive Dependency Conflict
**Symptom:** Build fails with version conflict despite lock file

**Solution:**
1. Check dependency tree:
```bash
./gradlew :app:dependencies --configuration debugRuntimeClasspath > deps.txt
```

2. Add explicit dependency resolution in `app/build.gradle`:
```gradle
configurations.all {
    resolutionStrategy {
        force("group:artifact:version")
    }
}
```

3. Update locks:
```bash
./gradlew dependencies --write-locks
```

### Issue 3: Lock File Not Generated
**Symptom:** `--write-locks` doesn't create files

**Solution:**
1. Verify lock mode is set to `STRICT` in `build.gradle`
2. Ensure `dependencyLocking` block is configured
3. Run with debug output:
```bash
./gradlew dependencies --write-locks --debug 2>&1 | grep -i lock
```

## Success Criteria

✅ All verification tests pass
✅ Lock files exist for all modules
✅ Build succeeds with existing lock files
✅ Build fails when dependencies change without lock update
✅ Lock files are committed to version control
✅ Team members can build with locked dependencies
✅ CI/CD pipeline respects lock files

## Next Steps

After verification:
1. Commit all lock files to Git
2. Document lock file update process for team
3. Add lock file checks to CI/CD pipeline
4. Schedule periodic dependency updates
5. Monitor for security vulnerabilities

## Maintenance Schedule

- **Weekly:** Check for available dependency updates
- **Monthly:** Review and update dependencies if needed
- **Quarterly:** Full dependency audit and security review
- **As Needed:** Emergency updates for critical security patches

## Additional Resources

- [Gradle Dependency Locking](https://docs.gradle.org/current/userguide/dependency_locking.html)
- [Version Catalog Best Practices](https://docs.gradle.org/current/userguide/platforms.html)
- [Dependency Management Strategies](https://docs.gradle.org/current/userguide/dependency_management.html)

