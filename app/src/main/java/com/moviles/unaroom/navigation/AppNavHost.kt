package com.moviles.unaroom.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.moviles.unaroom.core.UserMessages
import com.moviles.unaroom.data.AppContainer
import com.moviles.unaroom.data.Classroom
import com.moviles.unaroom.ui.components.AppNavigationBar
import com.moviles.unaroom.ui.components.UnaRoomScaffold
import com.moviles.unaroom.ui.screens.calendar.CalendarPlaceholderScreen
import com.moviles.unaroom.ui.screens.classrooms.ClassroomDetailScreen
import com.moviles.unaroom.ui.screens.classrooms.ClassroomsScreen
import com.moviles.unaroom.ui.screens.home.HomeScreen
import com.moviles.unaroom.ui.screens.login.LoginScreen
import com.moviles.unaroom.ui.screens.profile.ProfilePlaceholderScreen
import com.moviles.unaroom.ui.screens.reservation.ReservationCreateScreen
import com.moviles.unaroom.ui.theme.AppBackground
import com.moviles.unaroom.ui.theme.AppPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var successMessage by rememberSaveable { mutableStateOf<String?>(null) }

    NavHost(
        navController = navController,
        startDestination = AppDestinations.LOGIN,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = AppDestinations.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    successMessage = UserMessages.Navigation.LOGIN_SUCCESS_SNACKBAR
                    navController.navigate(AppDestinations.HOME) {
                        popUpTo(AppDestinations.LOGIN) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = AppDestinations.HOME) {
            val snackbarHostState = remember { SnackbarHostState() }
            UnaRoomScaffold(
                title = UserMessages.Screens.HOME_TITLE,
                showBack = false,
                onBackClick = { },
                snackbarHostState = snackbarHostState,
                bottomBar = {
                    MainBottomNavigationBar(
                        navController = navController,
                        currentRoute = currentRoute
                    )
                },
                showFab = true,
                fab = {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate(
                                AppDestinations.reservationCreateRoute(
                                    classroomId = null,
                                    classroomName = null
                                )
                            )
                        },
                        containerColor = AppPrimary,
                        contentColor = AppBackground
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = UserMessages.Accessibility.CREATE_RESERVATION
                        )
                    }
                },
                topBarActions = {
                    IconButton(
                        onClick = {
                            AppContainer.authRepository.clearLocalSession()
                            navController.navigate(AppDestinations.LOGIN) {
                                popUpTo(AppDestinations.HOME) {
                                    inclusive = true
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Logout,
                            contentDescription = UserMessages.Accessibility.LOGOUT,
                            tint = AppPrimary
                        )
                    }
                }
            ) { padding ->
                HomeScreen(
                    snackbarHostState = snackbarHostState,
                    successMessage = successMessage,
                    onSuccessMessageShown = { successMessage = null },
                    modifier = Modifier.padding(padding),
                    onBrowseClassroomsClick = {
                        navController.navigate(AppDestinations.CLASSROOMS) {
                            popUpTo(AppDestinations.HOME) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        composable(route = AppDestinations.CLASSROOMS) {
            val snackbarHostState = remember { SnackbarHostState() }
            UnaRoomScaffold(
                title = UserMessages.Screens.CLASSROOMS_TITLE,
                showBack = false,
                onBackClick = { },
                snackbarHostState = snackbarHostState,
                bottomBar = {
                    MainBottomNavigationBar(
                        navController = navController,
                        currentRoute = currentRoute
                    )
                },
                showFab = true,
                fab = {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate(
                                AppDestinations.reservationCreateRoute(
                                    classroomId = null,
                                    classroomName = null
                                )
                            )
                        },
                        containerColor = AppPrimary,
                        contentColor = AppBackground
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = UserMessages.Accessibility.CREATE_RESERVATION
                        )
                    }
                },
                topBarActions = {
                    IconButton(
                        onClick = {
                            AppContainer.authRepository.clearLocalSession()
                            navController.navigate(AppDestinations.LOGIN) {
                                popUpTo(AppDestinations.HOME) {
                                    inclusive = true
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Logout,
                            contentDescription = UserMessages.Accessibility.LOGOUT,
                            tint = AppPrimary
                        )
                    }
                }
            ) { padding ->
                ClassroomsScreen(
                    snackbarHostState = snackbarHostState,
                    modifier = Modifier.padding(padding),
                    successMessage = successMessage,
                    onSuccessMessageShown = { successMessage = null },
                    onClassroomClick = { classroom ->
                        navController.navigate(
                            AppDestinations.classroomDetailRoute(
                                id = classroom.id,
                                name = classroom.name,
                                capacity = classroom.capacity,
                                location = classroom.location
                            )
                        )
                    }
                )
            }
        }

        composable(
            route = "${AppDestinations.CLASSROOM_DETAIL}/{id}/{name}/{capacity}/{location}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType },
                navArgument("capacity") { type = NavType.IntType },
                navArgument("location") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val args = backStackEntry.arguments
            val classroom = Classroom(
                id = args?.getString("id").orEmpty(),
                name = args?.getString("name").orEmpty(),
                capacity = args?.getInt("capacity") ?: 0,
                location = args?.getString("location").orEmpty()
            )
            UnaRoomScaffold(
                title = classroom.name,
                showBack = true,
                onBackClick = { navController.popBackStack() },
                snackbarHostState = null,
                bottomBar = {
                    MainBottomNavigationBar(
                        navController = navController,
                        currentRoute = currentRoute
                    )
                }
            ) { padding ->
                ClassroomDetailScreen(
                    classroom = classroom,
                    onReserveClick = {
                        navController.navigate(
                            AppDestinations.reservationCreateRoute(
                                classroomId = classroom.id,
                                classroomName = classroom.name
                            )
                        )
                    },
                    modifier = Modifier.padding(padding)
                )
            }
        }

        composable(
            route = "${AppDestinations.RESERVATION_CREATE}?classroomId={classroomId}&classroomName={classroomName}",
            arguments = listOf(
                navArgument("classroomId") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("classroomName") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val classroomIdArg = backStackEntry.arguments?.getString("classroomId").orEmpty()
            val classroomNameArg = backStackEntry.arguments?.getString("classroomName").orEmpty()
            val snackbarHostState = remember { SnackbarHostState() }
            UnaRoomScaffold(
                title = UserMessages.Screens.RESERVATION_CREATE_TITLE,
                showBack = true,
                onBackClick = { navController.popBackStack() },
                snackbarHostState = snackbarHostState,
                bottomBar = {
                    MainBottomNavigationBar(
                        navController = navController,
                        currentRoute = currentRoute
                    )
                }
            ) { padding ->
                ReservationCreateScreen(
                    snackbarHostState = snackbarHostState,
                    initialClassroomId = classroomIdArg,
                    initialClassroomName = classroomNameArg,
                    modifier = Modifier.padding(padding)
                )
            }
        }

        composable(route = AppDestinations.CALENDAR) {
            UnaRoomScaffold(
                title = UserMessages.Screens.CALENDAR_TITLE,
                showBack = true,
                onBackClick = { navController.popBackStack() },
                snackbarHostState = null,
                bottomBar = {
                    MainBottomNavigationBar(
                        navController = navController,
                        currentRoute = currentRoute
                    )
                }
            ) { padding ->
                CalendarPlaceholderScreen(modifier = Modifier.padding(padding))
            }
        }

        composable(route = AppDestinations.PROFILE) {
            UnaRoomScaffold(
                title = UserMessages.Screens.PROFILE_TITLE,
                showBack = true,
                onBackClick = { navController.popBackStack() },
                snackbarHostState = null,
                bottomBar = {
                    MainBottomNavigationBar(
                        navController = navController,
                        currentRoute = currentRoute
                    )
                }
            ) { padding ->
                ProfilePlaceholderScreen(modifier = Modifier.padding(padding))
            }
        }
    }
}

@Composable
private fun MainBottomNavigationBar(
    navController: NavController,
    currentRoute: String?
) {
    AppNavigationBar(
        selectedTab = routeToMainTab(currentRoute),
        onHomeClick = {
            navController.navigate(AppDestinations.HOME) {
                popUpTo(AppDestinations.HOME) { inclusive = false }
                launchSingleTop = true
            }
        },
        onClassroomsClick = {
            navController.navigate(AppDestinations.CLASSROOMS) {
                popUpTo(AppDestinations.HOME) { inclusive = false }
                launchSingleTop = true
            }
        },
        onCalendarClick = {
            navController.navigate(AppDestinations.CALENDAR) {
                popUpTo(AppDestinations.HOME) { inclusive = false }
                launchSingleTop = true
            }
        },
        onProfileClick = {
            navController.navigate(AppDestinations.PROFILE) {
                popUpTo(AppDestinations.HOME) { inclusive = false }
                launchSingleTop = true
            }
        }
    )
}
