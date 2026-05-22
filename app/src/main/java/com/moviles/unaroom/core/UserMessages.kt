package com.moviles.unaroom.core

/**
 * Central user-visible English strings (ViewModels + repositories).
 * Prefer migrating these to `strings.xml` if you add localization.
 */
object UserMessages {

    const val TAP_TO_RETRY_SUFFIX = ". Tap to retry"

    const val BRAND_NAME = "UnaRoom"

    const val GENERIC_CLASSROOM_LABEL = "Classroom"

    object LoginUi {
        const val HEADER_SUBTITLE = "Classroom Reservation"
        const val EMAIL_LABEL = "Email"
        const val EMAIL_PLACEHOLDER = "student@university.edu"
        const val PASSWORD_LABEL = "Password"
        const val BUTTON_LOGIN = "Login"
        const val BUTTON_LOADING = "Loading..."
    }

    object Auth {
        const val LOGIN_EMPTY_FIELDS = "Please fill in all fields"
        const val INVALID_CREDENTIALS = "Invalid email or password"
        const val LOGIN_FAILED = "Login failed"
        const val LOGIN_JSON_MISMATCH =
            "Login response did not match the app (check id/name/email types)."
        const val LOGIN_FOOTER = "Sign in with your backend credentials"
    }

    object Home {
        const val SECTION_UPCOMING = "Upcoming reservations"
        const val LOADING_RESERVATIONS = "Loading your reservations..."
        const val SUBTITLE = "Here's what's coming up in UnaRoom."
        const val BROWSE_CLASSROOMS = "Browse classrooms"
        const val EMPTY_RESERVATIONS_TITLE = "No upcoming reservations"
        const val EMPTY_RESERVATIONS_HINT =
            "Use the + button to book a room, or open the Classrooms tab."
        const val SESSION_EXPIRED = "Session expired. Please log in again."

        fun greeting(displayName: String): String {
            val n = displayName.trim()
            return if (n.isEmpty()) "Hello" else "Hello, $n"
        }
    }

    object Classrooms {
        const val LOADING = "Loading classrooms..."
        const val EMPTY = "No classrooms available"
        const val FETCH_FAILED = "Could not fetch classrooms"
        const val JSON_MISMATCH =
            "Classrooms JSON did not match the app (e.g. id must be a string Guid)."
    }

    object Reservation {
        const val SUMMARY_CHOOSE_CLASSROOM = "Choose a classroom"
        const val SUMMARY_FOR_ROOM_PREFIX = "Creating reservation for "
        const val SUMMARY_FOR_ROOM_FALLBACK = "Creating reservation for this room"
        const val SUMMARY_HINT_NO_ROOM = "Select a room below, then pick date and times."
        const val SUMMARY_HINT_WITH_ROOM =
            "Pick a date and start / end times. Your account is sent automatically as userId."
        const val FIELD_DATE = "Date"
        const val FIELD_DATE_PLACEHOLDER = "Tap to choose date"
        const val FIELD_START = "Start time"
        const val FIELD_END = "End time"
        const val FIELD_TIME_PLACEHOLDER = "Tap to choose time"
        const val BUTTON_CREATING = "Creating..."
        const val BUTTON_CREATE = "Create reservation"
        const val SESSION_EXPIRED = "Session expired. Please log in again."
        const val VALIDATION_INCOMPLETE = "Please choose a classroom, date, and times."
        const val SUCCESS_CREATED = "Reservation created successfully"
    }

    object Network {
        const val COULD_NOT_CONNECT = "Could not connect to server"
        const val OFFLINE_BANNER = "You're offline – showing local data"
    }

    object Navigation {
        const val LOGIN_SUCCESS_SNACKBAR = "Login successful"
    }

    object ClassroomDetail {
        const val NAME_PREFIX = "Name: "
        const val CAPACITY_PREFIX = "Capacity: "
        const val LOCATION_PREFIX = "Location: "
        const val RESERVE_BUTTON = "Create reservation for this room"
    }

    object Placeholders {
        const val CALENDAR = "Calendar will be implemented here."
        const val PROFILE = "Profile will be implemented here."
    }

    object NavigationBar {
        const val HOME = "Home"
        const val CLASSROOMS = "Classrooms"
        const val CALENDAR = "Calendar"
        const val PROFILE = "Profile"
    }

    object Picker {
        const val OK = "OK"
        const val CANCEL = "Cancel"
        const val RETRY = "Retry"
    }

    object ReservationSummary {
        const val CHANGE_CLASSROOM = "Change classroom"
    }

    object Accessibility {
        const val BACK = "Back"
        const val CREATE_RESERVATION = "Create reservation"
        const val LOGOUT = "Logout"
    }

    object Screens {
        const val HOME_TITLE = "Home"
        const val CLASSROOMS_TITLE = "Available Classrooms"
        const val RESERVATION_CREATE_TITLE = "Create reservation"
        const val CALENDAR_TITLE = "Calendar"
        const val PROFILE_TITLE = "Profile"
    }

    object ReservationApi {
        const val CONFLICT = "Reservation conflict. Choose another time."
        const val CREATE_FAILED = "Could not create reservation"
        const val CREATE_JSON_MISMATCH = "Reservation response JSON did not match the app."
        const val LIST_FAILED = "Could not load reservations"
        const val LIST_JSON_MISMATCH = "Reservations response JSON did not match the app."
    }
}
