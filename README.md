# UnaRoom

Academic mobile app project for the course **Diseno y Programacion de Plataformas Moviles**.

UnaRoom is a beginner-friendly Android app foundation for a classroom reservation system, built with **Kotlin** and **Jetpack Compose**.

## Current Scope

- Jetpack Compose UI with Material 3
- Navigation Compose with multiple screens (Login, Classrooms, Reservations, Home, Calendar, Profile)
- Reusable UI components
- MVVM architecture (ViewModel + Repository)
- **Room local database** for offline classroom data
- **Firebase Cloud Messaging (FCM)** for push notifications
- Retrofit + OkHttp for REST API integration

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Navigation | Navigation Compose |
| Local DB | Room (SQLite) |
| Network | Retrofit 2 + OkHttp |
| Async | Kotlin Coroutines + Flow |
| Push | Firebase Cloud Messaging |
| Build | Android Gradle Plugin + KSP |

## Package Structure

Main package: `com.moviles.unaroom`

```text
app/src/main/java/com/moviles/unaroom/
├── UnaRoomApplication.kt          ← initialises AppContainer (Room needs a Context)
├── MainActivity.kt
├── UnaRoomApp.kt
├── core/
│   ├── AppConstants.kt            ← API URLs, tags
│   └── UserMessages.kt            ← all user-visible strings
├── data/
│   ├── Classroom.kt               ← domain model (used by UI)
│   ├── AppContainer.kt            ← manual DI container
│   ├── local/                     ← Room layer
│   │   ├── ClassroomEntity.kt     ← @Entity: one table row
│   │   ├── ClassroomDao.kt        ← @Dao: SQL operations as Kotlin functions
│   │   └── UnaRoomDatabase.kt     ← @Database: singleton Room instance
│   ├── remote/                    ← Retrofit layer
│   │   ├── ApiService.kt
│   │   ├── RetrofitClient.kt
│   │   └── model/
│   │       ├── AuthModels.kt      ← LoginRequest, UserDto, FcmTokenRequest
│   │       └── ReservationModels.kt
│   └── repository/
│       ├── ApiResult.kt
│       ├── AuthRepository.kt
│       ├── ClassroomRepository.kt ← Room + API (source of truth = Room)
│       ├── FcmTokenRepository.kt  ← sends token to .NET backend
│       └── ReservationRepository.kt
├── notifications/
│   ├── NotificationHelper.kt                    ← shows system notifications
│   └── UnaRoomFirebaseMessagingService.kt        ← handles FCM events
└── ui/
    ├── screens/
    │   ├── classrooms/
    │   │   ├── ClassroomsScreen.kt
    │   │   ├── ClassroomsViewModel.kt ← collects Room Flow
    │   │   └── ClassroomDetailScreen.kt
    │   └── login/
    │       ├── LoginScreen.kt
    │       └── LoginViewModel.kt      ← registers FCM token after login
    └── theme/
```

---

## Room Architecture

Room is Android's official SQLite abstraction library. It removes all the boilerplate SQL code and lets you work with Kotlin classes and coroutines.

### Three key classes

| Class | Annotation | Role |
|---|---|---|
| `ClassroomEntity` | `@Entity` | Maps to one table in SQLite |
| `ClassroomDao` | `@Dao` | Declares queries as Kotlin functions |
| `UnaRoomDatabase` | `@Database` | Singleton — the database itself |

### Data flow (reactive)

```
Room DB ──Flow──▶ ClassroomRepository ──Flow──▶ ClassroomsViewModel ──StateFlow──▶ ClassroomsScreen
```

1. `ClassroomDao.getAllClassrooms()` returns a **`Flow<List<ClassroomEntity>>`**.
2. `ClassroomRepository.getClassroomsFlow()` maps entities → domain models.
3. `ClassroomsViewModel` collects the Flow and exposes a `StateFlow<ClassroomsUiState>`.
4. `ClassroomsScreen` calls `collectAsStateWithLifecycle()` — Compose recomposes automatically whenever the list changes.

### Seeding

On first launch, `ClassroomRepository.seedIfEmpty()` inserts five sample classrooms so the app shows data without a running backend. The `IGNORE` conflict strategy makes this safe to call on every launch.

### Schema changes

If you add a column or a new entity, increment `version` in `@Database` and provide a `Migration` object, or use `fallbackToDestructiveMigration()` during development.

---

## Firebase Cloud Messaging (FCM)

FCM is a cross-platform messaging service. It lets a server send push notifications to mobile devices.

### How the token flow works

```
┌──────────────────────┐        ┌─────────────────────┐        ┌────────────────┐
│   Android Device     │        │   .NET Backend API  │        │    Firebase    │
│                      │        │                     │        │                │
│ 1. App starts        │        │                     │        │                │
│    Firebase assigns  │        │                     │        │                │
│    a unique token ───┼──────▶ │ 2. POST /api/auth/  │        │                │
│                      │        │    fcm-token        │        │                │
│                      │        │    stores token in  │        │                │
│                      │        │    DB               │        │                │
│                      │        │                     │        │                │
│                      │        │ 3. Server wants to  │        │                │
│                      │        │    notify user ─────┼──────▶ │ 4. Firebase    │
│                      │        │                     │        │    delivers    │
│ 5. onMessageReceived │◀───────┼─────────────────────┼────────┼── the push     │
│    shows notification│        │                     │        │                │
└──────────────────────┘        └─────────────────────┘        └────────────────┘
```

### Android-side classes

| Class | Responsibility |
|---|---|
| `UnaRoomFirebaseMessagingService` | Receives `onNewToken` and `onMessageReceived` callbacks |
| `FcmTokenRepository` | Sends the token to the backend after login |
| `NotificationHelper` | Creates the notification channel and shows notifications |
| `LoginViewModel` | Triggers token registration after a successful login |

### Token lifecycle

- `onNewToken` fires when the app first installs **and** whenever Firebase rotates the token.
- `LoginViewModel.registerFcmTokenWithBackend()` fires after every successful login to ensure the backend always has a valid token.

---

## Setup

### Prerequisites

- Android Studio (latest stable recommended)
- Android SDK installed from Android Studio
- JDK managed by Android Studio (default setup is fine)

### google-services.json

This file connects the app to a Firebase project. It is **not committed to version control** because it contains project credentials.

To obtain it:
1. Open [Firebase Console](https://console.firebase.google.com).
2. Select or create a project.
3. Add an Android app with package name `com.moviles.unaroom`.
4. Download `google-services.json` and place it at `app/google-services.json`.

A `google-services.example.json` placeholder is included in the repo so students can see the expected file shape.

> **Class projects:** Each student/team should use their own Firebase project, or the instructor can share a class project. Never commit the real `google-services.json` to a public repository.

### Open the project

1. Open Android Studio.
2. Select **Open** → choose the `UnaRoom` folder.
3. Wait for Gradle sync to finish.

### Run on emulator or device

1. Create or start an Android emulator (Device Manager), or connect a physical device with USB debugging.
2. In Android Studio, select the `app` configuration.
3. Click **Run**.

> **Note on KSP version:** If Gradle cannot resolve the KSP plugin, update `ksp` in `gradle/libs.versions.toml` to match your Kotlin version. The format is `{kotlinVersion}-{kspPatch}`, e.g. `2.2.10-2.0.2`. Check available versions at [KSP releases](https://github.com/google/ksp/releases).

