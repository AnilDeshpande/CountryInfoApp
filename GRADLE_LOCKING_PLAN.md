# Gradle Configuration Locking – Project Plan

This plan documents decisions, scope, and the step-by-step approach to achieve deterministic, no-surprise builds on any machine. It’s tailored to CountryInfoApp and reflects the final state implemented in the repo.

## Objectives

- Reproducible builds on all machines (clean checkout → build succeeds)
- Single source of truth for dependency versions
- Strict dependency locking for all variants (debug/release/tests)
- Enforce JDK 11 with Gradle Toolchains API
- Simple, documented workflow for updating dependencies and locks

## Decisions (Architecture Record)

- Version catalog migration: All-at-once (plugins and dependencies) for consistency
- Lock file maintenance: Documented process using `./gradlew dependencies --update-locks` and PR checklist
- Build cache strategy: Leave disabled (`org.gradle.caching=false`) for now; revisit later if needed
- Gradle Toolchains: Enforce JDK 11 via toolchains; allow auto-download
- Lock files to commit: Both debug and release variants (full module lock files)
- CI/CD: Do not add a workflow now; keep a documented plan to add later

## Scope

- Root and app module covered
- Buildscript/plugin classpath lockfile tracked (`settings-gradle.lockfile`)
- App module dependency lockfile tracked (`app/gradle.lockfile`)
- Version catalog maintained in `gradle/libs.versions.toml`

## Implementation Steps

1. Introduce Version Catalog
   - File: `gradle/libs.versions.toml`
   - Move plugin versions, library versions, and bundles here
2. Migrate builds to Version Catalog Aliases
   - Root `build.gradle`: `plugins { alias(libs.plugins.*) }`
   - `app/build.gradle`: Replace hardcoded coordinates with `libs.*` references
3. Enable Dependency Locking
   - Root: `subprojects { dependencyLocking { lockAllConfigurations(); lockMode = LockMode.STRICT } }`
   - App: keep explicit `dependencyLocking { ... }` (strict)
4. Generate Lock Files
   - Command: `./gradlew dependencies --write-locks`
   - Files produced: `settings-gradle.lockfile`, `app/gradle.lockfile`
5. Enforce JDK 11 Toolchains
   - Root `build.gradle`: configure Java and Kotlin `jvmToolchain(11)` for all projects
   - `gradle.properties`: `org.gradle.java.installations.auto-download=true`
6. Documentation
   - Add/Update: `README.md`, `DEPENDENCY_LOCK_MANAGEMENT.md`, `GRADLE_CONFIGURATION_LOCKING.md`, `QUICK_REFERENCE.md`, `VERIFICATION_STEPS.md`
7. Verification
   - Clean build with locks: `./gradlew clean build`
   - Inspect lockfile diffs on changes

## Developer Workflow

- Daily build: `./gradlew build` (uses existing locks)
- Add/Update a dependency:
  1) Edit `gradle/libs.versions.toml` and `app/build.gradle`
  2) Update locks:
     - All locks: `./gradlew dependencies --write-locks`
     - Scoped update: `./gradlew dependencies --update-locks group:artifact:*`
  3) Build & test: `./gradlew clean build`
  4) Commit: catalog + build files + both lockfiles

## PR Review Checklist (Short)

- Version updated in `libs.versions.toml`
- `app/build.gradle` references `libs.*` (no hardcoded versions)
- Lock files updated (`settings-gradle.lockfile`, `app/gradle.lockfile`)
- No dynamic versions (`+`, ranges)
- Clean build and tests pass
- Lockfile diffs look intentional (no surprising transitives)

## CI/CD (Future Work – Specification)

- Workflow validates lock files present and up-to-date
- Builds from clean checkout using locks
- Optional dependency report + vulnerability scan
- See: `DEPENDENCY_LOCK_MANAGEMENT.md` → CI/CD Integration

## Rollback Plan

- If needed, revert to previous `build.gradle` and delete lockfiles
- Remove dependencyLocking blocks
- Considered low-risk; current state is stable

## Risks and Mitigations

- Risk: Dev forgets to update locks → Build fails early (by design). Fix: run `--write-locks`.
- Risk: Large lockfile diffs hide issues → PR checklist requires diff review.
- Risk: Wrong JDK on a machine → Toolchains enforce JDK 11; auto-download enabled.

## Success Criteria

- Clean checkout builds pass with no manual setup
- Lockfiles present and committed
- Dependency changes always accompanied by lock updates
- Toolchains consistently enforce JDK 11 on all machines

## Current Status

- Version catalog: Complete
- Dependency locking: Enabled, strict
- Toolchains: Enforced (Java/Kotlin) with auto-download
- Lockfiles: Generated and committed
- Docs: Complete
- CI/CD: Pending (plan documented)

Last updated: 2025-11-17

