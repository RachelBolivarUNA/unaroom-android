# UnaRoom

Academic mobile app project for the course **Diseno y Programacion de Plataformas Moviles**.

UnaRoom is a beginner-friendly Android app foundation for a classroom reservation system, built with **Kotlin** and **Jetpack Compose**.

## Current Scope (UI Foundation)

This repository currently includes:

- Jetpack Compose UI with Material 3
- Navigation Compose with two screens:
  - `login`
  - `classrooms`
- Reusable UI components (`AppTextField`, `AppButton`)
- Mock classroom model and hardcoded sample data
- Clean, simple MVVM-inspired folder structure (without ViewModels yet)

This stage intentionally does **not** include:

- Backend integration
- API calls
- Database persistence
- Authentication logic

## Tech Stack

- Kotlin
- Android Gradle Plugin
- Jetpack Compose (Material 3)
- Navigation Compose

## Package Structure

Main package: `com.moviles.unaroom`

```text
app/src/main/java/com/moviles/unaroom/
├── MainActivity.kt
├── UnaRoomApp.kt
├── data/
│   └── Classroom.kt
├── navigation/
│   ├── AppDestinations.kt
│   └── AppNavHost.kt
├── ui/
│   ├── components/
│   │   ├── AppButton.kt
│   │   └── AppTextField.kt
│   ├── screens/
│   │   ├── classrooms/
│   │   │   └── ClassroomsScreen.kt
│   │   └── login/
│   │       └── LoginScreen.kt
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
```

## Setup

### 1) Prerequisites

- Android Studio (latest stable recommended)
- Android SDK installed from Android Studio
- JDK managed by Android Studio (default setup is fine)

### 2) Open the project

1. Open Android Studio.
2. Select **Open**.
3. Choose this folder: `UnaRoom`.
4. Wait for Gradle sync to finish.

### 3) Run on emulator or device

1. Create or start an Android emulator (Device Manager), or connect a physical Android device with USB debugging.
2. In Android Studio, select the `app` configuration.
3. Click **Run**.
