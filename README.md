# GitLens 🔍

[![Kotlin](https://img.shields.io/badge/Kotlin-2.4.20-blue.svg?logo=kotlin)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-2026.09.00-green.svg?logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Material 3](https://img.shields.io/badge/Material%203-Enabled-purple.svg)](https://m3.material.io)
[![Koin](https://img.shields.io/badge/Koin-4.2.2-orange.svg)](https://insert-koin.io)
[![EasifyAndroid](https://img.shields.io/badge/Easify--Android-2.2.0-teal.svg)](https://github.com/ahbandegan/Easify-Android)
[![License: GPL-3.0](https://img.shields.io/badge/License-GPL3-yellow.svg)](LICENSE)

**GitLens** is a modern, responsive, and elegant Android application designed to thoroughly inspect GitHub users, developer profiles, and open-source repositories. Built entirely with **Jetpack Compose**, **Material 3**, and powered by the **EasifyAndroid** modular ecosystem.

---

## ✨ Features

- **🔍 Dual Real-Time Search**:
  - Seamlessly switch between searching **Users** and **Repositories** using the animated `EasifySegmentedControl`.
  - Debounced input (500ms) prevents rate-limiting and ensures instant, responsive queries.
  - Interactive shimmer placeholders (`Modifier.shimmer()`) while fetching results.

- **👤 In-Depth Developer Inspection**:
  - Full developer profile cards featuring avatar, name, handle, company, and location.
  - Expandable bio with clean show-more/show-less toggles (`EasifyExpandableText`).
  - Stat counters for Public Repositories, Followers, and Following with compact number formatting (e.g., `1.5K`, `24M`).
  - Complete list of public repositories with direct navigation to repository details.
  - Developer metadata: User ID, Blog/Website, Twitter/X handle, Email, Public Gists, and Account Creation date.

- **📦 Repository Inspection & Code Access**:
  - Full repository summary, topic tags, primary programming language badge, and default branch indicator.
  - Metrics cards displaying live Stars, Forks, Watchers, and Open Issues formatted with `Long.toCompactFormat()`.
  - **One-Tap Git Clone**: Copy HTTPS and SSH clone commands instantly to the clipboard with haptic confirmation.
  - Quick actions to open in external browser or share across applications.

- **🎬 Fluid Motion & Feedback**:
  - Bidirectional slide-and-fade navigation transitions on opening and closing screens.
  - Tactile iOS-style bounce-on-press animations (`Modifier.bounceClick`).
  - Tactile haptic feedback (`performClick()` on tap, `performSuccess()` on copy actions) via `easify-haptic`.
  - Automatic software keyboard dismissal on tap outside input fields (`Modifier.hideKeyboardOnTapOutside()`).

---

## 🛠️ Architecture & Tech Stack

- **UI & Toolkit**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with [Material 3](https://m3.material.io) surface hierarchy.
- **Language**: [Kotlin](https://kotlinlang.org) (Coroutines, StateFlow).
- **Dependency Injection**: [Koin](https://insert-koin.io) (ViewModel DSL, modules).
- **Networking**: [Ktor Client](https://ktor.io) with GitHub REST API headers and JSON serialization.
- **Image Loading**: [Coil 3](https://coil-kt.github.io/coil/) (Compose integration with OkHttp).
- **Navigation**: Type-Safe [Navigation Compose](https://developer.android.com/guide/navigation) with `kotlinx.serialization`.
- **Modular Utilities**: [EasifyAndroid](https://github.com/ahbandegan/Easify-Android):
  - `easify-ui`: `EasifySegmentedControl`, `bounceClick`, `EasifyExpandableText`, `shimmer`, `hideKeyboardOnTapOutside`.
  - `easify-format`: `Long.toCompactFormat()`, `toTimeAgo()`.
  - `easify-haptic`: `HapticFeedback.performClick()`, `HapticFeedback.performSuccess()`.
  - `easify-context`: `copyToClipboard()`, `openBrowser()`, `shareText()`, `toast()`.
  - `easify-network`: `createKtorClient`.

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug (or newer)
- JDK 17 or JDK 21
- Android SDK 24+ (Android 7.0+)

### Building from Source

1. Clone the repository:
   ```bash
   git clone https://github.com/ahbandegan/Git-Lens.git
   cd Git-Lens
   ```

2. Open the project in Android Studio or build via terminal:
   ```bash
   # On Windows PowerShell
   .\gradlew.bat assembleDebug

   # On macOS / Linux
   ./gradlew assembleDebug
   ```

3. Locate the generated APK:
   ```
   app/build/intermediates/apk/debug/app-debug.apk
   ```

---

## 📁 Project Structure

```
GitLens/
├── app/
│   ├── src/main/java/ir/amirhesambandegan/gitlens/
│   │   ├── base/               # Application class & Koin DI graph
│   │   ├── model/              # GitHub API serializable data models
│   │   ├── navigation/         # Type-safe routes & NavHost with transitions
│   │   │   ├── modals/         # Serializable route objects
│   │   │   └── screens/        # HomeScreen, UserDetailScreen, RepoDetailScreen
│   │   ├── repository/         # Ktor GitHub API client repository
│   │   ├── ui/theme/           # Material 3 Blue-Harmony Theme & Typography
│   │   └── viewModel/          # HomeViewModel, UserDetailViewModel, RepoDetailViewModel
├── gradle/
│   └── libs.versions.toml      # Version catalog dependencies
├── build.gradle.kts
└── settings.gradle.kts
```

---

## 👤 Author

Developed by **Amirhesam Bandegan**
- GitHub: [@ahbandegan](https://github.com/ahbandegan)
- Project: [Git-Lens](https://github.com/ahbandegan/Git-Lens)
- Ecosystem: [Easify-Android](https://github.com/ahbandegan/Easify-Android)

---

## 📄 License

This project is open-source and licensed under the [GPL-3.0 License](LICENSE).
