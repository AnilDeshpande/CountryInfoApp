# Gradle Dependency Locking - Quick Reference Card

## 📋 Most Common Commands

### Build Project (Normal Development)
```bash
./gradlew clean build
```
Uses existing lock files. This is what you'll use 99% of the time.

### Update Lock Files (After Changing Dependencies)
```bash
./gradlew dependencies --write-locks
```
Run this ONLY when you've added/updated/removed dependencies.

### View Dependency Tree
```bash
./gradlew :app:dependencies --configuration debugRuntimeClasspath
```
See all dependencies and their locked versions.

### Check for Dependency Updates
```bash
./gradlew dependencyUpdates
```
Shows available updates without changing anything.

---

## 🔧 Adding a New Dependency

### Step 1: Add to Version Catalog
Edit `gradle/libs.versions.toml`:

```toml
[versions]
newlib = "1.0.0"

[libraries]
newlib = { group = "com.example", name = "newlib", version.ref = "newlib" }
```

### Step 2: Reference in build.gradle
Edit `app/build.gradle`:

```gradle
dependencies {
    implementation(libs.newlib)
}
```

### Step 3: Update Lock Files
```bash
./gradlew dependencies --write-locks
```

### Step 4: Commit Everything
```bash
git add gradle/libs.versions.toml app/build.gradle app/gradle.lockfile
git commit -m "Add newlib dependency"
```

---

## 🔄 Updating an Existing Dependency

### Step 1: Update Version
Edit `gradle/libs.versions.toml`:
```toml
[versions]
retrofit = "2.10.0"  # Changed from 2.9.0
```

### Step 2: Update Locks
```bash
./gradlew dependencies --write-locks
```

### Step 3: Test
```bash
./gradlew clean build
./gradlew test
```

### Step 4: Commit
```bash
git add gradle/libs.versions.toml app/gradle.lockfile
git commit -m "Update Retrofit to 2.10.0"
```

---

## ❌ Common Errors & Fixes

### Error: "Lock state is out of date"
**Cause:** Dependencies changed but lock files not updated

**Fix:**
```bash
./gradlew dependencies --write-locks
```

### Error: Build fails after git pull
**Cause:** Someone else updated dependencies

**Fix:**
```bash
git pull  # Make sure you have latest lock files
./gradlew clean build
```

### Error: "Could not resolve all dependencies"
**Cause:** Lock file conflicts with new dependency

**Fix:**
```bash
./gradlew dependencies --write-locks
```

---

## 📁 File Locations

| File | Purpose | When to Edit |
|------|---------|--------------|
| `gradle/libs.versions.toml` | All dependency versions | Adding/updating dependencies |
| `app/gradle.lockfile` | Locked app dependencies | Auto-generated, commit to Git |
| `settings-gradle.lockfile` | Locked build dependencies | Auto-generated, commit to Git |
| `app/build.gradle` | Dependency declarations | Adding new dependencies |

---

## ✅ Daily Workflow Checklist

### Starting Your Day
- [ ] `git pull` to get latest code and lock files
- [ ] `./gradlew build` to verify everything works

### Adding Dependencies
- [ ] Edit `gradle/libs.versions.toml`
- [ ] Edit `app/build.gradle`
- [ ] Run `./gradlew dependencies --write-locks`
- [ ] Test: `./gradlew clean build`
- [ ] Commit: version catalog + build.gradle + lock files

### Before Committing
- [ ] Run `./gradlew build` successfully
- [ ] Include lock files if you changed dependencies
- [ ] Write clear commit message about dependency changes

### Code Review Checklist
- [ ] Lock files included if dependencies changed
- [ ] Version changes documented in commit message
- [ ] Build succeeds on CI/CD

---

## 🚫 Don't Do This

❌ Don't manually edit lock files
❌ Don't add lock files to .gitignore
❌ Don't update dependencies without updating locks
❌ Don't skip testing after dependency updates
❌ Don't commit version catalog without lock files

---

## ✅ Always Do This

✅ Commit lock files with dependency changes
✅ Run `--write-locks` after changing dependencies
✅ Test build after updating dependencies
✅ Document why you're updating in commit message
✅ Review dependency changes in PRs

---

## 🆘 Emergency Procedures

### Build Completely Broken
```bash
# 1. Stash your changes
git stash

# 2. Get clean state
git checkout main
./gradlew clean

# 3. Try build
./gradlew build

# 4. If works, reapply changes carefully
git stash pop
```

### Lock Files Corrupted
```bash
# 1. Delete lock files
rm app/gradle.lockfile settings-gradle.lockfile

# 2. Regenerate
./gradlew dependencies --write-locks

# 3. Test
./gradlew clean build

# 4. Commit
git add *.lockfile app/gradle.lockfile
git commit -m "Regenerate lock files"
```

### Need to Force a Version
Edit `app/build.gradle`:
```gradle
configurations.all {
    resolutionStrategy {
        force("com.squareup.retrofit2:retrofit:2.9.0")
    }
}
```
Then run `./gradlew dependencies --write-locks`

---

## 📚 More Information

- **Detailed Guide:** `DEPENDENCY_LOCK_MANAGEMENT.md`
- **Technical Details:** `GRADLE_CONFIGURATION_LOCKING.md`
- **Verification Tests:** `VERIFICATION_STEPS.md`
- **Implementation Summary:** `IMPLEMENTATION_SUMMARY.md`
- **Official Docs:** https://docs.gradle.org/current/userguide/dependency_locking.html

---

## 💡 Pro Tips

1. **Update dependencies in small batches** - Easier to troubleshoot
2. **Review lock file diffs** - Catch unexpected transitive updates
3. **Keep dependencies reasonably current** - Don't fall too far behind
4. **Test thoroughly after updates** - Especially major version bumps
5. **Document breaking changes** - Help your teammates

---

## 🎯 Remember

> **The lock files are the source of truth for what gets built.**
> 
> If your build.gradle says version X but the lock file says version Y,
> version Y will be used. Always update locks when changing dependencies!

---

**Quick Help:**
- Build: `./gradlew build`
- Update locks: `./gradlew dependencies --write-locks`
- View deps: `./gradlew :app:dependencies`
- Check updates: `./gradlew dependencyUpdates`

---

*Last Updated: November 17, 2025*
*Keep this card handy - you'll reference it often!*

