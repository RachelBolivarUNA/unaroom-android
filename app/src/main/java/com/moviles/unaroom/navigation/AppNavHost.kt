package com.moviles.unaroom.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moviles.unaroom.ui.screens.classrooms.ClassroomsScreen
import com.moviles.unaroom.ui.screens.login.LoginScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    var successMessage by rememberSaveable { mutableStateOf<String?>(null) }

    NavHost(
        navController = navController,
        startDestination = AppDestinations.LOGIN,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = AppDestinations.LOGIN) {
            LoginScreen(
                onLoginClick = {
                    successMessage = "Login successful"
                    navController.navigate(AppDestinations.CLASSROOMS) {
                        popUpTo(AppDestinations.LOGIN) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = AppDestinations.CLASSROOMS) {
            ClassroomsScreen(
                successMessage = successMessage,
                onSuccessMessageShown = {
                    successMessage = null
                },
                onLogoutClick = {
                    navController.navigate(AppDestinations.LOGIN) {
                        popUpTo(AppDestinations.CLASSROOMS) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}
