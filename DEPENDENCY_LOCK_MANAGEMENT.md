# Dependency Lock File Management Guide

This document explains how to manage dependency lock files to ensure reproducible builds across all development environments and CI/CD pipelines.

## 📋 Table of Contents
- [Overview](#overview)
- [Lock File Structure](#lock-file-structure)
- [Common Operations](#common-operations)
- [PR Checklist](#pr-checklist-for-dependency-changes)
- [Troubleshooting](#troubleshooting)
- [CI/CD Integration (Future)](#cicd-integration-future)

---

## Overview

### What are Dependency Lock Files?

Dependency lock files freeze the exact versions of all direct and transitive dependencies used in the project. This ensures that:
- ✅ Builds are reproducible across different machines
- ✅ New developers get the exact same dependency versions
- ✅ CI/CD builds match local development builds
- ✅ Transitive dependency updates don't break builds unexpectedly

### Files Involved

```
CountryInfoApp/
├── settings-gradle.lockfile         # Root buildscript/plugins lock file
├── app/
│   └── gradle.lockfile             # App module lock file (debug & release)
└── gradle/
    └── libs.versions.toml          # Version catalog (source of truth)
```

---

## Lock File Structure

Lock files are generated for ALL configurations (debug, release, test, androidTest, etc.) to ensure complete reproducibility.

### Example Lock File Entry
```
androidx.core:core-ktx:1.8.0=debugCompileClasspath,debugRuntimeClasspath,releaseCompileClasspath,releaseRuntimeClasspath
```

This shows that `core-ktx:1.8.0` is locked for both debug and release variants across compile and runtime classpaths.

---

## Common Operations

### 1. Initial Lock File Generation

**When:** First time setting up the project or after cloning.

```bash
# Clean any existing lock files
find . -name "gradle.lockfile" -o -name "settings-gradle.lockfile" -delete

# Generate lock files for all configurations
./gradlew dependencies --write-locks
./gradlew :app:dependencies --write-locks

# Verify build works with locked dependencies
./gradlew clean assembleDebug assembleRelease
./gradlew testDebugUnitTest
```

**Expected Output:**
- `settings-gradle.lockfile` at repository root
- `app/gradle.lockfile` under the app module
- Both files should be committed to version control

---

### 2. Adding a New Dependency

**When:** Adding a new library to the project.

**Steps:**

1. **Update version catalog** (`gradle/libs.versions.toml`):
   ```toml
   [versions]
   new-library = "1.0.0"
   
   [libraries]
   new-library = { group = "com.example", name = "new-library", version.ref = "new-library" }
   ```

2. **Update build.gradle** (`app/build.gradle`):
   ```groovy
   dependencies {
       implementation libs.new.library
   }
   ```

3. **Update lock files**:
   ```bash
   ./gradlew dependencies --write-locks
   ```

4. **Verify the build**:
   ```bash
   ./gradlew clean assembleDebug
   ./gradlew testDebugUnitTest
   ```

5. **Review changes**:
   ```bash
   git diff settings-gradle.lockfile app/gradle.lockfile
   ```

6. **Commit all changes**:
   ```bash
   git add gradle/libs.versions.toml
   git add app/build.gradle
   git add settings-gradle.lockfile app/gradle.lockfile
   git commit -m "Add new-library dependency"
   ```

---

### 3. Updating an Existing Dependency

**When:** Upgrading a library to a newer version.

**Steps:**

1. **Update version in catalog** (`gradle/libs.versions.toml`):
   ```toml
   [versions]
   retrofit = "2.10.0"  # Changed from 2.9.0
   ```

2. **Update lock files** (use --update-locks to limit scope, or --write-locks):
   ```bash
   # Update only retrofit artifacts
   ./gradlew dependencies --update-locks com.squareup.retrofit2:*

   # Or update all locks
   ./gradlew dependencies --write-locks
   ```

3. **Test thoroughly**:
   ```bash
   ./gradlew clean build
   ./gradlew testDebugUnitTest
   ./gradlew connectedAndroidTest  # If applicable
   ```

4. **Review changes carefully**:
   ```bash
   git diff settings-gradle.lockfile app/gradle.lockfile
   ```
   
   Look for:
   - ✅ Expected version updates
   - ⚠️ Unexpected transitive dependency changes
   - ❌ Major version jumps that might break compatibility

5. **Commit changes**:
   ```bash
   git add gradle/libs.versions.toml
   git add settings-gradle.lockfile app/gradle.lockfile
   git commit -m "Update retrofit from 2.9.0 to 2.10.0"
   ```

---

### 4. Refreshing All Lock Files

**When:** Major refactoring, clearing stale locks, or after Gradle version upgrade.

```bash
# Delete all existing lock files
find . -name "gradle.lockfile" -o -name "settings-gradle.lockfile" -delete

# Regenerate from scratch
./gradlew dependencies --write-locks

# Full clean build verification
./gradlew clean build
```

---

### 5. Resolving Lock File Conflicts

**When:** Git merge conflicts in lock files.

**Option A: Accept theirs and regenerate**
```bash
# Take the incoming changes
git checkout --theirs settings-gradle.lockfile app/gradle.lockfile

# Regenerate to ensure consistency
./gradlew dependencies --write-locks

# Verify
./gradlew build
```

**Option B: Manual merge**
```bash
# Resolve conflicts manually in lock files
# Then regenerate to validate
./gradlew dependencies --write-locks
```

**⚠️ Warning:** Never manually edit lock files. Always regenerate after resolving conflicts.

---

## PR Checklist for Dependency Changes

When submitting a PR that modifies dependencies, ensure:

- [ ] **Version catalog updated** (`gradle/libs.versions.toml`)
- [ ] **Build.gradle updated** with new dependency references
- [ ] **Lock files regenerated** using `--write-locks` or `--update-locks`
- [ ] **Both debug and release** lock files included
- [ ] **Clean build succeeds**: `./gradlew clean build`
- [ ] **All tests pass**: `./gradlew test`
- [ ] **Lock file diff reviewed** for unexpected changes
- [ ] **No dynamic versions** (no `+`, `latest.release`, or version ranges)
- [ ] **Commit message** describes the dependency change and reason
- [ ] **Breaking changes documented** if major version update

### PR Template Addition

Add this section to your PR description when modifying dependencies:

```markdown
## Dependency Changes

### Modified Dependencies
- `library-name`: `old-version` → `new-version`

### Reason
[Why is this change needed?]

### Impact
- [ ] Breaking changes: [Yes/No - describe if yes]
- [ ] Transitive dependency updates: [List significant ones]
- [ ] Build time impact: [Increased/Decreased/No change]

### Testing
- [ ] Unit tests pass
- [ ] Integration tests pass
- [ ] Manual testing performed: [Describe]

### Lock File Changes
```
[Paste relevant git diff of lock files]
```
```

---

## Troubleshooting

### Issue: "Could not resolve all dependencies"

**Symptom:**
```
* What went wrong:
Execution failed for task ':app:dependencies'.
> Could not resolve all dependencies for configuration ':app:debugCompileClasspath'.
```

**Solution:**
```bash
# Check for lock file corruption
find . -name "*.lockfile" -exec cat {} \;

# Regenerate lock files
find . -name "gradle.lockfile" -o -name "settings-gradle.lockfile" -delete
./gradlew dependencies --write-locks
```

---

### Issue: "Dependency lock state out of date"

**Symptom:**
```
Dependency lock state for configuration ':app:debugCompileClasspath' is out of date
```

**Solution:**
```bash
# Update lock files (scoped)
./gradlew dependencies --update-locks group:artifact:*

# Or regenerate completely
./gradlew dependencies --write-locks
```

---

### Issue: Build fails after pulling latest code

**Symptom:**
Build fails with dependency resolution errors after `git pull`.

**Solution:**
```bash
# Sync Gradle files
./gradlew --refresh-dependencies dependencies --write-locks

# Clean and rebuild
./gradlew clean build
```

---

### Issue: Lock files not being generated

**Symptom:**
Running `--write-locks` doesn't create lock files.

**Solution:**
Check that `dependencyLocking` is configured in `build.gradle`:

```groovy
dependencyLocking {
    lockAllConfigurations()
    lockMode = LockMode.STRICT
}
```

---

### Issue: Transitive dependency conflict

**Symptom:**
Multiple versions of the same library appear in lock file.

**Solution:**
```bash
# View dependency tree
./gradlew :app:dependencies --configuration debugCompileClasspath

# Force specific version in build.gradle
configurations.all {
    resolutionStrategy {
        force 'com.example:conflicting-library:1.0.0'
    }
}

# Regenerate locks
./gradlew dependencies --write-locks
```

---

## CI/CD Integration (Future)

### Planned GitHub Actions Workflow

This section documents the planned CI/CD integration for automated dependency lock validation. **This is not yet implemented** but serves as a specification for future work.

#### Workflow Goals

1. **Validate lock files** are present and up-to-date
2. **Verify clean builds** from scratch using locked dependencies
3. **Detect lock file drift** (changes not committed)
4. **Run security scans** on locked dependencies
5. **Generate dependency reports** for auditing

#### Proposed Workflow File

**Location:** `.github/workflows/dependency-lock-validation.yml`

**Content:**
```yaml
name: Dependency Lock Validation

on:
  pull_request:
    paths:
      - 'gradle/libs.versions.toml'
      - '**/build.gradle'
      - '**/gradle.lockfile'
      - '.github/workflows/dependency-lock-validation.yml'
  push:
    branches: [ main, develop ]

jobs:
  validate-locks:
    runs-on: ubuntu-latest
    timeout-minutes: 30
    
    steps:
      - name: Checkout code
        uses: actions/checkout@v4
        
      - name: Set up JDK 11
        uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '11'
          
      - name: Cache Gradle packages
        uses: actions/cache@v3
        with:
          path: |
            ~/.gradle/caches
            ~/.gradle/wrapper
          key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties') }}
          restore-keys: |
            ${{ runner.os }}-gradle-
            
      - name: Validate Gradle wrapper
        uses: gradle/wrapper-validation-action@v1
        
      - name: Check lock files exist
        run: |
          if [ ! -f "gradle.lockfile" ]; then
            echo "::error::Root gradle.lockfile is missing"
            exit 1
          fi
          if [ ! -f "app/gradle.lockfile" ]; then
            echo "::error::app/gradle.lockfile is missing"
            exit 1
          fi
          echo "✅ All lock files present"
          
      - name: Verify lock files are up-to-date
        run: |
          # Create backup of lock files
          cp gradle.lockfile gradle.lockfile.backup
          cp app/gradle.lockfile app/gradle.lockfile.backup
          
          # Regenerate lock files
          ./gradlew dependencies --write-locks
          
          # Compare with committed versions
          if ! diff -q gradle.lockfile gradle.lockfile.backup; then
            echo "::error::Root gradle.lockfile is out of date"
            echo "Run: ./gradlew dependencies --write-locks"
            exit 1
          fi
          
          if ! diff -q app/gradle.lockfile app/gradle.lockfile.backup; then
            echo "::error::app/gradle.lockfile is out of date"
            echo "Run: ./gradlew dependencies --write-locks"
            exit 1
          fi
          
          echo "✅ Lock files are up-to-date"
          
      - name: Build with locked dependencies
        run: |
          ./gradlew clean assembleDebug assembleRelease --no-daemon
          
      - name: Run tests with locked dependencies
        run: |
          ./gradlew testDebugUnitTest --no-daemon
          
      - name: Generate dependency report
        if: github.event_name == 'pull_request'
        run: |
          ./gradlew :app:dependencies --configuration releaseRuntimeClasspath > dependency-report.txt
          
      - name: Upload dependency report
        if: github.event_name == 'pull_request'
        uses: actions/upload-artifact@v3
        with:
          name: dependency-report
          path: dependency-report.txt
          
      - name: Comment PR with dependency changes
        if: github.event_name == 'pull_request'
        uses: actions/github-script@v6
        with:
          script: |
            const fs = require('fs');
            const report = fs.readFileSync('dependency-report.txt', 'utf8');
            
            github.rest.issues.createComment({
              issue_number: context.issue.number,
              owner: context.repo.owner,
              repo: context.repo.repo,
              body: `## Dependency Lock Report\n\n<details><summary>View Dependencies</summary>\n\n\`\`\`\n${report.substring(0, 5000)}\n\`\`\`\n\n</details>`
            });

  dependency-security-scan:
    runs-on: ubuntu-latest
    needs: validate-locks
    
    steps:
      - name: Checkout code
        uses: actions/checkout@v4
        
      - name: Run dependency vulnerability scan
        uses: dependency-check/Dependency-Check_Action@main
        with:
          project: 'CountryInfoApp'
          path: '.'
          format: 'HTML'
          
      - name: Upload scan results
        uses: actions/upload-artifact@v3
        with:
          name: dependency-security-scan
          path: reports/
```

#### Implementation Checklist (Future)

When implementing CI/CD integration:

- [ ] Create `.github/workflows/` directory
- [ ] Add `dependency-lock-validation.yml` workflow file
- [ ] Configure repository secrets (if needed for private repos)
- [ ] Test workflow on a feature branch
- [ ] Add workflow status badge to README.md
- [ ] Document workflow in this file
- [ ] Set up branch protection rules requiring workflow pass
- [ ] Configure notifications for workflow failures
- [ ] Schedule periodic dependency updates PRs (optional)
- [ ] Integrate with dependency vulnerability scanning (Dependabot/Snyk)

#### Expected Benefits (Post-Implementation)

1. **Automated Validation**: Every PR automatically validates lock files
2. **Early Detection**: Catches lock file issues before merge
3. **Build Confidence**: Ensures clean builds from scratch
4. **Dependency Visibility**: PRs show exactly what dependencies changed
5. **Security**: Automated vulnerability scanning of locked dependencies
6. **Documentation**: Automatic generation of dependency reports

---

## Best Practices

### ✅ DO

- **Always commit lock files** to version control
- **Regenerate locks** after changing `libs.versions.toml`
- **Review lock file diffs** before committing
- **Test builds** after updating locks
- **Document breaking changes** in dependency updates
- **Use version catalog** as single source of truth
- **Lock both debug and release** configurations

### ❌ DON'T

- **Don't manually edit** lock files
- **Don't use dynamic versions** (`+`, `latest.release`)
- **Don't ignore lock file conflicts** - resolve properly
- **Don't skip testing** after lock updates
- **Don't commit partial** lock file changes
- **Don't use version ranges** in version catalog
- **Don't bypass** the `--write-locks` regeneration process

---

## Quick Reference

| Task | Command |
|------|---------|
| Generate lock files | `./gradlew dependencies --write-locks` |
| Update specific dependency | `./gradlew dependencies --update-locks group:artifact:*` |
| Update all locks | `./gradlew dependencies --write-locks` |
| Delete all locks | `find . -name "gradle.lockfile" -o -name "settings-gradle.lockfile" -delete` |
| View dependencies | `./gradlew :app:dependencies` |
| Verify build | `./gradlew clean build` |
| Run tests | `./gradlew testDebugUnitTest` |

---

## Support

For issues or questions about dependency lock management:
1. Check [Troubleshooting](#troubleshooting) section
2. Review [Common Operations](#common-operations)
3. Check Gradle documentation: https://docs.gradle.org/current/userguide/dependency_locking.html

---

**Last Updated:** November 17, 2025  
**Gradle Version:** 8.0  
**AGP Version:** 8.0.0
