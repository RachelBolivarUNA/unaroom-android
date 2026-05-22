package com.moviles.unaroom.navigation

/**
 * Bottom navigation tabs. Route matching lives next to [AppDestinations].
 */
enum class MainTab {
    Home,
    Classrooms,
    Calendar,
    Profile
}

fun routeToMainTab(route: String?): MainTab {
    val r = route.orEmpty()
    return when {
        r == AppDestinations.HOME ||
            r.startsWith(AppDestinations.RESERVATION_CREATE) -> MainTab.Home
        r == AppDestinations.CLASSROOMS ||
            r.startsWith("${AppDestinations.CLASSROOM_DETAIL}/") -> MainTab.Classrooms
        r == AppDestinations.CALENDAR -> MainTab.Calendar
        r == AppDestinations.PROFILE -> MainTab.Profile
        else -> MainTab.Home
    }
}
